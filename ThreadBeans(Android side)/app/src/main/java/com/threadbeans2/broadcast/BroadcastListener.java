package com.threadbeans2.broadcast;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.threadbeans2.activity.InstructionActivity;
import com.threadbeans2.activity.MainActivity;
import com.threadbeans2.activity.SplashActivity;
import com.threadbeans2.applicationNetwork.ReceiveFile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Rohit on 7/15/2017.
 */

public class BroadcastListener {
    public static InetAddress inet = null;

    private static ServerSocket serverSocket;
    public static Socket socket;

    public static ObjectInputStream ois;
    public static ObjectOutputStream oos;

    static boolean connected;

    public static void startListening() {
        new AsyncCaller().execute();
    }

    public static class AsyncCaller extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                serverSocket = new ServerSocket(5000);
                socket = serverSocket.accept();

                Log.d("456", "Listener: connection established at 6000");

                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                Log.d("456", "Listener: streams up at 6000");

                androidStream();

                connected = true;

                Intent i = new Intent(InstructionActivity.ch.getContext(), MainActivity.class);
                InstructionActivity.ch.getContext().startActivity(i);
                ((Activity) InstructionActivity.ch.getContext()).finish();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new AsyncReceiver().execute();
                }
            },1000);


        }

        public static void androidStream(){
            try {
                inet = (InetAddress) ois.readObject();
                Log.d("456", "Listener: inet of PC received at 6000");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String name = Build.MODEL;
                oos.writeObject(name);
                oos.flush();
                Log.d("456", "Listener: model name of android received at 6000");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public static void kill_waiting() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void kill_con_returnToSplash() {
            try {
                socket.close();
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            connected = false;
            Log.d("456", "doInBackground: Socket closed at 5000!");
            Log.d("456", "doInBackground: All streams closed, GoodBye!");

            Intent i = new Intent(MainActivity.ch.getContext(), SplashActivity.class);
            (MainActivity.ch.getContext()).startActivity(i);
            ((Activity) MainActivity.ch.getContext()).finish();
        }
    }

    public static class AsyncReceiver extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
                while(true) {
                    Log.d("456", "Listener: Receiver started at 5000!");
                    String read = "";
                    try {
                        read = (String) ois.readObject();
                        Log.d("456", "Listener: "+new String(read).toUpperCase() + " received");
                    } catch (Exception e) {
                        Log.d("456", "Listener: Cannot read from 5000 stream");
                    }

                    if (read.equals("sending")) {
                        try {
                            ((Activity) MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ReceiveFile.initiateReceive();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (read.equals("Cancel Receiving")) {
                        ReceiveFile.cancelTransfer = true;
                        ((Activity)MainActivity.ch.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.receive_dialog.dismiss();
                            }
                        });
                        Log.d("456", "BroadcastListener: CancelTranfer = " + ReceiveFile.cancelTransfer);
                    }

                    if (read.equals("exit")) {
                        try {
                            AsyncCaller.kill_con_returnToSplash();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            break;
                        }

                    }
                    Log.d("456", "BroadcastListener: Receiver: end of an iteration!");
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }


    }

}
