package com.example.zhudi.myapplication.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.utils.AddToolBar;
import com.example.zhudi.myapplication.utils.Utils;

public class ResetPassword extends BaseActivity implements View.OnClickListener {

    private EditText etResetPassword;
    private Button btnDone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
    }

    @Override
    public void initView() {

        AddToolBar.addToolBar(this,"新密码","#4DB6AC");

        etResetPassword = (EditText)findViewById(R.id.etResetPassword);
        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDone:
                String newCode = etResetPassword.getText().toString();
                if (TextUtils.isEmpty(newCode)){
                    Utils.alertShort(this,"输入新密码");
                }else if(newCode.length()<6){
                    Utils.alertShort(this,"密码不能少于6位");
                }else{
                    Intent intent = new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }
        }
    }
}
