package com.proj.iptools.wifi_scanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;

import java.util.List;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Connect_Wifi extends AsyncTask<String, String, String> {

    private Activity activity;
    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetworkInfo;
    private WifiManager wifiManager;
    private WifiConfiguration conf;
    private View view;
    private ProgressDialog progress;

    private String secure;
    private String networkSSID = "";
    private String networkPass = "";

    public AsyncTask_Connect_Wifi(View view, String networkSSID, String secure, String networkPass) {
        this.view = view;
        this.networkSSID = networkSSID;
        this.secure = secure;
        this.networkPass = networkPass;
        activity = (Activity)view.getContext();
        wifiManager =  (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
        conf = new WifiConfiguration();
        conf.SSID = "\"" + this.networkSSID + "\"";
    }

    //Detect whether there is an Internet connection available
    private boolean isNetworkAvailable() {
        connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void Connect() {
        if (this.secure.equalsIgnoreCase("WEP"))
        {
            conf.wepKeys[0] = "\"" + this.networkPass + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        }
        else if (this.secure.equalsIgnoreCase("WPA") || this.secure.equalsIgnoreCase("WPA2"))
        {
            conf.preSharedKey = "\""+ this.networkPass +"\"";
        }
        else
        { //secure : Open
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        //add it to Android wifi manager
        wifiManager.addNetwork(conf);

        //enable it, so Android connects to it
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + this.networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(this.view.getContext());
        progress.setMessage("Enable wifi service...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            Connect();
            boolean wait = true;
            int limitTime = 15; //10s
            int i = 0;
            while (wait) {
                SystemClock.sleep(500);//0.5s
                if (isNetworkAvailable()){
                    wait = false;
                }
                i++;
                if (i == limitTime) {
                    wait = false;
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String unused) {
        progress.dismiss();
    }
}
