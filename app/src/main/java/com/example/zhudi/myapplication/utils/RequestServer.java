package com.example.zhudi.myapplication.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ww on 2017/12/14.
 */

public class RequestServer {
    private static String TAG = "url";

    public static String RequestServer(String urlString) {
        BufferedReader br = null;
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL reqURL = new URL(urlString);
            conn = (HttpURLConnection) reqURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            //打开网络通讯输入流
            int code = conn.getResponseCode();
            if (code != 200){
                return null;
            }
            //Log.e("chen", "code:" + code);
            InputStream is = conn.getInputStream();
            //通过InputStream is 创建InputStreamReader对象
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            //通过InputStreamReader isr 创建BufferReader对象
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}

