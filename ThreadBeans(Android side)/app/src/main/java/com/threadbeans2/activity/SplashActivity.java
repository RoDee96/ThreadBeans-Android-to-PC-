package com.threadbeans2.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.threadbeans2.R;
import com.threadbeans2.helper.ContextHelper;
import com.threadbeans2.jettyServer.EmbeddedJettyMain;
import com.threadbeans2.myWifiManager.MyWifiManager;


public class SplashActivity extends Activity {

    Button pcButton, andriodButton;

    ContextHelper ch;

    Dialog dialog_Wifi;
    Button dialog_wifi_ok_button, dialog_wifi_cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                pcButton.setAnimation(animFadeIn);
                andriodButton.setAnimation(animFadeIn);

                //andriodButton.setVisibility(Button.VISIBLE);
                pcButton.setVisibility(Button.VISIBLE);
                View view = findViewById(R.id.dot);
                view.setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }

    private void init() {
        initDialog();

        pcButton = (Button) findViewById(R.id.pcButton);
        andriodButton = (Button) findViewById(R.id.androidButton);
        andriodButton.setVisibility(View.GONE);

        pcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 0);
                if (checkPermission()) {
                    pcButtonActions();
                } else {
                    Toast.makeText(SplashActivity.this, "Permissions needed!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        andriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                andriodButtonActions();
            }
        });
    }

    private boolean checkPermission(){
        boolean flagPermission0 = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean flagPermission1 = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean flagPermission2 = (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED);
        boolean flagPermission3 = (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED);
        boolean flagPermission4 = (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED);
        boolean flagPermission5 = (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED);
        if(flagPermission0 && flagPermission1 && flagPermission2 && flagPermission3 && flagPermission4 && flagPermission5){
            return true;
        }else{
            return false;
        }

    }

    private void initDialog() {
        dialog_Wifi = new Dialog(SplashActivity.this);
        dialog_Wifi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Wifi.setContentView(R.layout.dialog_wifi_layout);

        dialog_wifi_ok_button = (Button) dialog_Wifi.findViewById(R.id.okayButton);
        dialog_wifi_cancel_button = (Button) dialog_Wifi.findViewById(R.id.cancelButton);

        dialog_wifi_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWifiManager.setHotspotName("ThreadBeans", SplashActivity.this);
                MyWifiManager.EnableWifiHotspot(SplashActivity.this);
                Toast.makeText(SplashActivity.this, "Done", Toast.LENGTH_SHORT).show();
                dialog_Wifi.dismiss();

                Intent i = new Intent(SplashActivity.this, InstructionActivity.class);
                startActivity(i);
                finish();
            }
        });

        dialog_wifi_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWifiManager.DisableWifiHotspot(SplashActivity.this);
                Toast.makeText(SplashActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                dialog_Wifi.dismiss();
            }
        });
    }

    private void pcButtonActions() {
        dialog_Wifi.show();
    }

    private void andriodButtonActions() {

    }

    @Override
    protected void onDestroy() {
        try {
            EmbeddedJettyMain.server.stop();
            EmbeddedJettyMain.server.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
