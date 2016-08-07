package com.proj.iptools.ping;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.proj.iptools.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Ping extends AsyncTask<String, String, String> {

    private ListView ping_show;
    private View ipPingView;
    private ArrayList<String> ping_Result;
    private ArrayAdapter<String> arrayAdapter = null;
    private String ping_query;
    private String target;

    public AsyncTask_Ping(View ipPingView, String target, String ping_query) {
        this.ipPingView = ipPingView;
        this.ping_query = ping_query;
        this.target = target;

        ping_show = (ListView) ipPingView.findViewById(R.id.ping_show);
        ping_Result = new ArrayList<String>();
        arrayAdapter = new Ping_Adapter(ipPingView.getContext(), R.layout.ping_list_item, ping_Result);
        ping_show.setAdapter(arrayAdapter);
    }

    /**
     * Execute ping target
     */
    public void ping() {
        String str= "";
        try {

            Process process = Runtime.getRuntime().exec( "/system/bin/" + ping_query);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while((line = reader.readLine()) != null) {
                publishProgress(line);
            }
            reader.close();
        } catch (IOException ex) {
        }
    }

    @Override
        protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            ping();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //lấy giá trị truyền từ publishProgress
        ping_Result.add(values[0]);
        //cập nhật lại giao diện
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(String unused) {
    }
}
