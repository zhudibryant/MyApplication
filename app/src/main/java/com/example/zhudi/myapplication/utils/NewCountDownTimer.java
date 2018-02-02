package com.example.zhudi.myapplication.utils;

import com.example.zhudi.myapplication.interfaces.TimerListener;

import java.sql.Time;

/**
 * Created by ww on 2018/1/31.
 */

public class NewCountDownTimer {
    private long countDownTime, delayMillis;
    private TimerListener listener;
    public NewCountDownTimer(long cdTime, long dMillis, TimerListener listener) {
        this.countDownTime = cdTime;
        this.delayMillis = dMillis;
        this.listener = listener;

    }


    public void start() {
        handler.postDelayed(runnable,0);

    }

    public void exit(){
        handler.removeCallbacks(runnable);
    }

    android.os.Handler handler = new android.os.Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countDownTime -= 1000;
            if (listener!=null){
                listener.myTimeTick(countDownTime);
            }
            if (countDownTime<=0){
                if(listener!=null){
                    listener.myTimeFinish();
                }
                return;
            }

            handler.postDelayed(this, delayMillis);
        }
    };
}
