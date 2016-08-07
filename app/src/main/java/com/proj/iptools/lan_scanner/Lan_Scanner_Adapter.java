package com.proj.iptools.lan_scanner;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.proj.iptools.R;

import java.util.ArrayList;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Lan_Scanner_Adapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> strArray;
    public String gate_way = "";
    public String ip_device = "";


    public Lan_Scanner_Adapter (Context context, int layoutId, ArrayList<String> strArray, DhcpInfo dhcpInfo, WifiInfo wifiInfo) {
        super(context, layoutId, strArray);
        this.context = context;
        this.strArray = strArray;
        gate_way = Formatter.formatIpAddress(dhcpInfo.gateway);
        ip_device = Formatter.formatIpAddress(wifiInfo.getIpAddress());
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lan_scanner_list_item, parent, false);

        ImageView host = (ImageView) rowView.findViewById(R.id.host);
        TextView lan_scanner_result = (TextView) rowView.findViewById(R.id.lan_scanner_result);

        String[] str_host = strArray.get(position).toString().split("\n");
        String[] str = strArray.get(position).toString().split("\n");

        String str_host_name = str_host[0].substring(str_host[0].lastIndexOf(":") + 2).trim();
        String ip = str[1].substring(str[1].lastIndexOf(":") + 2).trim();

        if (ip.equalsIgnoreCase(gate_way)) {
            host.setImageResource(R.drawable.router);
        }
        else if (ip.equalsIgnoreCase(ip_device) || str_host_name.contains("android"))
        {
            host.setImageResource(R.drawable.android);
        }
        else if (str_host_name.contains("Windows-Phone") || str_host_name.contains("Windows"))
        {
            host.setImageResource(R.drawable.windows_phone);
        }
        else if (str_host_name.contains("Iphone") || str_host_name.contains("iphone"))
        {
            host.setImageResource(R.drawable.iphone);
        }
        else
        {
            host.setImageResource(R.drawable.computer);
        }
        lan_scanner_result.setText(strArray.get(position));
        return rowView;
    }
}

