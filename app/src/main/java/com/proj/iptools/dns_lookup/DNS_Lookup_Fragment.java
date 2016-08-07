package com.proj.iptools.dns_lookup;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.proj.iptools.R;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class DNS_Lookup_Fragment extends Fragment{

    private EditText target;
    private ImageButton dns;
    private View DNS_Lookup_View;
    private Spinner dns_lookup_type;
    private AsyncTask_DNS_lookup asyncTask_dns_lookup;
    private ArrayAdapter<String> dataAdapter;
    private String[] entries_lookup_type = {"A", "AAAA", "NS", "MX", "TXT"};

    /**
     * Initialize object
     */
    public void initializeObject(View DNS_Lookup_View) {
        target = (EditText) DNS_Lookup_View.findViewById(R.id.target);
        dns = (ImageButton) DNS_Lookup_View.findViewById(R.id.dns);
        dns_lookup_type = (Spinner) DNS_Lookup_View.findViewById(R.id.lookup_type);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DNS_Lookup_View = inflater.inflate(R.layout.dns_lookup_layout, container, false);
        initializeObject(DNS_Lookup_View);

        dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_dns, entries_lookup_type);
        dns_lookup_type.setAdapter(dataAdapter);

        dns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _target = "";
                if (target.getText().toString().equals("") || target.getText().toString().equals(null)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Input host name or IP address.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    _target = target.getText().toString();
                }
                String lookup_type = dns_lookup_type.getSelectedItem().toString();
                asyncTask_dns_lookup = new AsyncTask_DNS_lookup(DNS_Lookup_View, _target, lookup_type);
                asyncTask_dns_lookup.execute();
            }
        });
        return DNS_Lookup_View;
    }
}
