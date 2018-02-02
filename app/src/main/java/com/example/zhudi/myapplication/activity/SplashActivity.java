package com.example.zhudi.myapplication.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.zhudi.myapplication.R;

/**
 * Created by ww on 2018/1/3.
 */

public class SplashActivity extends BaseActivity {



//监听返回键并用无意义的判断让返回键无效

            private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {

//                Toast.makeText(getApplicationContext(), "应用正在加载中...", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                Toast.makeText(getApplicationContext(), "应用正在加载中...", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        SharedPreferences user = getSharedPreferences("name", Context.MODE_PRIVATE);
        String a = user.getString("inf", "no");

        if (a.equals("yes")) {

            //延迟2S跳转
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);

        }
    }
}