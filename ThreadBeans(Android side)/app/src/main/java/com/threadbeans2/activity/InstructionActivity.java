package com.threadbeans2.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.threadbeans2.R;

import com.threadbeans2.broadcast.BroadcastListener;
import com.threadbeans2.helper.ContextHelper;
import com.threadbeans2.jettyServer.EmbeddedJettyMain;


public class InstructionActivity extends Activity {

    public static ContextHelper ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        ch = new ContextHelper();
        ch.setContext(this);

        setContentView(R.layout.activity_instruction);


    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EmbeddedJettyMain.server.stop();
            EmbeddedJettyMain.server.destroy();

            BroadcastListener.AsyncCaller.kill_waiting();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        new AsyncCaller().execute();
    }


    private class AsyncCaller extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread

        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            BroadcastListener.startListening();
            Log.d("456", "doInBackground: port 8080 opened!");
            try {
                EmbeddedJettyMain.main(null);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread
        }

    }

}
