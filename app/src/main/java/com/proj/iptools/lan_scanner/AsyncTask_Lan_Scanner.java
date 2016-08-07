package com.proj.iptools.lan_scanner;

import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.Formatter;

import com.proj.iptools.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;


/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Lan_Scanner extends AsyncTask<String, String, String> {

    private static final String PING_FAILED = "Unreachable";
    private ListView lan_show;
    private TextView total_host;
    private View Lan_Scanner_View;
    private ArrayList<String> lan_scanner_Result;
    private ArrayAdapter<String> arrayAdapter = null;
    private Lan_Scanner_Adapter lan_scanner_adapter;
    private final int HOST_LIMIT = 255;

    private String gateway = "";
    private float value;
    private int step = 1;
    private String discoverHost;
    private int host;
    private int last;//last number of gateway;

    public AsyncTask_Lan_Scanner(View Lan_Scanner_View, DhcpInfo dhcpInfo, WifiInfo wifiInfo) {
        this.Lan_Scanner_View = Lan_Scanner_View;
        this.gateway = Formatter.formatIpAddress(dhcpInfo.gateway);
        total_host = (TextView) Lan_Scanner_View.findViewById(R.id.total_host);
        lan_show = (ListView) Lan_Scanner_View.findViewById(R.id.lan_show);
        lan_scanner_Result = new ArrayList<>();
        arrayAdapter = new Lan_Scanner_Adapter(Lan_Scanner_View.getContext(), R.layout.lan_scanner_list_item, lan_scanner_Result, dhcpInfo, wifiInfo);
        lan_show.setAdapter(arrayAdapter);
    }

    private String ping(String url) {
        String str = "";
        try
        {
            String ping_query = "ping -c 1" + url;
            Process process = Runtime.getRuntime().exec( "/system/bin/" + ping_query);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while((line = reader.readLine()) != null)
            {
                str += line + "\n";
            }
            reader.close();
        }
        catch (IOException ex)
        {
        }

        return str;
    }

    /**
     * Scan port with option ports range
     */
    public void scanHostRange() {
        String str_host_c = gateway.substring(0, gateway.lastIndexOf(".") + 1);
        String str = "";
        int i = 0;//increase value of process bar

        for (host = last; host <= HOST_LIMIT; host++)
        {
            try
            {
                String _target = str_host_c + host;
                String res = ping(_target);
                if (res.toLowerCase().contains(PING_FAILED.toLowerCase()) == false) {
                    InetAddress inetAddress = InetAddress.getByName(_target);
                    String hostName = inetAddress.getHostName();
                    String ip_host = inetAddress.getHostAddress();
                    str = "Host : " + hostName + "\n" + "IP : " + ip_host + "~~";
                    if (!ip_host.trim().equalsIgnoreCase(hostName.trim()))
                    {
                        publishProgress(str);
                    }
                    else
                    {
                        if (ip_host.equalsIgnoreCase(this.gateway))
                        {
                            publishProgress(str);
                        }
                    }
                }
            }
            catch (Exception e)
            {
            }
            int value = i + step;
            str = String.valueOf(value) + "~~";
            //Update processBar
            publishProgress(str);
            i += step;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        last = Integer.parseInt(gateway.substring(gateway.lastIndexOf(".") + 1));
        scanHostRange();
        return null;
    }

    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        ProgressBar progressBarScanHost = (ProgressBar) Lan_Scanner_View.findViewById(R.id.progressBarScanHost);
        progressBarScanHost.setMax(255);
        String result = values[0];
        String[] arrayResult = result.split("~~");

        try
        {
            Integer isNumber = Integer.parseInt(arrayResult[0]);
            value = Integer.parseInt(arrayResult[0]);
            //tăng giá trị của Progressbar lên
            progressBarScanHost.setProgress(Math.round(value));
        }
        catch(NumberFormatException nfe)
        {
            discoverHost = arrayResult[0];
            lan_scanner_Result.add(discoverHost);
            total_host.setText("" + "Total Host : " + lan_scanner_Result.size() + "");
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPostExecute(String unused) {
        Toast.makeText(Lan_Scanner_View.getContext(), "Scan finished", Toast.LENGTH_SHORT).show();
    }
}
