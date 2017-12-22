package com.threadbeans2.myWifiManager;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Rohit on 7/10/2017.
 */

public class MyWifiManager {


    public static void EnableWifiHotspot(Context context){
        try{
            WifiManager wifi_manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiConfiguration wifi_configuration = null;


            Method wifiHotspotEnabledMethod = wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            wifiHotspotEnabledMethod.invoke(wifi_manager, wifi_configuration, true);
        }
        catch (NoSuchMethodException e)
        {

            e.printStackTrace();

        }
        catch (IllegalArgumentException e)
        {

            e.printStackTrace();

        }
        catch (IllegalAccessException e)
        {

            e.printStackTrace();

        }
        catch (InvocationTargetException e)
        {

            e.printStackTrace();

        }

    }

    public static void DisableWifiHotspot(Context context){
        try{
            WifiManager wifi_manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiConfiguration wifi_configuration = null;


            Method wifiHotspotEnabledMethod = wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            wifiHotspotEnabledMethod.invoke(wifi_manager, wifi_configuration, false);
        }
        catch (NoSuchMethodException e)
        {

            e.printStackTrace();

        }
        catch (IllegalArgumentException e)
        {

            e.printStackTrace();

        }
        catch (IllegalAccessException e)
        {

            e.printStackTrace();

        }
        catch (InvocationTargetException e)
        {

            e.printStackTrace();

        }

    }

    public static boolean setHotspotName(String newName, Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifiManager);


            wifiConfig.SSID = newName;

            Method setConfigMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setConfigMethod.invoke(wifiManager, wifiConfig);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
