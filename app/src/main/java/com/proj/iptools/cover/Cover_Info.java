package com.proj.iptools.cover;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.proj.iptools.R;
import com.proj.iptools.info.Info_Fragment;

/**
 * Created by Jin.
 * Date : 5/19/2015
 * Time : 5:55 PM
 * Version : 1.0
 */

public class Cover_Info extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View CoverView = inflater.inflate(R.layout.cover_layout, container, false);

        CoverView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP: {
                        Fragment InfoView = new Info_Fragment();
                        // update the main content by replacing fragments
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, InfoView)
                                .commit();
                        break;
                    }
                }
                return true;
            }
        });
        return CoverView;
    }
}
