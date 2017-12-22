package com.threadbeans2.activity;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.threadbeans2.R;
import com.threadbeans2.adapter.GridAdapter;
import com.threadbeans2.applicationNetwork.ReceiveFile;
import com.threadbeans2.applicationNetwork.SendFile;
import com.threadbeans2.beans.GridItemBeans;
import com.threadbeans2.broadcast.BroadcastListener;
import com.threadbeans2.helper.ContextHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int count;//count for closing

    public static int countSelected = 0;

    public static ContextHelper ch;
    private static GridView gridView;
    private static GridAdapter adapter;
    public static ArrayList<GridItemBeans> items = new ArrayList<>();

    public static  Toolbar toolbar;

    private static String TAG = "456";

    private static String path;
    private final String start = "start";
    private static TextView pathView;

    public static boolean longClickFuncOn;

    public static Dialog receive_dialog;
    private Button dialogCancelButton;

    private void init() {
        ch = new ContextHelper();
        ch.setContext(this);

        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new GridAdapter(this, R.layout.grid_item, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!items.get(position).is_path())
                    countSelected++;

                boolean existsAnyNonDirectory = false;
                for (int i = 0; i < items.size(); i++) {
                    if (!items.get(i).is_path())
                        existsAnyNonDirectory = true;
                }

                if (existsAnyNonDirectory) {
                    Snackbar.make(view, "Selected: " + countSelected, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if (!items.get(position).is_path()) {
                        items.get(position).setChecked(true);
                    }
                    onLongClickListItem();
                    return true;
                }
                return false;
            }
        });

        pathView = (TextView) findViewById(R.id.pathView);

    }

    private void initDialog(){
        receive_dialog = new Dialog(MainActivity.this);
        receive_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        receive_dialog.setContentView(R.layout.dialog_receive);
        receive_dialog.setCancelable(false);

        dialogCancelButton = (Button) receive_dialog.findViewById(R.id.cancel_Button);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "MainActivity: ReceiveDialog: Cancel clicked");
                receive_dialog.dismiss();
                destroyFileServerSocket();
                ReceiveFile.cancelTransfer = true;
            }
        });
    }

    private void initMaterialDesign() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Connected to: " + BroadcastListener.inet.getHostName());
        toolbar.setBackgroundColor(Color.parseColor("#1f201f"));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabButtonActions();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void onLongClickListItem() {
        longClickFuncOn = true;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!items.get(position).is_path()) {
                    if (!items.get(position).isChecked()) {
                        items.get(position).setChecked(true);
                        Log.d(TAG, "onItemClick: " + items.get(position).isChecked());
                        countSelected++;
                        Snackbar.make(view, "Selected: " + countSelected, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        items.get(position).setChecked(false);
                        countSelected--;
                        Snackbar.make(view, "Selected: " + countSelected, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.d(TAG, "onItemClick: " + items.get(position).isChecked());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).is_path())
                items.get(i).setVisible(true);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 0);

        countSelected = 0;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMaterialDesign();
        initDialog();

        init();
        initFillValues();
        addListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        count = 1;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (longClickFuncOn == true) {
                longClickFuncOn = false;
                countSelected = 0;
                Snackbar.make(findViewById(R.id.drawer_layout), "Selected: " + MainActivity.countSelected, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addListeners();
                undoLongClickActions();
            } else if (path.equals(start) && count == 1) {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                count--;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 1;
                    }
                }, 3000);

            } else if (path.equals(start) && count == 0) {
                try {
                    BroadcastListener.oos.writeObject((String)"exit");
                    BroadcastListener.oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BroadcastListener.AsyncCaller.kill_con_returnToSplash();
            } else {
                File file = new File(path);
                path = file.getParent();
                pathView.setText(path);
                Log.d(TAG, "onKeyDown: " + path);

                if (path.equals("/mnt")) {
                    path = start;
                    pathView.setText("/");
                    initFillValues();
                } else {
                    fillValues();
                }

            }
        }

    }

    public static void undoLongClickActions() {
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).is_path()) {
                items.get(i).setVisible(false);
                items.get(i).setChecked(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fabButtonActions() {
        if(longClickFuncOn){
            SendFile.initiateSend(items);
        }else {
            Snackbar.make(findViewById(R.id.fab), "Select items to Send", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void initFillValues() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        Log.d(TAG, "initFillValues: start");
        items.clear();
        this.path = start;

        File file_1 = new File(Environment.getRootDirectory().getParent() + "/mnt/sdcard");
        File file_2 = new File(Environment.getRootDirectory().getParent() + "/mnt/extSdCard");


        if (!file_2.isDirectory()) {
            GridItemBeans g = new GridItemBeans(file_1.getName(), file_1.getAbsolutePath(), file_1.isDirectory());
            items.add(g);
        } else {
            GridItemBeans g1 = new GridItemBeans("Internal Storage", file_1.getAbsolutePath(), file_1.isDirectory());
            GridItemBeans g2 = new GridItemBeans("External SD Card", file_2.getAbsolutePath(), file_2.isDirectory());

            items.add(g1);
            items.add(g2);
        }
        adapter.notifyDataSetChanged();
    }

    private static void fillValues() {
        items.clear();
        Log.d(TAG, "fillValues: " + path);
        File file = new File(path);

        File[] files = file.listFiles();

        //if directory is empty...
        if (files == null) {

            Toast.makeText(MainActivity.ch.getContext(), "Empty directory", Toast.LENGTH_SHORT).show();
        } else
            for (int x = 0; x < files.length; x++) {
                GridItemBeans g = new GridItemBeans(files[x].getName(), files[x].getAbsolutePath(), files[x].isDirectory());
                items.add(g);
            }
        adapter.notifyDataSetChanged();
    }

    public static void addListeners() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.get(position).is_path()) {
                    path = items.get(position).getPath();
                    Log.d("123", "onItemClick: " + path);
                    pathView.setText(path);
                    fillValues();
                } else {
                    Log.d("123", "onItemClick: " + path);
                }

            }
        });


    }

    private void destroyFileServerSocket() {
        try{
            BroadcastListener.oos.writeObject("Cancel Sending");
            BroadcastListener.oos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
