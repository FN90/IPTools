package com.proj.iptools.trace_route;

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
public class Trace_Route_Adapter extends ArrayAdapter<String> {

    private Context context;
    ArrayList<String> strArray;


    public Trace_Route_Adapter(Context context, int layoutId, ArrayList<String> strArray) {
        super(context, layoutId, strArray);
        this.context = context;
        this.strArray = strArray;
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tracer_list_item, parent, false);

        ImageView flag = (ImageView) rowView.findViewById(R.id.flag);
        TextView tracert = (TextView) rowView.findViewById(R.id.tracert);

        flag.setImageResource(R.drawable.flag);
        tracert.setText(strArray.get(position));


        return rowView;
    }


}

