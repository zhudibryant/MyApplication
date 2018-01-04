package com.example.zhudi.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.utils.AddToolBar;
import com.example.zhudi.myapplication.utils.Utils;

/**
 * Created by ww on 2017/9/19.
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText etInputPhone;
    private EditText etSCode;
    private TextView tvGetSCode;
    private Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget);
        initView();
    }

    @Override
    public void initView() {

        AddToolBar.addToolBar(this, "找回密码", "#4DB6AC");

        etInputPhone = (EditText) findViewById(R.id.etInputPhone);
        etSCode = (EditText) findViewById(R.id.etSCode);
        tvGetSCode = (TextView) findViewById(R.id.tvGetSCode);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                String phoneNumber = etInputPhone.getText().toString();
                String sCode = etSCode.getText().toString();
                boolean legalNumber = isMobie(phoneNumber);
                if (legalNumber == false) {
                    Utils.alertShort(this, "请输入有效的手机号");
                    return;
                }else if (TextUtils.isEmpty(sCode)){
                    Utils.alertShort(this,"收到短信后输入验证码");
                    return;
                }else {
                    Intent intent = new Intent(this,ResetPassword.class);
                    startActivity(intent);
                }

                break;
        }
    }

    private boolean isMobie(String phoneNumber) {
         /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return phoneNumber.matches(num);
        }
    }
}
