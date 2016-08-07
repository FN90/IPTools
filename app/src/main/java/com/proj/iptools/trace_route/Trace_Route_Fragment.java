package com.proj.iptools.trace_route;

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
public class Trace_Route_Fragment extends Fragment {

    private EditText target;
    private ImageButton trace;
    private View TraceView;
    private AsyncTask_Trace_Route asyncTask_trace_route;

    /**
     * Initialize object
     */
    public void initializeObject(View TraceView) {

        target = (EditText) TraceView.findViewById(R.id.target);
        trace = (ImageButton) TraceView.findViewById(R.id.trace);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TraceView = inflater.inflate(R.layout.trace_route_layout, container, false);

        initializeObject(TraceView);

        trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _target = "";
                if (target.getText().toString().equals("") || target.getText().toString().equals(null)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Input host name or IP address.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    _target = target.getText().toString();
                }
                asyncTask_trace_route = new AsyncTask_Trace_Route(TraceView, _target);
                asyncTask_trace_route.execute();
            }
        });
        return TraceView;
    }
}

