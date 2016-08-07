package com.proj.iptools.navDrawerCustom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import com.proj.iptools.R;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Navigation_Drawer_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Navigation_Drawer_Item> navigation_drawer_items;

    public Navigation_Drawer_Adapter(Context context, ArrayList<Navigation_Drawer_Item> navigation_drawer_items){
        this.context = context;
        this.navigation_drawer_items = navigation_drawer_items;
    }

    @Override
    public int getCount() {
        return navigation_drawer_items.size();
    }

    @Override
    public Object getItem(int position) {
        return navigation_drawer_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageResource(navigation_drawer_items.get(position).getIcon());
        txtTitle.setText(navigation_drawer_items.get(position).getTitle());

        return convertView;
    }

}