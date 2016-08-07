package com.proj.iptools.wifi_scanner;

import android.annotation.SuppressLint;
import android.content.Context;
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
public class Wifi_Scanner_Adapter extends ArrayAdapter<String> {

    private Context context;
    ArrayList<String> strArray;


    public Wifi_Scanner_Adapter(Context context, int layoutId, ArrayList<String> strArray) {
        super(context, layoutId, strArray);
        this.context = context;
        this.strArray = strArray;
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.wifi_scanner_list_item, parent, false);

        ImageView access_point = (ImageView) rowView.findViewById(R.id.access_point);
        TextView access_point_name = (TextView) rowView.findViewById(R.id.access_point_name);

        access_point.setImageResource(R.drawable.access_point);
        access_point_name.setText(strArray.get(position));


        return rowView;
    }

}