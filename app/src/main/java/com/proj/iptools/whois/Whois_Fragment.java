package com.proj.iptools.whois;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.proj.iptools.R;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Whois_Fragment extends Fragment {

    private EditText target;
    private ImageButton whois;
    private View WhoisView;
    private AsyncTask_Whois asyncTask_whois;

    /**
     * Initialize object
     */
    public void initializeObject(View whoisView) {

        target = (EditText) whoisView.findViewById(R.id.target);
        whois = (ImageButton) whoisView.findViewById(R.id.whois);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WhoisView = inflater.inflate(R.layout.whois_layout, container, false);

        initializeObject(WhoisView);

        whois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _target = "";
                if (target.getText().toString().equals("") || target.getText().toString().equals(null)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Input host name or IP address.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    _target = target.getText().toString();
                }
                asyncTask_whois = new AsyncTask_Whois(WhoisView, _target);
                asyncTask_whois.execute();
            }
        });
        return WhoisView;
    }
}
