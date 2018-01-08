package com.example.zhudi.myapplication.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ww on 2018/1/8.
 */

public class ErTongHaoDanXuanMethod {
    private static List<String> list;
    private static ArrayList<String> arrayList;
    private static int i, j;
    public static List<String> MixNumber(String[] doubleList, String[] singleList) {
        list = new ArrayList<>();
        arrayList = new ArrayList<>();
        int doubleListLen = doubleList.length;
        int singleListLen = singleList.length;
        StringBuffer sb = new StringBuffer();

        for (i = 0; i < doubleListLen; i++) {
            for (j = 0; j < singleListLen; j++) {
                if (doubleList[i] == singleList[j]) {

                } else {
                    Log.i("zhu", doubleList[i]+","+singleList[j]);

                    arrayList.add(doubleList[i]);
                    arrayList.add(doubleList[i]);
                    arrayList.add(singleList[j]);
                    int y = arrayList.size();
                    sb.delete(0,sb.toString().length());
                    for (int x = 0; x < y; x++) {
                        if (x == 0) {
                            sb.append(arrayList.get(x));
                        } else if (x == y - 1) {
                            sb.append("," + arrayList.get(x));
                        } else {
                            sb.append("," + arrayList.get(x));
                        }
                    }
                    list.add(sb.toString());
                    arrayList.clear();
                }
            }
        }
        return list;
    }
}
