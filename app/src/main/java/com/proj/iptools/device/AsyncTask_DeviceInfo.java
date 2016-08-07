package com.proj.iptools.device;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Display;
import com.proj.iptools.R;
import com.stericson.RootTools.RootTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Jin.
 * Date : 5/21/2015
 * Time : 5:58 PM
 * Version : 1.0
 */
public class AsyncTask_DeviceInfo extends AsyncTask<String, String, String> {

    private Context context;
    private WifiInfo wifiInfo;
    private Display display;
    private WindowManager windowManager;
    private ActivityManager activityManager;
    private ActivityManager.MemoryInfo memoryInfo;

    private View DeviceView;
    private ProgressDialog progress;
    private TextView device_network;
    private TextView device_technology;
    private TextView device_monitor;
    private TextView device_store;
    private TextView model_name;
    private ImageView model_image;


    private StringBuilder str_model_name;
    private StringBuilder str_device_network;
    private StringBuilder str_device_technology;
    private StringBuilder str_device_monitor;
    private StringBuilder str_device_store;



    public AsyncTask_DeviceInfo(View DeviceView, WifiManager wifiManager, WifiInfo wifiInfo, DhcpInfo dhcpInfo) {
        this.DeviceView = DeviceView;
        this.context = DeviceView.getContext();
        this.device_network = (TextView)DeviceView.findViewById(R.id.device_network);
        this.device_technology = (TextView)DeviceView.findViewById(R.id.device_technology);
        this.model_name = (TextView)DeviceView.findViewById(R.id.model_name);
        this.device_monitor = (TextView) DeviceView.findViewById(R.id.device_monitor);
        this.device_store = (TextView) DeviceView.findViewById(R.id.device_store);
        this.model_image = (ImageView) DeviceView.findViewById(R.id.model_image);
        this.wifiInfo = wifiInfo;
    }

    public String getModelName() {
        String cpuInfo = "Unknown";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("model name")) {
                    cpuInfo = line.substring(line.lastIndexOf(":") + 1).trim();
                    break;
                }
            }
        }catch (FileNotFoundException ex) {

        }catch (IOException ex){

        }

        return  cpuInfo;
    }

    /**
     * get information data
     * - memory
     * - internal store
     * - external store is plus
     */
    public void getDataStore() {
        str_device_store = new StringBuilder();

        //Memory information
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        long availableMemory = memoryInfo.availMem / 1048576L;
        long totalMemory = memoryInfo.totalMem / 1048576L;
        //Store information
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        long megAvailable = bytesAvailable / (1024 * 1024);
        Log.e("","Available MB : "+ megAvailable);

        str_device_store.append("Total RAM : " + totalMemory + " MB" + "\n");
        str_device_store.append("Used RAM : " + (totalMemory - availableMemory) + " MB" + "\n");
        str_device_store.append("Available RAM : " + availableMemory + " MB");
    }

    public void loadDeviceInfo() {

        getDataStore();

        str_model_name = new StringBuilder();
        str_device_network = new StringBuilder();
        str_device_technology = new StringBuilder();
        str_device_monitor = new StringBuilder();

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

        str_model_name.append(getModelName());

        if (Formatter.formatIpAddress(wifiInfo.getIpAddress()).equalsIgnoreCase("0.0.0.0"))
        {
            str_device_network.append("Internal IP : 127.0.0.1" + "\n");
        }
        else
        {
            str_device_network.append("Internal IP : " + Formatter.formatIpAddress(wifiInfo.getIpAddress()) + "\n");
        }
        str_device_network.append("MAC address : " + wifiInfo.getMacAddress() );

        str_device_technology.append("Model : " + Build.MODEL + " ( " + Build.PRODUCT + " )" + "\n");
        str_device_technology.append("Manufacturer : " + Build.MANUFACTURER + "\n");
        str_device_technology.append("Board : " + Build.BOARD + "\n");
        str_device_technology.append("Bootloader : " + Build.BOOTLOADER + "\n");
        str_device_technology.append("Build ID : " + Build.DISPLAY + "\n");
        str_device_technology.append("Hardware : " + Build.HARDWARE + "\n");
        str_device_technology.append("Android Version : " + Build.VERSION.RELEASE + "\n");
        str_device_technology.append("API Level : " + Build.VERSION.SDK_INT + "\n");
        str_device_technology.append("Java VM : " + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.specification.version") + "\n");
        str_device_technology.append("Kernel Version : " + System.getProperty("os.version") + "\n");
        if (RootTools.isRootAvailable())
        {
            str_device_technology.append("Root Access : Yes");
        }
        else
        {
            str_device_technology.append("Root Access : No");
        }
        str_device_monitor.append("Screen Resolution : " + display.getWidth() + " X " + display.getHeight());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(DeviceView.getContext());
        progress.setMessage("Loading data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

    }

    @Override
    protected String doInBackground(String... urls) {
        loadDeviceInfo();
        return null;
    }

    protected void onProgressUpdate(String... progress) {
    }

    @Override
    protected void onPostExecute(String unused) {
        device_network.setText(str_device_network);
        device_technology.setText(str_device_technology);
        model_name.setText(str_model_name);
        device_monitor.setText(str_device_monitor);
        device_store.setText(str_device_store);
        //Set image for cpu
        if (getModelName().contains("Intel"))
        {
            //model_image.setImageResource(R.drawable.information);
        }
        else if (getModelName().contains("Qualcomm"))
        {
            //model_image.setImageResource(R.drawable.information);
        }
        else if (getModelName().contains("Snapdragon"))
        {
            //model_image.setImageResource(R.drawable.information);
        }
        else
        {
            //model_image.setImageResource(R.drawable.information);
        }
        progress.dismiss();
    }
}
