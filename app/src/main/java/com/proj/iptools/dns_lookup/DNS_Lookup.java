package com.proj.iptools.dns_lookup;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class DNS_Lookup {
    // JSON Node names
    private static final String TAG_VALUE = "value";
    private static final String TAG_TTL = "ttl";
    private static final String TAG_TYPE = "type";
    private static final String TAG_NAME = "name";
    public ArrayList<String> recordsValue;
    public int recordsTTL;

    // contacts JSONArray
    JSONArray jsonArray = null;

    private String lookup_type;
    private String target;
    public StringBuffer result;

    public DNS_Lookup(String target, String lookup_type) {
        this.lookup_type = lookup_type;
        this.target = target;
    }

    /**
     * get dns lookup json data
     */
    public void getRecords(){
        recordsValue = new ArrayList<String>();
        try {
            String host = "http://dns-api.org/" + lookup_type + "/" + target;
            URL url = new URL(host);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            InputStream stream = conn.getInputStream();
            String jsonStr = convertStreamToString(stream);

            if (jsonStr != null) {
                try {
                    jsonArray = new JSONArray(jsonStr);
                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String value = c.getString(TAG_VALUE);
                        recordsTTL = c.getInt(TAG_TTL);
                        String type = c.getString(TAG_TYPE);
                        String name = c.getString(TAG_NAME);
                        recordsValue.add(value);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
