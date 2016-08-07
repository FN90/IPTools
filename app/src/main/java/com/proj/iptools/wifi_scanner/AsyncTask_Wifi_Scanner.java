package com.proj.iptools.wifi_scanner;

import android.app.ProgressDialog;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.proj.iptools.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Wifi_Scanner extends AsyncTask<String, String, String> {

    private WifiManager wifiManager;
    private Wifi_Data wifi_data;

    private View wifi_Scanner_View;
    private List<ScanResult> scanResults;
    private TextView total_essid;
    public  ArrayList<String> wifi_scanner_Result;
    private ArrayAdapter<String> arrayAdapter = null;
    private ListView wifi_show;

    private ProgressDialog progress;
    Map<String, String> whilteList = new HashMap<String, String>();

    public AsyncTask_Wifi_Scanner(View wifi_Scanner_View, WifiManager wifiManager) {
        wifi_data = new Wifi_Data();
        this.wifi_Scanner_View = wifi_Scanner_View;
        this.wifiManager = wifiManager;
        total_essid = (TextView)wifi_Scanner_View.findViewById(R.id.total_essid);
        wifi_show = (ListView)this.wifi_Scanner_View.findViewById(R.id.wifi_show);
        wifi_scanner_Result = new ArrayList<>();
        arrayAdapter = new Wifi_Scanner_Adapter(this.wifi_Scanner_View.getContext(),
                R.layout.port_scanner_list_item, wifi_scanner_Result);
        wifi_show.setAdapter(arrayAdapter);
    }

    /**
     * check signal level wifi
     * @param signal
     * @return
     */
    public String getSignalLevelString(int signal) {
        String str_signal = "";
        if (signal > 0 && signal < 1)
        {
            str_signal = "Poor";
        }
        else if (signal >= 1 && signal < 2)
        {
            str_signal = "Fair";
        }
        else if (signal >= 2 && signal < 3)
        {
            str_signal = " Average";
        }
        else if (signal >= 3 && signal < 4)
        {
            str_signal = "Good";
        }
        else
        {
            str_signal = "Excellent";
        }
        return  str_signal;
    }

    public  void scanWifi() {
        String str_result;
        int numberOfLevels = 5;//use for signal strength
        scanResults = wifiManager.getScanResults();
        boolean flag = true;

        for (int i = 0; i < scanResults.size(); i ++) {
            str_result = "ESSID  : " + scanResults.get(i).SSID + "\n" +
                    "BSSID  : " + scanResults.get(i).BSSID + "\n" +
                    "Secure : " + wifi_data.getEncryption(scanResults.get(i).capabilities) + "\n" +
                    "Signal  : " + getSignalLevelString(WifiManager.calculateSignalLevel(scanResults.get(i).level, numberOfLevels)) + "\n" +
                    "FR        : " + scanResults.get(i).frequency + " MHz" + "\n" +
                    "CH        : " + wifi_data.getValueChannel(scanResults.get(i).frequency);

            if (whilteList.size() != 0)
            {
                for (Map.Entry<String, String> entry : whilteList.entrySet()) {
                    //If ESSID exist
                    if (scanResults.get(i).SSID.equals(entry.getKey()) || scanResults.get(i).BSSID.equals(entry.getValue())) {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag)
            {
                publishProgress(str_result);
                whilteList.put(scanResults.get(i).SSID, scanResults.get(i).BSSID);
            }
            flag = true;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(wifi_Scanner_View.getContext());
        progress.setMessage("Loading data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        try
        {
            scanWifi();
        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        wifi_scanner_Result.add(values[0]);
        total_essid.setText("" + "Total Access Point : " + wifi_scanner_Result.size() + "");
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(String unused) {
        progress.dismiss();
    }
}
