package com.proj.iptools.info;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.view.Display;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.proj.iptools.R;
import com.proj.iptools.cover.Cover_Info;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Info extends AsyncTask<String, String, String> {

    private Activity activity;
    private View InfoView;
    private ProgressDialog progress;
    private Info ipInfo;
    private WindowManager windowManager;
    private Display display;

    private StringBuilder str_geolocation_Information;
    private StringBuilder str_access_Point_Information;
    private StringBuilder str_access_Point_Technology;
    private StringBuilder str_access_Point_ip;
    private StringBuilder str_connection_type;

    public AsyncTask_Info(Context context, View InfoView, WifiManager wifiManager, WifiInfo wifiInfo, DhcpInfo dhcpInfo) {
        this.InfoView = InfoView;
        this.ipInfo = new Info(context, wifiManager, wifiInfo, dhcpInfo);
        activity = (Activity)InfoView.getContext();
    }

    /**
     * Get total information from access point include :
     * general IP Information
     * geolocation Information
     * wiFi Information
     */
    public void loadWifiInfo() {
        str_geolocation_Information = new StringBuilder();
        str_access_Point_Information = new StringBuilder();
        str_access_Point_Technology = new StringBuilder();
        str_access_Point_ip = new StringBuilder();
        str_connection_type = new StringBuilder();

        ipInfo.setGeolocationData();
        ipInfo.setWifiData();

        while (ipInfo.parsingComplete) {
            ipInfo.getExternal_ip();
            ipInfo.getIsp();
            ipInfo.getOrganization();
            ipInfo.getCountry();
            ipInfo.getState_region();
            ipInfo.getCity();
            ipInfo.getLatitude();
            ipInfo.getLongitude();
        }

        str_access_Point_Information.append("Name : " + ipInfo.getWireless_network_name() + "\n");
        str_access_Point_Information.append("ISP : " + ipInfo.getIsp() + "\n");
        str_access_Point_Information.append("Organization : " + ipInfo.getOrganization());

        str_access_Point_Technology.append("External IP : " + ipInfo.getExternal_ip() + "\n");
        str_access_Point_Technology.append("MAC Address : " + ipInfo.getMac_address_access_point() + "\n");
        str_access_Point_Technology.append("Signal Strength : " + ipInfo.getSignal_strength() + "\n");
        str_access_Point_Technology.append("Frequency  : " + ipInfo.getFrequency() + " MHz" + "\n");
        str_access_Point_Technology.append("Speed : " + ipInfo.getSpeed() + " Mbps" + "\n");
        str_access_Point_Technology.append("Channel : " + ipInfo.getChannel() + "\n");
        str_access_Point_Technology.append("Secure : " + ipInfo.getSecurity());

        str_access_Point_ip.append("Subnet Mask : " + ipInfo.getSubnet_mask() + "\n");
        str_access_Point_ip.append("Default Gateway : " + ipInfo.getDefault_gateway() + "\n");
        str_access_Point_ip.append("DNS(1) Address : " + ipInfo.getDns_1() + "\n");
        str_access_Point_ip.append("DNS(2) Address : " + ipInfo.getDns_2() + "\n");
        str_access_Point_ip.append("Server Address : " + ipInfo.getServer_address());

        str_connection_type.append("Connection Type : " + ipInfo.getConnection_type());

        str_geolocation_Information.append("Country : " + ipInfo.getCountry() + "\n");
        str_geolocation_Information.append("State/Region : " + ipInfo.getState_region() + "\n");
        str_geolocation_Information.append("City : " + ipInfo.getCity() + "\n");
        str_geolocation_Information.append("Provider Location " + "\n");

        str_geolocation_Information.append("   + Latitude : " + ipInfo.getLatitude() + "\n");
        str_geolocation_Information.append("   + Longitude : " + ipInfo.getLongitude());

        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Create new cover
        Fragment cover = new Cover_Info();
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, cover)
                .commit();

        progress = new ProgressDialog(InfoView.getContext());
        progress.setMessage("Loading data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            loadWifiInfo();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... progress) {
    }

    @Override
    protected void onPostExecute(String unused) {

        //Create new cover
        Info_Fragment info_fragment = new Info_Fragment();
        info_fragment.turn_on_wifi = true;
        info_fragment.str_geolocation_Information = str_geolocation_Information;
        info_fragment.str_access_Point_Information = str_access_Point_Information;
        info_fragment.str_access_Point_Technology = str_access_Point_Technology;
        info_fragment.str_access_Point_ip = str_access_Point_ip;
        info_fragment.str_connection_type = str_connection_type;

        Fragment action = info_fragment;
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, action)
                .commit();

        progress.dismiss();
    }
}
