package com.example.zhudi.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.utils.Constant;
import com.example.zhudi.myapplication.utils.RequestServer;
import com.example.zhudi.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "url";
    //Web服务网址
    private static String urlFormat = Constant.serverURL + "/user/login?" + "username=%s&psw=%s";

    public EditText etUser;
    private EditText etPassword;
    private Button btnRegister;
    private Button btnLogin;
    private TextView tvFindCode;
    private TextView tvMsg;
    private String login;
    private String password;
    private String msg;


    private MyHandler mHandler = new MyHandler(new WeakReference<Activity>(this));
    private static final int LOGINSUCCESS = 1;
    private static final int LOGINFAIL = 0;

    private static class MyHandler extends Handler {
        private WeakReference<Activity> wf;

        public MyHandler(WeakReference<Activity> wf) {
            this.wf = wf;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginActivity activity = (LoginActivity) wf.get();
            if (activity == null)
                return;
            switch (msg.what) {
                case LOGINSUCCESS:
                    activity.loginSuccess();
                    break;
                case LOGINFAIL:
                    activity.loginFail();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }


    @Override
    public void initView() {
        //标题栏
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("登录");
        toolBar.setBackgroundColor(Color.parseColor("#2b566e"));
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etUser = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvFindCode = (TextView) findViewById(R.id.tvFindCode);
        tvMsg = (TextView) findViewById(R.id.tvMsg);

        etUser.setOnClickListener(this);
        etPassword.setOnClickListener(this);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvFindCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.etUser:
                tvMsg.setVisibility(View.INVISIBLE);
                break;
            case R.id.etPassword:
                tvMsg.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnLogin:
                login = etUser.getText().toString();
                password = etPassword.getText().toString();
                //判断手机号是否为空
                if (login == null || "".equals(login)) {
                    Utils.alertShort(this, "请输入正确的账号");
                    return;
                }
                //判断密码是否为空
                if (TextUtils.isEmpty(password)) {
                    Utils.alertShort(this, "请输入密码");
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        String urlString = String.format(urlFormat, login, password);
                        String json = RequestServer.RequestServer(urlString);
                        Log.i("json","---"+json);
                        int code = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            code = jsonObject.optInt("code");
                            msg = jsonObject.optString("msg");
                            //Log.i("code","---"+code);
                            //Log.i("msg","---"+msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code == 1) {
                            mHandler.sendEmptyMessage(LOGINSUCCESS);
                        } else {
                            mHandler.sendEmptyMessage(LOGINFAIL);
                        }
                    }
                }.start();
                break;
            case R.id.btnRegister:
                //Log.e("error","break");
                Intent register = new Intent(this, RegisterActivity.class);
                startActivityForResult(register, 1001);
                break;
            case R.id.tvFindCode:
                Intent findCode = new Intent(this, ForgetPasswordActivity.class);
                startActivity(findCode);
                break;
        }
    }

    private void loginSuccess() {
        Intent doLogin = new Intent(this, MainActivity.class);
        startActivity(doLogin);
    }
    private void loginFail(){
        tvMsg.setVisibility(View.VISIBLE);
        tvMsg.setText("登录失败，请核对您的账号和密码");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1001:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String account = data.getStringExtra("account");
                        etUser.setText(account);
                    }
                }
                break;
        }
    }
}

