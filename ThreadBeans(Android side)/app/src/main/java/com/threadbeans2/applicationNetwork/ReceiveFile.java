package com.threadbeans2.applicationNetwork;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.threadbeans2.R;
import com.threadbeans2.activity.MainActivity;
import com.threadbeans2.beans.FileBean;
import com.threadbeans2.broadcast.BroadcastListener;
import com.threadbeans2.helper.StorageHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Rohit on 8/2/2017.
 */

public class ReceiveFile {
    static InputStream is;
    static OutputStream os;

    static FileOutputStream fos;

    static ObjectOutputStream oos;
    static ObjectInputStream ois;

    static Socket socket;

    public static boolean cancelTransfer;

    public static void initiateReceive() {
        new AsyncFileReceive().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class AsyncFileReceive extends AsyncTask<Void, Void, Void> {
        static ProgressBar progBar;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((Activity) MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.receive_dialog.show();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                initiate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            new BroadcastListener.AsyncReceiver();
            ((Activity)MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.receive_dialog.dismiss();
                }
            });

        }

        public static void initiate() throws Exception {
            Log.d("456", "      ReceiveFile: initiate: ServerIP: "+BroadcastListener.inet.getHostAddress());
            socket = new Socket(BroadcastListener.inet.getHostAddress(), 6000);

            is = socket.getInputStream();
            os = socket.getOutputStream();

            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);

            progBar = (ProgressBar) MainActivity.receive_dialog.findViewById(R.id.progressBar);
            prepareToReceive();
        }

        private static void prepareToReceive() throws Exception {
            FileBean fileBean = (FileBean) ois.readObject();

            Log.d("456", "      prepareToReceive: At Location: " + StorageHelper.storageLocation);
            Log.d("456", "      prepareToReceive: FileName: " + fileBean.getFilename());
            Log.d("456", "      prepareToReceive: FileSize: " + fileBean.getFilesize());

            fos = new FileOutputStream(StorageHelper.storageLocation + fileBean.getFilename());

            updateUI(fileBean);
        }

        private static void updateUI(FileBean fileBean) throws Exception {
            final FileBean fileBean_ = fileBean;

            ((Activity)MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) MainActivity.receive_dialog.findViewById(R.id.nameView)).setText(fileBean_.getFilename());
                }
            });

            receive(fileBean);
        }

        private static void receive(FileBean fileBean) {

            try {
                cancelTransfer = false;
                byte[] bytes = new byte[1024];
                int count;
                for (double bytesRead = 1; true; bytesRead+=1024) {
                    if (cancelTransfer == true) {
                        Log.d("456", "      ReceiveFile: Break from receive loop: "+cancelTransfer);
                        Thread.sleep(1000);
                        break;
                    }
                    int percent = (int) (int)(( bytesRead / fileBean.getFilesize() ) * 100);
                    updateProgressBar(percent);
                    //Log.d("475", "receive: "+percent);

                    if ((count = is.read(bytes)) > 0) {
                        fos.write(bytes, 0, count);
                        fos.flush();
                    } else

                        break;

                }

                if (cancelTransfer == false) {//sucessfull receving
                    try {
                        Log.d("456", "      ReceiveFile: File received Successfully!");
                        displayMessageWithToast("Received");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (cancelTransfer == true) {//unsucessful receving
                    try {
                        Log.d("456", "      ReceiveFile: transfer cancelled by user!");
                        displayMessageWithToast("Cancelled");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("456", "      ReceiveFile: (error) File not received!");
            } finally {
                try {
                    socket.close();
                    ois.close();
                    oos.close();
                    is.close();
                    os.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("456", "      ReceiveFile: Socket/Streams not closed!");
                }
            }
        }

        private static void displayMessageWithToast(String msg_) {
            final String msg = msg_;

            ((Activity)MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.ch.getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private static void updateProgressBar(int percent){
            progBar.setProgress(percent);
        }

    }

}
