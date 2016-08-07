package com.proj.iptools.port_scanner;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.proj.iptools.R;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Port_Scanner extends AsyncTask<String, String, String> {

    private ListView port_show;
    private View portScannerView;
    private ArrayList<String> port_scanner_Result;
    private ArrayAdapter<String> arrayAdapter = null;

    private String _scanner_settings;
    private String _target = "";
    private int _port_start;
    private int _port_end;
    private int _delay_time;
    private String _ports_select_des;
    private String[] _port_select;

    private Port_Data portData;

    private float step;
    private String openPort;
    private float value;
    private int port;

    public AsyncTask_Port_Scanner(View portScannerView, String _scanner_settings, String _target, int _port_start,
                                  int _port_end, String _ports_select_des,  int _delay_time) {
        portData = new Port_Data();
        this.portScannerView = portScannerView;

        this._scanner_settings = _scanner_settings;
        this._target = _target;
        this._port_start = _port_start;
        this._port_end = _port_end;
        this._ports_select_des = _ports_select_des;
        this._delay_time = _delay_time;

        if (_ports_select_des != "") {
            _port_select = _ports_select_des.split(",");
            optimizePortSelect();
        }

        port_show = (ListView) portScannerView.findViewById(R.id.port_show);
        port_scanner_Result = new ArrayList<String>();
        arrayAdapter = new Port_Scanner_Adapter(portScannerView.getContext(), R.layout.port_scanner_list_item, port_scanner_Result);
        port_show.setAdapter(arrayAdapter);


    }

    // avoid space character
    public void optimizePortSelect() {
        for (int i = 0; i < _port_select.length; i++) {
            _port_select[i] = _port_select[i].trim();
        }
    }

    /**
     * Scan port with option ports range
     */
    public void scanPortRange() {
        boolean isConnect = false;
        String port_name = "";
        String str = "";
        float i = 0;//increase value of process bar

        for (port = _port_start; port <= _port_end; port++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(_target, port), _delay_time);
                isConnect = socket.isConnected();

                if (isConnect == true) {
                    port_name = portData.getPortName(port);
                    str = "Port : " + port + "\n" + "Name : " + port_name + "~~";
                    publishProgress(str);
                }
                socket.close();
            }
            catch (Exception e) {
            }
            int value = Math.round(i + step);
            str = String.valueOf(value) + "~~";
            //Update processBar
            publishProgress(str);
            i += step;
        }
    }

    /**
     * Scan port with options default and select ports
     */
    public void scanPortSelect_Default() {
        boolean isConnect = false;
        String port_name = "";
        String str = "";
        float i = 0;//increase value of process bar


        for (int j = 0; j <  _port_select.length; j++) {
            try {
                port = Integer.parseInt(_port_select[j]);
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(_target, port), _delay_time);
                isConnect = socket.isConnected();

                if (isConnect == true) {
                    port_name = portData.getPortName(port);
                    str = "Port : " + port + "\n" + "Name : " + port_name + "~~";
                    publishProgress(str);
                }
                socket.close();
            }
            catch (Exception e) {
            }
            int value = Math.round(i + step);
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
        try {
            switch (_scanner_settings) {
                case "Ports Range":
                    step = (float)100 / (_port_end - _port_start);
                    scanPortRange();
                    break;
                case "Ports Select":
                case "Ports Default":
                    step = (float)100 / _port_select.length;
                    scanPortSelect_Default();
                    break;
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        ProgressBar progressBarScanPort;
        String result = values[0];
        String[] arrayResult = result.split("~~");

        try
        {
            Integer isNumber = Integer.parseInt(arrayResult[0]);
            value = Integer.parseInt(arrayResult[0]);
            progressBarScanPort = (ProgressBar) portScannerView.findViewById(R.id.progressBarScanPort);
            //tăng giá trị của Progressbar lên
            progressBarScanPort.setProgress(Math.round(value));
        }
        catch(NumberFormatException nfe)
        {
            openPort = arrayResult[0];
            port_scanner_Result.add(openPort);
            Collections.reverse(port_scanner_Result);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPostExecute(String unused) {
        Toast.makeText(portScannerView.getContext(), "Scan finished", Toast.LENGTH_SHORT).show();
    }

}
