package com.proj.iptools.wifi_scanner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;

import com.proj.iptools.R;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Start_Wifi_Service extends AsyncTask<String, String, String> {

    private Activity activity;
    private View view;
    private WifiManager wifiManager;
    private ProgressDialog progress;


    public AsyncTask_Start_Wifi_Service(View view) {
        this.view = view;
        activity = (Activity)this.view.getContext();
        this.wifiManager =  (WifiManager)this.view.getContext().getSystemService(Context.WIFI_SERVICE);
    }

    public boolean isWifiManagerAvailable(View CoverView) {
        wifiManager =  (WifiManager)CoverView.getContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    public void enableWifiManager() {
        boolean wait = true;
        wifiManager.setWifiEnabled(true);
        int limitTime = 10; //10s
        int i = 0;
        while (wait) {
            SystemClock.sleep(500);//0.5s
            if (isWifiManagerAvailable(this.view)) {
                wait = false;
            }
            i++;
            if (i == limitTime) {
                wait = false;
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
            enableWifiManager();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... progress) {
    }

    @Override
    protected void onPostExecute(String unused) {
        Wifi_Scanner_Fragment wifi_scanner_fragment = new Wifi_Scanner_Fragment();
        wifi_scanner_fragment.turn_on_wifi_manager = true;

        Fragment action = wifi_scanner_fragment;
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, action)
                .commit();


        progress.dismiss();
    }
}
