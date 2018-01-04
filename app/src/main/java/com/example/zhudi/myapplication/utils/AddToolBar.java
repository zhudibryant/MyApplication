package com.example.zhudi.myapplication.utils;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.zhudi.myapplication.R;

/**
 * Created by ww on 2017/9/16.
 */

public class AddToolBar {
    public static void addToolBar(final Activity activity, String s,String rgb){
        Toolbar toolBar = (Toolbar) activity.findViewById(R.id.toolBar);
        toolBar.setBackgroundColor(Color.parseColor(rgb));
        toolBar.setTitle(s);

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }
}
