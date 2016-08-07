package com.proj.iptools.ping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.proj.iptools.R;

import java.util.ArrayList;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Ping_Adapter extends ArrayAdapter<String> {

    private Context context;
    ArrayList<String> strArray;


    public Ping_Adapter(Context context, int layoutId, ArrayList<String> strArray) {
        super(context, layoutId, strArray);
        this.context = context;
        this.strArray = strArray;
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.ping_list_item, parent, false);

        TextView ping_show = (TextView) rowView.findViewById(R.id.list_result);
        ping_show.setText(strArray.get(position));

        return rowView;
    }

}

