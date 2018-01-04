package com.example.zhudi.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.utils.AddToolBar;

public class RulesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        initView();
    }

    @Override
    public void initView() {
        AddToolBar.addToolBar(this,"规则介绍","#4DB6AC");
    }
}
