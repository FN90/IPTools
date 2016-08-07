package com.proj.iptools.port_scanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.proj.iptools.R;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Port_Scanner_Fragment extends Fragment {

    private ListView port_show;
    private View port_scanner_settings;
    private View portScannerView;
    private View port_options;
    private EditText target;
    private EditText port_select;
    private EditText ports_start;
    private EditText ports_end;
    private ImageButton settings;
    private ImageButton port;
    private Spinner spinner_settings;
    private Spinner spinner_delay_time;
    private AsyncTask_Port_Scanner asyncTask_port_scanner;
    private RadioGroup options;

    private String _scanner_settings = "";
    private String _target = "";
    private int _port_start = 0;
    private int _port_end = 0;
    private int _delay_time = 500;

    private String[] entries_delay_time = {"0.5 seconds", "1 seconds", "2 seconds", "3 seconds"};
    private String[] entries_settings = {"Ports Default", "Ports Range", "Ports Select"};
    private String _ports_select = "";
    private CharSequence information_port;
    private ArrayAdapter<String> dataAdapter;
    public boolean remote_call = false;
    public String remote_target = "";

    /**
     * Initialize object
     */
    public void initializeObject(View portScannerView) {
        target = (EditText) portScannerView.findViewById(R.id.target);
        port = (ImageButton) portScannerView.findViewById(R.id.port);
        settings = (ImageButton) portScannerView.findViewById(R.id.settings);
        port_show = (ListView) portScannerView.findViewById(R.id.port_show);
    }

    /**
     * Execute scanner open port
     */
    public void runPortScanner(View portScannerView) {
        if (target.getText().toString().equals("") || target.getText().toString().equals(null)) {
            Toast.makeText(getActivity().getApplicationContext(), "Input host name or IP address.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            _target = target.getText().toString();
        }

        asyncTask_port_scanner = new AsyncTask_Port_Scanner(portScannerView, _scanner_settings,
                _target, _port_start, _port_end, _ports_select, _delay_time);
        asyncTask_port_scanner.execute();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        portScannerView = inflater.inflate(R.layout.port_scanner_layout, container, false);
        initializeObject(portScannerView);

        if (remote_call) {
            target.setText("" + remote_target +"");
        }

        //select another port to do somethings
        port_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getActivity());
                port_options = li.inflate(R.layout.port_options, null);
                options = (RadioGroup) port_options.findViewById(R.id.options);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                // set prompts.xml to alert dialog builder
                alertDialogBuilder.setView(port_options);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        information_port = ((TextView) view.findViewById(R.id.port_scanner_result)).getText();
                                        String[] values = information_port.toString().split("\n");
                                        String host = "http://" + _target + ":" + values[0].substring(values[0].indexOf(":") + 1).trim();

                                        int choose = options.getCheckedRadioButtonId();
                                        if(choose == R.id.connect) {
                                            //Open web browser
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(host));
                                            startActivity(browserIntent);
                                        } else {
                                            //Brute force attack
                                            Toast.makeText(getActivity(), "Comming soon :)", Toast.LENGTH_SHORT).show();
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

        //Open options settings for ports scanner
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                //Initialize object
                port_scanner_settings = li.inflate(R.layout.port_scanner_setting, null);
                spinner_settings = (Spinner) port_scanner_settings.findViewById(R.id.spinner_settings);
                spinner_delay_time = (Spinner) port_scanner_settings.findViewById(R.id.spinner_delay_time);

                port_select = (EditText) port_scanner_settings.findViewById(R.id.port_select);
                ports_start = (EditText) port_scanner_settings.findViewById(R.id.ports_start);
                ports_end = (EditText) port_scanner_settings.findViewById(R.id.ports_end);

                ports_start.setEnabled(false);
                ports_start.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));
                ports_end.setEnabled(false);
                ports_end.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));
                port_select.setEnabled(false);
                port_select.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));

                dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, entries_settings);
                spinner_settings.setAdapter(dataAdapter);

                dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, entries_delay_time);
                spinner_delay_time.setAdapter(dataAdapter);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                // set prompts.xml to alert dialog builder
                alertDialogBuilder.setView(port_scanner_settings);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        _scanner_settings = spinner_settings.getSelectedItem().toString();
                                        switch (_scanner_settings) {
                                            case "Ports Range":
                                                _port_start = Integer.parseInt(ports_start.getText().toString());
                                                _port_end = Integer.parseInt(ports_end.getText().toString());
                                                break;
                                            case "Ports Select":
                                                _ports_select = port_select.getText().toString();
                                                break;
                                            case "Ports Default":
                                                _ports_select = "21,22,23,25,53,80,110,115,135,139,143,194,443,445,1433,3306,3389,5632,5900,6112";
                                                break;
                                        }
                                        String str = spinner_delay_time.getSelectedItem().toString();
                                        str = str.substring(0, str.indexOf(" ")).trim();
                                        float _f_delay_time = Float.parseFloat(str) * 1000;
                                        _delay_time = Math.round(_f_delay_time);
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

                //Event select kind of scan
                spinner_settings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String select = spinner_settings.getSelectedItem().toString();
                        switch (select) {
                            case "Ports Range":
                                ports_start.setEnabled(true);
                                ports_start.setPadding(0, 6, 0, 0);
                                ports_start.setBackgroundResource(R.drawable.border_edit_text);
                                ports_end.setEnabled(true);
                                ports_end.setPadding(0, 6, 0, 0);
                                ports_end.setBackgroundResource(R.drawable.border_edit_text);
                                port_select.setEnabled(false);
                                port_select.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));
                                port_select.setText("");
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                break;
                            case "Ports Select":
                                ports_start.setEnabled(false);
                                ports_start.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));
                                ports_start.setText("");
                                ports_end.setEnabled(false);
                                ports_end.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));
                                ports_end.setText("");
                                port_select.setEnabled(true);
                                ports_start.setPadding(0, 7, 0, 0);
                                port_select.setBackgroundResource(R.drawable.border_edit_text);
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                break;
                            case "Ports Default":
                                ports_start.setEnabled(false);
                                ports_start.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));
                                ports_start.setText("");
                                ports_end.setEnabled(false);
                                ports_end.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));
                                ports_end.setText("");
                                port_select.setEnabled(false);
                                port_select.setBackground(new ColorDrawable(Color.parseColor("#9E9E9E")));
                                port_select.setText("");
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //Event check password not empty
                ports_start.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (ports_start.getText().toString().equals("") || ports_start.getText().toString().equals(null) ||
                                ports_end.getText().toString().equals("") || ports_end.getText().toString().equals(null)) {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                //Event check password not empty
                ports_end.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (ports_start.getText().toString().equals("") || ports_start.getText().toString().equals(null) ||
                                ports_end.getText().toString().equals("") || ports_end.getText().toString().equals(null)) {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                //Event check password not empty
                port_select.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (port_select.getText().toString().equals("") || port_select.getText().toString().equals(null)) {
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
        });
        port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_scanner_settings == "") {
                    _scanner_settings = "Ports Default";
                    _ports_select = "21,22,23,25,53,80,110,115,135,139,143,194,443,445,1433,3306,3389,5632,5900,6112";
                }
                runPortScanner(portScannerView);
            }
        });
        return portScannerView;
    }
}
