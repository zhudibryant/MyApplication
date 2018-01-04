package com.example.zhudi.myapplication.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ww on 2017/9/15.
 */

public class Utils {
    public static void alertShort(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
