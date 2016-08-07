package com.proj.iptools.device;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.proj.iptools.R;


/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:58 PM
 * Version : 1.0
 */
public class Device_Fragment extends Fragment {

    private AsyncTask_DeviceInfo asyncTask_deviceInfo;
    private View DeviceView;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private DhcpInfo dhcpInfo;
    /**
     * Initialize wifi service
     */
    public void initializeWifiService(View DeviceView) {
        wifiManager =  (WifiManager)DeviceView.getContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        dhcpInfo = wifiManager.getDhcpInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DeviceView = inflater.inflate(R.layout.os_layout, container, false);
        initializeWifiService(DeviceView);

        asyncTask_deviceInfo = new AsyncTask_DeviceInfo(DeviceView, wifiManager, wifiInfo, dhcpInfo);
        asyncTask_deviceInfo.execute();
        return DeviceView;
    }
}
