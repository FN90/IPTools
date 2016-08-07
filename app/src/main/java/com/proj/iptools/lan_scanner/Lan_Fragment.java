package com.proj.iptools.lan_scanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.FragmentManager;

import com.proj.iptools.R;
import com.proj.iptools.ping.Ping_Fragment;
import com.proj.iptools.port_scanner.Port_Scanner_Fragment;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Lan_Fragment extends Fragment{
    private ListView lan_show;
    private View Lan_Scanner_View;
    private View host_options;
    private RadioGroup options;
    private AsyncTask_Lan_Scanner asyncTask_lan_scanner;
    private ImageButton lan;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private DhcpInfo dhcpInfo;
    private CharSequence information_host;
    private Ping_Fragment ping_fragment;
    private Port_Scanner_Fragment port_scanner_fragment;
    private ListView mDrawerListView;

    /**
     * Initialize object
     */
    public void initializeObject(View Lan_Scanner_View) {
        lan = (ImageButton) Lan_Scanner_View.findViewById(R.id.lan);
        lan_show = (ListView) Lan_Scanner_View.findViewById(R.id.lan_show);
    }

    /**
     * Initialize wifi service
     */
    public void initializeWifiService(View Lan_Scanner_View) {
        wifiManager =  (WifiManager)Lan_Scanner_View.getContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        dhcpInfo = wifiManager.getDhcpInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Lan_Scanner_View = inflater.inflate(R.layout.lan_layout, container, false);
        initializeObject(Lan_Scanner_View);
        initializeWifiService(Lan_Scanner_View);

        asyncTask_lan_scanner = new AsyncTask_Lan_Scanner(Lan_Scanner_View, dhcpInfo, wifiInfo);
        asyncTask_lan_scanner.execute();

        lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncTask_lan_scanner = new AsyncTask_Lan_Scanner(Lan_Scanner_View, dhcpInfo, wifiInfo);
                asyncTask_lan_scanner.execute();
            }
        });

        //select another host to do somethings
        lan_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getActivity());
                host_options = li.inflate(R.layout.lan_options, null);
                options = (RadioGroup) host_options.findViewById(R.id.host_options);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                // set prompts.xml to alert dialog builder
                alertDialogBuilder.setView(host_options);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        information_host = ((TextView) view.findViewById(R.id.lan_scanner_result)).getText();
                                        String[] values = information_host.toString().split("\n");
                                        String host = values[1].substring(values[1].indexOf(":") + 1).trim();

                                        int choose = options.getCheckedRadioButtonId();
                                        if(choose == R.id.ping_host) {
                                            ping_fragment = new Ping_Fragment();
                                            ping_fragment.remote_call = true;
                                            ping_fragment.remote_target = host;

                                            Fragment  ping = ping_fragment;
                                            // update the main content by replacing fragments
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.container, ping)
                                                    .commit();
                                        } else {
                                            port_scanner_fragment = new Port_Scanner_Fragment();
                                            port_scanner_fragment.remote_call = true;
                                            port_scanner_fragment.remote_target = host;

                                            Fragment port_scanner = port_scanner_fragment;
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.container, port_scanner)
                                                    .commit();
                                        }

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

        return Lan_Scanner_View;
    }
}
