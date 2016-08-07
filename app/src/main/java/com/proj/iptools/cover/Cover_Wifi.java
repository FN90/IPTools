package com.proj.iptools.cover;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proj.iptools.R;

/**
 * Created by Jin.
 * Date : 5/21/2015
 * Time : 5:58 PM
 * Version : 1.0
 */
public class Cover_Wifi extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View CoverView = inflater.inflate(R.layout.cover_layout, container, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //create information form
        builder.setTitle("Information");
        builder.setMessage("Wifi services is disable. Do you want to enable them ?");
        //get image
        builder.setIcon(R.drawable.information);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Enable wifi service
                // use for wifi scanner
            }
        });
        //nothings
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Show dialog
        builder.create().show();

        return CoverView;
    }
}
