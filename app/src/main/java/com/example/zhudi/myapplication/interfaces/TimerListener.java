package com.example.zhudi.myapplication.interfaces;

/**
 * Created by ww on 2018/1/31.
 */

public interface TimerListener {
    void myTimeTick(Long remaningTime);
    void myTimeFinish();
}
