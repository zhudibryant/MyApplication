package com.example.zhudi.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.utils.AddToolBar;
import com.example.zhudi.myapplication.utils.Utils;


public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText jine;
    private Button btn_payture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        AddToolBar.addToolBar(this, "充值", "#2b566e");

        jine = findViewById(R.id.jine);
        btn_payture = findViewById(R.id.btn_payture);
        btn_payture.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_payture:
                String b = jine.getText().toString();

                if (b.equals("")) {
                    Utils.alertShort(this, "没有输入哦亲！");
                } else if (Integer.parseInt(b) < 1) {
                    Utils.alertShort(this, "无效金额！");
                } else {
                    Utils.alertShort(this, "您充值了" + Integer.parseInt(b) + "元");
                }
                break;
        }
    }
}
