package com.example.zhudi.myapplication.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ww on 2018/1/25.
 */

public class BjKRecordBean implements Serializable{

    private long id;
    private int recordType;
    private String num;
    private String opencode;
    private Date endtime,servertime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRecordType(){return recordType;}

    public void setRecordType(int recordType){this.recordType = recordType;}

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

    public Date getEndtime(){return endtime;}

    public void setEndtime(Date endtime){this.endtime = endtime;}

    public Date getServertime(){return servertime;}

    public void setServertime(Date servertime){this.servertime = servertime;}
}
