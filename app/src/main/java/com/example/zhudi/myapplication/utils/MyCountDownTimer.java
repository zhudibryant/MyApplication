package com.example.zhudi.myapplication.utils;

import android.os.CountDownTimer;

/**
 * Created by ww on 2018/1/1.
 */

public class MyCountDownTimer extends CountDownTimer {
    public long min;
    public long second;
    public long countDown ;

    public MyCountDownTimer(long sec, long cd) {
        super(sec,cd);
        this.second = sec;
    }

    public MyCountDownTimer(long m, long s, long cd){
        super(s,cd);
        this.min = m;
        this.second = s;
        this.countDown = cd;
    }

    @Override
    public void onTick(long l) {

    }

    @Override
    public void onFinish() {

    }
}
