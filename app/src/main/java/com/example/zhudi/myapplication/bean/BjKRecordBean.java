package com.example.zhudi.myapplication.bean;

import java.io.Serializable;

/**
 * Created by ww on 2018/1/25.
 */

public class BjKRecordBean implements Serializable{
    private long id;
    private String num;
    private String opencode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOpencode() {
        return opencode;
    }

    public void setOpencode(String opencode) {
        this.opencode = opencode;
    }
}
