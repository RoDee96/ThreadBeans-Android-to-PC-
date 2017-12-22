package com.threadbeans2.applicationNetwork;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.threadbeans2.R;
import com.threadbeans2.activity.MainActivity;
import com.threadbeans2.beans.FileBean;
import com.threadbeans2.beans.GridItemBeans;
import com.threadbeans2.broadcast.BroadcastListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Rohit on 8/7/2017.
 */

public class SendFile {
    public static boolean cancelTransfer;
    public static boolean stoploop = false;

    static InputStream is;
    static OutputStream os;

    static FileInputStream fis;

    static ObjectOutputStream oos;
    static ObjectInputStream ois;

    static ServerSocket serverSocket;
    static Socket socket;

    static ArrayList<GridItemBeans> items;

    static ProgressBar progBar;

    private final static String TAG = "456";

    public static void initiateSend(ArrayList<GridItemBeans> obj) {
        items = obj;
        new AsynSendFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class AsynSendFile extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            initiate();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (MainActivity.longClickFuncOn == true) {
                MainActivity.longClickFuncOn = false;
                MainActivity.countSelected = 0;
                Snackbar.make(((Activity) MainActivity.ch.getContext()).findViewById(R.id.drawer_layout), "Selected: " + MainActivity.countSelected, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                ((Activity) MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.addListeners();
                        MainActivity.undoLongClickActions();
                    }
                });
            }
        }

        private static void initiate() {
            stoploop = false;

            progBar = (ProgressBar) MainActivity.receive_dialog.findViewById(R.id.progressBar);

            for (int i = 0; i < items.size(); i++) {
                if (stoploop == true) {
                    break;
                }
                if (items.get(i).isChecked()) {
                    File file = new File(items.get(i).getPath());
                    try {
                        prepareToSend(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private static void prepareToSend(File file) throws Exception {
            cancelTransfer = false;
            BroadcastListener.oos.writeObject((String) "sending");
            BroadcastListener.oos.flush();
            Log.d(TAG, "    SendFile: ServerSocket to be created");

            serverSocket = new ServerSocket(6000);
            socket = serverSocket.accept();

            Log.d(TAG, "    SendFile: ServerSocket created");

            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            FileBean fileBean = new FileBean();
            fileBean.setFilename(file.getName());
            fileBean.setFilesize(file.length());

            oos.writeObject(fileBean);
            oos.flush();

            System.out.println("Transfer Initiated:");
            System.out.println("    File name is: " + file.getName());
            System.out.println("    File size is: " + file.length());

            fis = new FileInputStream(file);

            updateUI(fileBean, file);
        }

        private static void updateUI(FileBean fileBean, File file) throws Exception {
            final FileBean fileBean_ = fileBean;

            ((Activity)MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) MainActivity.receive_dialog.findViewById(R.id.nameView)).setText(fileBean_.getFilename());
                }
            });

            send(fileBean, file);
        }


        private static void send(FileBean fileBean, File file) {
            try {
                System.out.println("    Sending...");

                os = socket.getOutputStream();
                is = socket.getInputStream();

                startingUIActions(file);

                int count;
                byte bytes[] = new byte[1024];
                for (double bytesRead = 1; true; bytesRead += 1024) {
                    if (cancelTransfer == true) {
                        break;
                    }

                    if ((count = fis.read(bytes)) > 0) {
                        os.write(bytes, 0, count);
                        os.flush();
                    } else {
                        break;
                    }

                    int percent = (int) ((bytesRead / file.length()) * 100);

                    updateProgressBar(percent);
                }

                if (cancelTransfer == true) {
                    Log.d(TAG, "    SendFIle: Transfer cancelled by user");
                    displayMessageWithToast("Cancelled");
                } else {
                    Log.d(TAG, "    SendFIle: File tranferred successfully");
                    displayMessageWithToast("Sent");
                }

            } catch (Exception e) {
                Log.d(TAG, "    SendFile: CancelTransfer = " + cancelTransfer);
                e.printStackTrace();

                if (cancelTransfer == true) {
                    Log.d(TAG, "    SendFile: Transfer cancelled by user");
                }else{
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            } finally {
                try {
                    postUIActions();

                    fis.close();
                    ois.close();
                    oos.close();

                    is.close();
                    os.close();
                    socket.close();
                    serverSocket.close();

                    Log.d(TAG, "    SendFile: Streams closed successfully");

                    BroadcastListener.oos.writeObject("fileSent");
                    oos.flush();
                    Log.d(TAG, "    SendFIle: FILESENT sent");
                    Thread.sleep(1000);

                } catch (Exception e) {
                    Log.d(TAG, "    SendFIle: Sender streams or server not closed!");
                    e.printStackTrace();
                }
            }
        }

        //utility methods
        private static void startingUIActions(File file) {
            cancelTransfer = false;

            ((Activity) MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.receive_dialog.show();
                }
            });
        }

        private static void postUIActions() {
            ((Activity) MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.receive_dialog.dismiss();
                }
            });
        }

        private static void updateProgressBar(int percent) {
            progBar.setProgress(percent);
        }

        private static void displayMessageWithToast(String msg_) {
            final String msg = msg_;

            ((Activity) MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.ch.getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}
