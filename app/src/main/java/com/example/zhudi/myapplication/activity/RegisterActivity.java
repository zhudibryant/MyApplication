package com.example.zhudi.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.utils.AddToolBar;
import com.example.zhudi.myapplication.utils.Constant;
import com.example.zhudi.myapplication.utils.RequestServer;
import com.example.zhudi.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import static android.os.SystemClock.sleep;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "url";

    //Web服务网址
    private static String urlFormat = Constant.serverURL + "/user/registered?" + "username=%s&psw=%s";

    private EditText etAccount;
    private EditText etPassword;
    private Button btnSubmit;
    private TextView tvMsg;
    private String msg;
    private MyHandler mHandler = new MyHandler(new WeakReference<Activity>(this));
    private static final int REGISTERSUCCESS = 1;
    private static final int REGISTERFAIL = 0;
    private static final int GOLOGIN = 2;
    private String account;
    private String password;

    private static class MyHandler extends Handler {
        private WeakReference<Activity> wf;

        public MyHandler(WeakReference<Activity> wf) {
            this.wf = wf;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RegisterActivity activity = (RegisterActivity) wf.get();
            if (activity == null)
                return;
            switch (msg.what) {
                case REGISTERFAIL:
                    activity.registerFail();
                    break;
                case REGISTERSUCCESS:
                    activity.registerSuccess();
                    break;
                case GOLOGIN:
                    Intent intent = activity.getIntent();
                    intent.putExtra("account", activity.account);
                    activity.setResult(RESULT_OK, intent);
                    activity.finish();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    @Override
    public void initView() {
        //定义标题栏
        AddToolBar.addToolBar(this, "注册", "#2b566e");

        etAccount = (EditText) findViewById(R.id.etAccount);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        //显示注册成功/失败信息
        tvMsg = (TextView) findViewById(R.id.tvMsg);

        btnSubmit.setOnClickListener(this);
        etAccount.setOnClickListener(this);
        etPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        account = etAccount.getText().toString();
        password = etPassword.getText().toString();

        switch (view.getId()) {
            case R.id.etAccount:
                tvMsg.setVisibility(View.INVISIBLE);
                break;
            case R.id.etPassword:
                tvMsg.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnSubmit:

                if (account == null || "".equals(account)) {
                    Utils.alertShort(this, "请创建账号");
                } else if (password == null || "".equals(password)) {
                    Utils.alertShort(this, "请创建密码");
                } else if (password.length() < 6 || password.length() > 12) {
                    Utils.alertShort(this, "密码不能小于6位且大于12位");
                } else {

                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            String urlString = String.format(urlFormat, account, password);
                            String json = RequestServer.RequestServer(urlString);
                            int status = 0;
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                status = jsonObject.optInt("status");
                                msg = jsonObject.optString("msg");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (status == 1) {
                                mHandler.sendEmptyMessage(REGISTERSUCCESS);
                            } else {
                                mHandler.sendEmptyMessage(REGISTERFAIL);
                            }
                        }
                    };
                    t.start();

                }
                break;
        }
    }

    private void registerSuccess() {

        tvMsg.setVisibility(View.VISIBLE);
        tvMsg.setText("注册成功，欢迎您——" + account);
        mHandler.sendEmptyMessageDelayed(GOLOGIN, 3000);

    }

    private void registerFail() {
        tvMsg.setVisibility(View.VISIBLE);
        tvMsg.setText("注册失败，名称已被注册。可能是网络丢包，谁知道呢，后台的锅。");
    }

}
