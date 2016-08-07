package com.proj.iptools.wifi_scanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proj.iptools.R;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Wifi_Scanner_Fragment extends Fragment{

    private WifiManager wifiManager;
    private ListView wifi_show;
    private View wifi_Scanner_View;
    private ImageView access_point;
    private ImageButton fresh;
    private AsyncTask_Wifi_Scanner asyncTask_wifi_scanner;
    private AsyncTask_Start_Wifi_Service asyncTask_start_wifi_service;

    //Form connect to wifi
    private View connect_Wifi;
    private TextView wifi_name;
    private EditText password;
    private CheckBox show_password;
    private String _wifi_name = "";
    private String _secure = "";
    private String _password = "";
    private CharSequence informationESSID;

    public boolean turn_on_wifi_manager = false;

    /**
     * Initialize object
     */
    public void initializeObject(View wifi_Scanner_View) {
        fresh = (ImageButton) wifi_Scanner_View.findViewById(R.id.fresh);
        wifi_show = (ListView) wifi_Scanner_View.findViewById(R.id.wifi_show);
        access_point = (ImageView) wifi_Scanner_View.findViewById(R.id.access_point);
    }

    //Detect whether there is an wifi manager connection available
    private void isWifiManagerAvailable(View wifi_Scanner_View) {
        wifiManager =  (WifiManager)wifi_Scanner_View.getContext().getSystemService(Context.WIFI_SERVICE);
        turn_on_wifi_manager = wifiManager.isWifiEnabled();
    }

    /**
     * Turn on wifi service
     */
    public void turnOnWifiService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //create information form
        builder.setTitle("Information");
        builder.setMessage("Wifi service is disable. Do you want to enable ?");
        //get image
        builder.setIcon(R.drawable.information);
        //call Map_fragment
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                asyncTask_start_wifi_service = new AsyncTask_Start_Wifi_Service(wifi_Scanner_View);
                asyncTask_start_wifi_service.execute();
            }
        });
        //nothings
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Show dialog
        builder.create().show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wifi_Scanner_View = inflater.inflate(R.layout.wifi_scanner_layout, container, false);
        initializeObject(wifi_Scanner_View);
        isWifiManagerAvailable(wifi_Scanner_View);

        if (turn_on_wifi_manager == false)
        {
            turnOnWifiService();
        } else {
            asyncTask_wifi_scanner = new AsyncTask_Wifi_Scanner(wifi_Scanner_View, wifiManager);
            asyncTask_wifi_scanner.execute();
        }

        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turn_on_wifi_manager == false)
                {
                    turnOnWifiService();
                } else {
                    asyncTask_wifi_scanner = new AsyncTask_Wifi_Scanner(wifi_Scanner_View, wifiManager);
                    asyncTask_wifi_scanner.execute();
                }
            }
        });

        wifi_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getActivity());
                //assign value
                informationESSID = ((TextView) view.findViewById(R.id.access_point_name)).getText();
                String[] values = informationESSID.toString().split("\n");
                _wifi_name = values[0].substring(values[0].indexOf(":") + 1).trim();
                _secure = values[2].substring(values[2].indexOf(":") + 1).trim();


                if (_secure.equalsIgnoreCase("Open")) {
                    connect_Wifi = li.inflate(R.layout.connect_wifi_open, null);
                } else {
                    connect_Wifi = li.inflate(R.layout.connect_wifi, null);
                    password = (EditText) connect_Wifi.findViewById(R.id.password);
                    show_password = (CheckBox) connect_Wifi.findViewById(R.id.show_password);
                }

                wifi_name = (TextView) connect_Wifi.findViewById(R.id.wifi_name);
                wifi_name.setText("" + _wifi_name + "");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                // set prompts.xml to alert dialog builder
                alertDialogBuilder.setView(connect_Wifi);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Connect",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (_secure.equalsIgnoreCase("Open")) {
                                            _password = "";
                                        } else {
                                            _password = password.getText().toString();
                                        }
                                        AsyncTask_Connect_Wifi asyncTask_connect_wifi = new AsyncTask_Connect_Wifi(connect_Wifi, _wifi_name, _secure, _password);
                                        asyncTask_connect_wifi.Connect();
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

                if (!_secure.equalsIgnoreCase("Open")) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                    //Convert between plaintext  vs ciphertext
                    show_password.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //is chkIos checked?
                            if (((CheckBox) v).isChecked()) {
                                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            } else {
                                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            }
                        }
                    });

                    //Event check password not empty
                    password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (password.getText().toString().equals("") || password.getText().toString().equals(null)) {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            } else {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
            }
        });
        return wifi_Scanner_View;
    }
}
