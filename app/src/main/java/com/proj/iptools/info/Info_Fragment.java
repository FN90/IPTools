package com.proj.iptools.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.FragmentManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.proj.iptools.R;
import com.proj.iptools.cover.Cover_Info;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Info_Fragment extends Fragment{

    private AsyncTask_Info asyncTask_ipInfo;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private DhcpInfo dhcpInfo;
    public boolean turn_on_wifi = false;//default wifi disable

    private TextView access_Point_Information, access_Point_technology, access_Point_ip, geolocation_Information, connection_type;
    private View InfoView;
    private ImageView imageMap;

    public Bitmap bmp = null;
    public StringBuilder str_geolocation_Information;
    public StringBuilder str_access_Point_Information;
    public StringBuilder str_access_Point_Technology;
    public StringBuilder str_access_Point_ip;
    public StringBuilder str_connection_type;
    /**
     * Initialize object
     */
    public void initializeObject(View InfoView) {
        access_Point_Information = (TextView) InfoView.findViewById(R.id.access_Point_Information);
        access_Point_technology = (TextView) InfoView.findViewById(R.id.access_Point_technology);
        access_Point_ip = (TextView) InfoView.findViewById(R.id.access_Point_ip);
        geolocation_Information = (TextView) InfoView.findViewById(R.id.geolocation_Information);
        connection_type = (TextView) InfoView.findViewById(R.id.connection_type);
    }

    /**
     * Initialize wifi service
     */
    public void initializeWifiService(View InfoView) {
        wifiManager =  (WifiManager)InfoView.getContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        dhcpInfo = wifiManager.getDhcpInfo();
    }

    /**
     * Get the network info
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        InfoView = inflater.inflate(R.layout.info_layout, container, false);
        initializeObject(InfoView);
        initializeWifiService(InfoView);

         if (turn_on_wifi == false) {
            if (isConnected(getActivity())) {//access internet
                asyncTask_ipInfo = new AsyncTask_Info(getActivity(), InfoView, wifiManager, wifiInfo, dhcpInfo);
                asyncTask_ipInfo.execute();
            }
            else
            {
                Toast.makeText(InfoView.getContext(), "Internet not available", Toast.LENGTH_SHORT).show();
                Fragment cover_info = new Cover_Info();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, cover_info)
                        .commit();
            }
         }
         else
         {
             geolocation_Information.setText(str_geolocation_Information);
             access_Point_Information.setText(str_access_Point_Information);
             access_Point_technology.setText(str_access_Point_Technology);
             access_Point_ip.setText(str_access_Point_ip);
             connection_type.setText(str_connection_type);
        }
        return InfoView;
    }
}
