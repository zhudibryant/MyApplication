package com.example.zhudi.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.utils.AddToolBar;


public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivAlipay;
    private ImageView ivQA;

    private LinearLayout ContectUs;
    private LinearLayout rootView;

    private TextView tvContectUs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        //设置联系我们的内容和超链接
        tvContectUs.setText("遇到任何问题，请发送邮件至 lingwu.ren@qq.com \n我们会在24小时之内进行解决并回复，谢谢您的理解和支持。");
    }

    @Override
    public void initView() {

        AddToolBar.addToolBar(this, "个人信息", "#2b566e");
        //获取全局界面
        rootView = (LinearLayout) findViewById(R.id.rootView);

        ivAlipay = (ImageView) findViewById(R.id.ivAlipay);
        ivQA = (ImageView) findViewById(R.id.ivQA);

        ContectUs = (LinearLayout) findViewById(R.id.ContectUs);
        tvContectUs = (TextView) findViewById(R.id.tvContectUs);
        //点击事件
        ivAlipay.setOnClickListener(this);
        ivQA.setOnClickListener(this);
        rootView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //联系我们
            case R.id.ivQA:
                ContectUs.setVisibility(View.VISIBLE);
                break;
            //点击提交进行微信支付的绑定
            /*case R.id.btnWechatSubmit:
                String wechat = etBandWechat.getText().toString();
                if (TextUtils.isEmpty(wechat)) {
                    Utils.alertShort(this, "请输入账号");
                    return;
                } else {
                    Utils.alertShort(this, "成功绑定");
                }
                BandWechat.setVisibility(View.GONE);
                break;
                */
            //点击提交进行支付宝的绑定
            /*case R.id.btnAlipaySubmit:
                String alipay = etBandAlipay.getText().toString();
                if (TextUtils.isEmpty(alipay)) {
                    Utils.alertShort(this, "请输入账号");
                    return;
                } else {
                    Utils.alertShort(this, "成功绑定");
                }
                BandAlipay.setVisibility(View.GONE);
                break;
                */
            //点击空白处隐藏绑定支付宝，绑微信，联系我们弹出的EditText
            case R.id.rootView:
                ContectUs.setVisibility(View.GONE);
                break;
        }
    }
}
