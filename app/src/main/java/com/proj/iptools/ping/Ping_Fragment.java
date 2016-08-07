package com.proj.iptools.ping;

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
public class Ping_Fragment extends Fragment{

    private View PingView;
    private EditText target;
    private EditText count, packet_size, timeout;
    private ImageButton ping;
    private String _target = "";
    private int _count = 0;
    private int _packet_size = 0;
    private int _timeout = 0;
    private AsyncTask_Ping asyncTask_ping;
    public boolean remote_call = false;
    public String remote_target = "";



    /**
     * Initialize object
     */
    public void initializeObject(View ipPingView) {
        target = (EditText) ipPingView.findViewById(R.id.target);
        count = (EditText) ipPingView.findViewById(R.id.count);
        packet_size = (EditText) ipPingView.findViewById(R.id.packet_size);
        timeout = (EditText) ipPingView.findViewById(R.id.time_out);
        ping = (ImageButton) ipPingView.findViewById(R.id.ping);

    }

    public void Ping() {
        if (target.getText().toString().equals("") || target.getText().toString().equals(null)) {
            Toast.makeText(getActivity().getApplicationContext(), "Input host name or IP address.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            _target = target.getText().toString();
        }

        if (count.getText().toString().equals("") || count.getText().toString().equals(null)) {

            Toast.makeText(getActivity().getApplicationContext(), "Input number of ping.", Toast.LENGTH_SHORT).show();
            return;

        } else {
            _count = Integer.parseInt(count.getText().toString());
        }

        if (packet_size.getText().toString().equals("") || packet_size.getText().toString().equals(null)) {

            Toast.makeText(getActivity().getApplicationContext(), "Input packet size.", Toast.LENGTH_SHORT).show();
            return;

        } else {
            _packet_size = Integer.parseInt(packet_size.getText().toString());
        }

        if (timeout.getText().toString().equals("") || timeout.getText().toString().equals(null)) {

            Toast.makeText(getActivity().getApplicationContext(), "Input timeout.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            _timeout = Integer.parseInt(timeout.getText().toString());
        }
        String ping_query = "ping -c " + _count + " -s " + _packet_size + " -W " + _timeout + " " + _target;
        asyncTask_ping = new AsyncTask_Ping(PingView, _target, ping_query);
        asyncTask_ping.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        PingView = inflater.inflate(R.layout.ping_layout, container, false);
        initializeObject(PingView);

        if (remote_call) {
            target.setText("" + remote_target +"");
        }

        ping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (target.getText().toString().equals("") || target.getText().toString().equals(null)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Input host name or IP address.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    _target = target.getText().toString();
                }

                if (count.getText().toString().equals("") || count.getText().toString().equals(null)) {

                    Toast.makeText(getActivity().getApplicationContext(), "Input number of ping.", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    _count = Integer.parseInt(count.getText().toString());
                }

                if (packet_size.getText().toString().equals("") || packet_size.getText().toString().equals(null)) {

                    Toast.makeText(getActivity().getApplicationContext(), "Input packet size.", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    _packet_size = Integer.parseInt(packet_size.getText().toString());
                }

                if (timeout.getText().toString().equals("") || timeout.getText().toString().equals(null)) {

                    Toast.makeText(getActivity().getApplicationContext(), "Input timeout.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    _timeout = Integer.parseInt(timeout.getText().toString());
                }
                String ping_query = "ping -c " + _count + " -s " + _packet_size + " -W " + _timeout + " " + _target;
                asyncTask_ping = new AsyncTask_Ping(PingView, _target, ping_query);
                asyncTask_ping.execute();
            }
        });

        return PingView;
    }
}
