package com.example.zhudi.myapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.adapter.RecyclerAdapter;
import com.example.zhudi.myapplication.bean.BjKRecordBean;
import com.example.zhudi.myapplication.utils.Arith;
import com.example.zhudi.myapplication.utils.Constant;
import com.example.zhudi.myapplication.utils.ErTongHaoDanXuanMethod;
import com.example.zhudi.myapplication.utils.MyCountDownTimer;
import com.example.zhudi.myapplication.utils.RequestServer;
import com.example.zhudi.myapplication.utils.Utils;
import com.example.zhudi.myapplication.utils.ZuHeMethod;
import com.example.zhudi.myapplication.view.AmountView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int GAINRECORDSUCCESS = 1;
    private static final int GAINRECORDFAIL = 2;
    //网络请求
    private static String urlFormat = Constant.serverURL + "/bjk/start";

    private MyHandler mHandler = new MyHandler(new WeakReference<Activity>(this));

    private static class MyHandler extends Handler {
        private WeakReference<Activity> wf;

        public MyHandler(WeakReference<Activity> wf) {
            this.wf = wf;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MainActivity activity = (MainActivity) wf.get();
            if (activity == null)
                return;
            switch (msg.what) {
                case GAINRECORDFAIL:
                    Utils.alertShort(activity, "no-");
                    break;
                case GAINRECORDSUCCESS:
                    List<BjKRecordBean> list = (List<BjKRecordBean>) msg.obj;
                    Date serverTime = list.get(0).getServertime();
                    Date endTime = list.get(0).getEndtime();
                    long st = serverTime.getTime();
                    long et = endTime.getTime();
                    long reTime = (long) Arith.sub(et, st);

                    new MyCountDownTimer(reTime, 1000) {

                        @Override
                        public void onTick(long l) {
                            countDownTimer.setText(toClock(l));
                        }

                        @Override
                        public void onFinish() {
                            activity.gainServerData();
                        }
                    }.start();
                    Log.e("server", "time:" + serverTime + "end:" + endTime);
                    Log.e("server", "st:" + st + "   et:" + et + "    sub:" + Arith.sub(et, st) + "   reTime:" + reTime);
                    // Log.e("server","min:"++"  sec:" +);
                    break;
                default:

            }
        }
    }

    private Spinner spinner;
    private LinearLayout sanTongHao;
    private LinearLayout buTongHao;
    private LinearLayout erTongHaoDanXuan;
    private LinearLayout erTongHaoFuXuan;
    private LinearLayout heZhi, quickChoice;
    private LinearLayout sanLianHaoTongXuan;
    private LinearLayout withoutAddNum;
    private DrawerLayout mDrawerLayout;

    private CheckBox buTongHaoOne, buTongHaoTwo, buTongHaoThree, buTongHaoFour, buTongHaoFive, buTongHaoSix;
    private CheckBox erTongOne, erTongTwo, erTongThree, erTongFour, erTongFive, erTongSix;
    private CheckBox danOne, danTwo, danThree, danFour, danFive, danSix;
    private CheckBox heZhiThree, heZhiFour, heZhiFive, heZhiSix, heZhiSeven, heZhiEight, heZhiNine, heZhiTen, heZhiEleven, heZhiTwelve, heZhiThirteen, heZhiFourteen, heZhiFifteen, heZhiSixteen, heZhiSeventeen, heZhiEighteen;
    private CheckBox fuXuanOne, fuXuanTwo, fuXuanThree, fuXuanFour, fuXuanFive, fuXuanSix;
    private CheckBox sanTongOne, sanTongTwo, sanTongThree, sanTongFour, sanTongFive, sanTongSix;
    private CheckBox TongXuan;

    private RadioButton Yuan, Jiao;

    private static TextView prevNum, prevNumCode;
    private static TextView countDownTimer;
    private TextView Xiao, Da, Dan, Shuang, Quan, Qing;
    private TextView zhuShu, jinE, beiShu;
    private RecyclerView recyclerView;
    private TextView tipForRecycler;

    private Button AddNum, ZhuiHao, Confirm;

    private ImageView Delete, Cart;

    private AmountView mAmountView;

    //每个checkbox对应的静态数字变量
    private static int ONE, TWO, THREE, FOUR, FIVE, SIX;
    private static int D_ONE, D_TWO, D_THREE, D_FOUR, D_FIVE, D_SIX;
    private static int SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE, THIRTEEN, FOURTEEN, FIFTEEN, SIXTEEN, SEVENTEEN, EIGHTEEN;
    //单个checkbox对应的权重
    private static int WEIGHT = 0, D_WEIGHT = 0;
    private static double AMOUNT = 1;
    private static int N = 0;
    private static double MonetaryUnit = 2;

    private String modeName;

    private static final String[] MODE = new String[]{"和值", "三同号(单选/通选)", "二同号复选", "二同号单选", "三不同号", "二不同号", "三连号通选"};
    private static ArrayList<String> arrayList = new ArrayList<String>();
    //二同号双数接收集合
    private static ArrayList<String> doubleList = new ArrayList<String>();
    //二同号单数接收集合
    private static ArrayList<String> singleList = new ArrayList<String>();

    List<String> list;
    private RecyclerAdapter recyclerAdapter;


    //在Toolbar显示menu必须声明的方法
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void initView() {

        final Toolbar toolbarOfMain = (Toolbar) findViewById(R.id.toolBar);
        toolbarOfMain.setTitle("快3");
        //此方法放在所有标题栏按钮事件函数的前面
        setSupportActionBar(toolbarOfMain);

        //toolbar元素图标绑定点击事件
        toolbarOfMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //信息图标点击
                    case R.id.toolbar_message:
                        Log.i("Msg", "message");
                        break;
                }
                return false;
            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbarOfMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.left_drawer);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.nav_rules:
                                    seeRules();
                                    break;
                                case R.id.nav_me:
                                    seeUserInfo();
                                    break;
                            }
                            mDrawerLayout.closeDrawers();
                            return false;
                        }
                    }
            );
        }

        //开奖内容区域控件获取
        prevNum = findViewById(R.id.prev_num);
        prevNumCode = findViewById(R.id.prev_num_code);
        //开奖倒计时
        countDownTimer = findViewById(R.id.count_down_num);

        gainServerData();

        spinner = findViewById(R.id.spinner);
        initSpnner();

        //获取 AmountView mAmountView;
        mAmountView = findViewById(R.id.amount_view);
        mAmountView.setMax_amount(1000);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                AMOUNT = amount;
                beiShu.setText(amount + "倍");

                if (N == 0) {
                    double result = Arith.mul(Arith.mul(WEIGHT, MonetaryUnit), AMOUNT);
                    jinE.setText("金额" + result + "元");
                } else {
                    double result = Arith.mul(Arith.mul(N, MonetaryUnit), AMOUNT);
                    jinE.setText("金额" + result + "元");
                }
            }
        });

        //根据spinner选择变换数字选择模块
        heZhi = findViewById(R.id.he_zhi);
        quickChoice = findViewById(R.id.quick_choice);

        sanTongHao = findViewById(R.id.san_tong_hao);
        buTongHao = findViewById(R.id.bu_tong_hao);
        erTongHaoDanXuan = findViewById(R.id.er_tong_hao_dan_xuan);
        erTongHaoFuXuan = findViewById(R.id.er_tong_hao_fu_xuan);
        sanLianHaoTongXuan = findViewById(R.id.san_lian_hao_tong_xuan);
        //获取"不同号"数字控件
        buTongHaoOne = findViewById(R.id.bu_tong_hao_one);
        buTongHaoTwo = findViewById(R.id.bu_tong_hao_two);
        buTongHaoThree = findViewById(R.id.bu_tong_hao_three);
        buTongHaoFour = findViewById(R.id.bu_tong_hao_four);
        buTongHaoFive = findViewById(R.id.bu_tong_hao_five);
        buTongHaoSix = findViewById(R.id.bu_tong_hao_six);

        //"三同号"数字控件获取
        sanTongOne = findViewById(R.id.san_tong_hao_one);
        sanTongTwo = findViewById(R.id.san_tong_hao_two);
        sanTongThree = findViewById(R.id.san_tong_hao_three);
        sanTongFour = findViewById(R.id.san_tong_hao_four);
        sanTongFive = findViewById(R.id.san_tong_hao_five);
        sanTongSix = findViewById(R.id.san_tong_hao_six);

        //"三连号"数字控件获取
        TongXuan = findViewById(R.id.san_lian_hao_tong);

        //"二同号复选"数字控件获取
        fuXuanOne = findViewById(R.id.fu_xuan_one);
        fuXuanTwo = findViewById(R.id.fu_xuan_two);
        fuXuanThree = findViewById(R.id.fu_xuan_three);
        fuXuanFour = findViewById(R.id.fu_xuan_four);
        fuXuanFive = findViewById(R.id.fu_xuan_five);
        fuXuanSix = findViewById(R.id.fu_xuan_six);

        //"二同号单选"数字控件获取
        //获取"二同号"数字控件
        erTongOne = findViewById(R.id.er_tong_hao_one);
        erTongTwo = findViewById(R.id.er_tong_hao_two);
        erTongThree = findViewById(R.id.er_tong_hao_three);
        erTongFour = findViewById(R.id.er_tong_hao_four);
        erTongFive = findViewById(R.id.er_tong_hao_five);
        erTongSix = findViewById(R.id.er_tong_hao_six);
        //获取"单号"数字控件
        danOne = findViewById(R.id.dan_xuan_one);
        danTwo = findViewById(R.id.dan_xuan_two);
        danThree = findViewById(R.id.dan_xuan_three);
        danFour = findViewById(R.id.dan_xuan_four);
        danFive = findViewById(R.id.dan_xuan_five);
        danSix = findViewById(R.id.dan_xuan_six);

        //获取"和值"数字控件
        heZhiThree = findViewById(R.id.he_zhi_three);
        heZhiFour = findViewById(R.id.he_zhi_four);
        heZhiFive = findViewById(R.id.he_zhi_five);
        heZhiSix = findViewById(R.id.he_zhi_six);
        heZhiSeven = findViewById(R.id.he_zhi_seven);
        heZhiEight = findViewById(R.id.he_zhi_eight);
        heZhiNine = findViewById(R.id.he_zhi_nine);
        heZhiTen = findViewById(R.id.he_zhi_ten);
        heZhiEleven = findViewById(R.id.he_zhi_eleven);
        heZhiTwelve = findViewById(R.id.he_zhi_twelve);
        heZhiThirteen = findViewById(R.id.he_zhi_thirteen);
        heZhiFourteen = findViewById(R.id.he_zhi_fourteen);
        heZhiFifteen = findViewById(R.id.he_zhi_fifteen);
        heZhiSixteen = findViewById(R.id.he_zhi_sixteen);
        heZhiSeventeen = findViewById(R.id.he_zhi_seventeen);
        heZhiEighteen = findViewById(R.id.he_zhi_eighteen);

        //获取"快速选择"TextView控件
        Xiao = findViewById(R.id.xiao);
        Da = findViewById(R.id.da);
        Dan = findViewById(R.id.dan);
        Shuang = findViewById(R.id.shuang);
        Quan = findViewById(R.id.quan);
        Qing = findViewById(R.id.qing);

        //为"快速选择"TextView控件绑定点击事件
        Xiao.setOnClickListener(this);
        Da.setOnClickListener(this);
        Dan.setOnClickListener(this);
        Shuang.setOnClickListener(this);
        Quan.setOnClickListener(this);
        Qing.setOnClickListener(this);

        //为数字"不同号"控件绑定点击事件
        buTongHaoOne.setOnCheckedChangeListener(this);
        buTongHaoTwo.setOnCheckedChangeListener(this);
        buTongHaoThree.setOnCheckedChangeListener(this);
        buTongHaoFour.setOnCheckedChangeListener(this);
        buTongHaoFive.setOnCheckedChangeListener(this);
        buTongHaoSix.setOnCheckedChangeListener(this);

        //"三同号"数字控件绑定OnCheckChange事件
        sanTongOne.setOnCheckedChangeListener(this);
        sanTongTwo.setOnCheckedChangeListener(this);
        sanTongThree.setOnCheckedChangeListener(this);
        sanTongFour.setOnCheckedChangeListener(this);
        sanTongFive.setOnCheckedChangeListener(this);
        sanTongSix.setOnCheckedChangeListener(this);

        //"三连号"数字控件绑定OnCheckChange事件
        TongXuan.setOnCheckedChangeListener(this);

        //"二同号复选"数字控件绑定OnCheckChange事件
        fuXuanOne.setOnCheckedChangeListener(this);
        fuXuanTwo.setOnCheckedChangeListener(this);
        fuXuanThree.setOnCheckedChangeListener(this);
        fuXuanFour.setOnCheckedChangeListener(this);
        fuXuanFive.setOnCheckedChangeListener(this);
        fuXuanSix.setOnCheckedChangeListener(this);
        //"二同号单选"数字控件绑定onCheckChange事件
        //为数字"二同号"控件绑定点击事件
        erTongOne.setOnCheckedChangeListener(this);
        erTongTwo.setOnCheckedChangeListener(this);
        erTongThree.setOnCheckedChangeListener(this);
        erTongFour.setOnCheckedChangeListener(this);
        erTongFive.setOnCheckedChangeListener(this);
        erTongSix.setOnCheckedChangeListener(this);
        //为数字"单号"控件绑定点击事件
        danOne.setOnCheckedChangeListener(this);
        danTwo.setOnCheckedChangeListener(this);
        danThree.setOnCheckedChangeListener(this);
        danFour.setOnCheckedChangeListener(this);
        danFive.setOnCheckedChangeListener(this);
        danSix.setOnCheckedChangeListener(this);

        //为数字"和值"CheckBox控件绑定点击事件
        heZhiThree.setOnCheckedChangeListener(this);
        heZhiFour.setOnCheckedChangeListener(this);
        heZhiFive.setOnCheckedChangeListener(this);
        heZhiSix.setOnCheckedChangeListener(this);
        heZhiSeven.setOnCheckedChangeListener(this);
        heZhiEight.setOnCheckedChangeListener(this);
        heZhiNine.setOnCheckedChangeListener(this);
        heZhiTen.setOnCheckedChangeListener(this);
        heZhiEleven.setOnCheckedChangeListener(this);
        heZhiTwelve.setOnCheckedChangeListener(this);
        heZhiThirteen.setOnCheckedChangeListener(this);
        heZhiFourteen.setOnCheckedChangeListener(this);
        heZhiFifteen.setOnCheckedChangeListener(this);
        heZhiSixteen.setOnCheckedChangeListener(this);
        heZhiSeventeen.setOnCheckedChangeListener(this);
        heZhiEighteen.setOnCheckedChangeListener(this);

        //获取“添加号码”按钮 并绑定点击事件
        AddNum = findViewById(R.id.add_num);
        AddNum.setOnClickListener(this);
        //获取“追号”按钮 并绑定点击事件
        ZhuiHao = findViewById(R.id.zhui_hao);
        ZhuiHao.setOnClickListener(this);
        //获取“确认投注”按钮 并绑定点击事件
        Confirm = findViewById(R.id.confirm);
        Confirm.setOnClickListener(this);
        //获取“删除”ImageView 并绑定点击事件
        Delete = findViewById(R.id.delete);
        Delete.setOnClickListener(this);
        //获取“购物袋”ImageView 并绑定点击事件
        Cart = findViewById(R.id.num_cart);
        Cart.setOnClickListener(this);

        //获取“组合号码”控件的父控件
        withoutAddNum = findViewById(R.id.without_add_num);

        //获取"元+角"RadioButton控件
        Yuan = findViewById(R.id.yuan);
        Jiao = findViewById(R.id.jiao);
        //绑定点击事件
        Yuan.setOnCheckedChangeListener(this);
        Jiao.setOnCheckedChangeListener(this);

        zhuShu = findViewById(R.id.zhu_shu);
        jinE = findViewById(R.id.jin_e);
        beiShu = findViewById(R.id.bei_shu);
        beiShu.setText("1倍");
        //使用RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        //获取RecyclerView操作提示控件
        tipForRecycler = findViewById(R.id.tip_for_recycler);
        //设置布局管理容器
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdapter = new RecyclerAdapter(this, list);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(MainActivity.this).setTitle("确认删除？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                recyclerAdapter.removeDate(position);
                                N = N - 1;
                                getResultWithN();
                            }
                        }).show().getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
            }
        });
    }

    /**
     * laqushuju
     */
    private void gainServerData() {
        //创建recordBean实体 存储json数据
        final BjKRecordBean recordBean = new BjKRecordBean();
        final List<BjKRecordBean> timeList = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                String json = RequestServer.RequestServer(urlFormat);
                Log.e("msg", "Main:" + json);
                int code = 0;
                String data = null;
                Date serverTime = null;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    code = jsonObject.optInt("code", 0);
                    data = jsonObject.optString("data");
                    serverTime = JSON.parseObject(json).getDate("serverTime");
                    //将serverTime放入recordBean
                    recordBean.setServertime(serverTime);
                    timeList.add(recordBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (code == 1) {
                    //success
                    List<BjKRecordBean> list = JSON.parseArray(data, BjKRecordBean.class);
                    if (list != null && list.size() == 2) {
                        final String NumNow = list.get(0).getNum();
                        Date endTime = list.get(0).getEndtime();
                        //获取开奖时间 放入recordBean
                        recordBean.setEndtime(endTime);
                        timeList.add(recordBean);
                        final String NumPrev = list.get(1).getNum();
                        final String NumPrevCode = list.get(1).getOpencode();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                prevNum.setText("第" + NumPrev + "期开奖号码");
                                prevNumCode.setText(NumPrevCode);
                            }
                        });
                    } else if (list != null && list.size() == 1) {
                        String NumNow = list.get(0).getNum();
                        Date endTime = list.get(0).getEndtime();
                        //获取开奖时间 放入recordBean
                        recordBean.setEndtime(endTime);
                        timeList.add(recordBean);
                    }
                    Message msg = new Message();
                    msg.what = GAINRECORDSUCCESS;
                    msg.obj = timeList;
                    mHandler.sendMessage(msg);
                } else {
                    //fail
                    mHandler.sendEmptyMessage(GAINRECORDFAIL);
                }

            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //跳转"用户信息"页面
    private void seeUserInfo() {
        Intent seeUserInfo = new Intent(this, UserInfoActivity.class);
        startActivity(seeUserInfo);
    }

    //跳转"游戏规则"页面
    private void seeRules() {
        Intent seeRules = new Intent(this, RulesActivity.class);
        startActivity(seeRules);
    }

    private void initSpnner() {
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_item, MODE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                modeName = MODE[i];

                if (i == 0) {
                    //重置数字选择状态
                    heZhiThree.setChecked(false);
                    heZhiFour.setChecked(false);
                    heZhiFive.setChecked(false);
                    heZhiSix.setChecked(false);
                    heZhiSeven.setChecked(false);
                    heZhiEight.setChecked(false);
                    heZhiNine.setChecked(false);
                    heZhiTen.setChecked(false);
                    heZhiEleven.setChecked(false);
                    heZhiTwelve.setChecked(false);
                    heZhiThirteen.setChecked(false);
                    heZhiFourteen.setChecked(false);
                    heZhiFifteen.setChecked(false);
                    heZhiSixteen.setChecked(false);
                    heZhiSeventeen.setChecked(false);
                    heZhiEighteen.setChecked(false);
                    //清空RecyclerView内容
                    recyclerAdapter.onrefresh(null);
                    zhuShu.setText("共0注");
                    jinE.setText("金额0.0元");
                    //重置WEIGHT
                    WEIGHT = 0;
                    sanTongHao.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.VISIBLE);
                    heZhi.setVisibility(View.VISIBLE);
                    buTongHao.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 1) {

                    sanTongOne.setChecked(false);
                    sanTongTwo.setChecked(false);
                    sanTongThree.setChecked(false);
                    sanTongFour.setChecked(false);
                    sanTongFive.setChecked(false);
                    sanTongSix.setChecked(false);
                    //重置WEIGHT
                    WEIGHT = 0;
                    //清空RecyclerView内容
                    recyclerAdapter.onrefresh(null);
                    zhuShu.setText("共0注");
                    jinE.setText("金额0.0元");

                    sanTongHao.setVisibility(View.VISIBLE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    buTongHao.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 2) {

                    fuXuanOne.setChecked(false);
                    fuXuanTwo.setChecked(false);
                    fuXuanThree.setChecked(false);
                    fuXuanFour.setChecked(false);
                    fuXuanFive.setChecked(false);
                    fuXuanSix.setChecked(false);
                    //重置WEIGHT
                    WEIGHT = 0;
                    //清空RecyclerView内容
                    recyclerAdapter.onrefresh(null);
                    zhuShu.setText("共0注");
                    jinE.setText("金额0.0元");

                    erTongHaoFuXuan.setVisibility(View.VISIBLE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    buTongHao.setVisibility(View.GONE);
                    sanTongHao.setVisibility(View.GONE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 3) {

                    //重置数字选择状态
                    erTongOne.setChecked(false);
                    erTongTwo.setChecked(false);
                    erTongThree.setChecked(false);
                    erTongFour.setChecked(false);
                    erTongFive.setChecked(false);
                    erTongSix.setChecked(false);
                    danOne.setChecked(false);
                    danTwo.setChecked(false);
                    danThree.setChecked(false);
                    danFour.setChecked(false);
                    danFive.setChecked(false);
                    danSix.setChecked(false);
                    //重置weight
                    WEIGHT = 0;
                    D_WEIGHT = 0;
                    //清空recycler
                    recyclerAdapter.onrefresh(null);
                    zhuShu.setText("共0注");
                    jinE.setText("金额0.0元");
                    erTongHaoDanXuan.setVisibility(View.VISIBLE);
                    sanTongHao.setVisibility(View.GONE);
                    buTongHao.setVisibility(View.GONE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 4 || i == 5) {
                    //spinner选择状况发生变化时，对每个数字的选择状态进行重置
                    buTongHaoOne.setChecked(false);
                    buTongHaoTwo.setChecked(false);
                    buTongHaoThree.setChecked(false);
                    buTongHaoFour.setChecked(false);
                    buTongHaoFive.setChecked(false);
                    buTongHaoSix.setChecked(false);
                    //重置weight
                    WEIGHT = 0;
                    recyclerAdapter.onrefresh(null);
                    zhuShu.setText("共0注");
                    jinE.setText("金额0.0元");
                    buTongHao.setVisibility(View.VISIBLE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    sanTongHao.setVisibility(View.GONE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 6) {
                    TongXuan.setChecked(false);
                    //重置weight
                    WEIGHT = 0;
                    recyclerAdapter.onrefresh(null);
                    zhuShu.setText("共0注");
                    jinE.setText("金额0.0元");
                    sanLianHaoTongXuan.setVisibility(View.VISIBLE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    sanTongHao.setVisibility(View.GONE);
                    buTongHao.setVisibility(View.GONE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                }
                //在二不同号和三不同号的情况下，显示tip TextView和AddNum Button
                if (i == 4 || i == 5 || i == 3) {
                    tipForRecycler.setVisibility(View.VISIBLE);
                    withoutAddNum.setVisibility(View.VISIBLE);
                } else {
                    tipForRecycler.setVisibility(View.GONE);
                    withoutAddNum.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner.clearFocus();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xiao:
                Xiao.setTextColor(Color.RED);
                Quan.setTextColor(Color.parseColor("#607D8B"));
                Da.setTextColor(Color.parseColor("#607D8B"));
                Dan.setTextColor(Color.parseColor("#607D8B"));
                Shuang.setTextColor(Color.parseColor("#607D8B"));
                Qing.setTextColor(Color.parseColor("#607D8B"));
                heZhiThree.setChecked(true);
                heZhiFour.setChecked(true);
                heZhiFive.setChecked(true);
                heZhiSix.setChecked(true);
                heZhiSeven.setChecked(true);
                heZhiEight.setChecked(true);
                heZhiNine.setChecked(true);
                heZhiTen.setChecked(true);

                heZhiEleven.setChecked(false);
                heZhiTwelve.setChecked(false);
                heZhiThirteen.setChecked(false);
                heZhiFourteen.setChecked(false);
                heZhiFifteen.setChecked(false);
                heZhiSixteen.setChecked(false);
                heZhiSeventeen.setChecked(false);
                heZhiEighteen.setChecked(false);
                break;
            case R.id.da:
                Da.setTextColor(Color.RED);
                Xiao.setTextColor(Color.parseColor("#607D8B"));
                Quan.setTextColor(Color.parseColor("#607D8B"));
                Dan.setTextColor(Color.parseColor("#607D8B"));
                Shuang.setTextColor(Color.parseColor("#607D8B"));
                Qing.setTextColor(Color.parseColor("#607D8B"));
                heZhiEleven.setChecked(true);
                heZhiTwelve.setChecked(true);
                heZhiThirteen.setChecked(true);
                heZhiFourteen.setChecked(true);
                heZhiFifteen.setChecked(true);
                heZhiSixteen.setChecked(true);
                heZhiSeventeen.setChecked(true);
                heZhiEighteen.setChecked(true);

                heZhiThree.setChecked(false);
                heZhiFour.setChecked(false);
                heZhiFive.setChecked(false);
                heZhiSix.setChecked(false);
                heZhiSeven.setChecked(false);
                heZhiEight.setChecked(false);
                heZhiNine.setChecked(false);
                heZhiTen.setChecked(false);
                break;
            case R.id.dan:
                Dan.setTextColor(Color.RED);
                Xiao.setTextColor(Color.parseColor("#607D8B"));
                Da.setTextColor(Color.parseColor("#607D8B"));
                Quan.setTextColor(Color.parseColor("#607D8B"));
                Shuang.setTextColor(Color.parseColor("#607D8B"));
                Qing.setTextColor(Color.parseColor("#607D8B"));
                heZhiThree.setChecked(true);
                heZhiFive.setChecked(true);
                heZhiSeven.setChecked(true);
                heZhiNine.setChecked(true);
                heZhiEleven.setChecked(true);
                heZhiThirteen.setChecked(true);
                heZhiFifteen.setChecked(true);
                heZhiSeventeen.setChecked(true);

                heZhiFour.setChecked(false);
                heZhiSix.setChecked(false);
                heZhiEight.setChecked(false);
                heZhiTen.setChecked(false);
                heZhiTwelve.setChecked(false);
                heZhiFourteen.setChecked(false);
                heZhiSixteen.setChecked(false);
                heZhiEighteen.setChecked(false);
                break;
            case R.id.shuang:
                Shuang.setTextColor(Color.RED);
                Xiao.setTextColor(Color.parseColor("#607D8B"));
                Da.setTextColor(Color.parseColor("#607D8B"));
                Dan.setTextColor(Color.parseColor("#607D8B"));
                Quan.setTextColor(Color.parseColor("#607D8B"));
                Qing.setTextColor(Color.parseColor("#607D8B"));
                heZhiFour.setChecked(true);
                heZhiSix.setChecked(true);
                heZhiEight.setChecked(true);
                heZhiTen.setChecked(true);
                heZhiTwelve.setChecked(true);
                heZhiFourteen.setChecked(true);
                heZhiSixteen.setChecked(true);
                heZhiEighteen.setChecked(true);

                heZhiThree.setChecked(false);
                heZhiFive.setChecked(false);
                heZhiSeven.setChecked(false);
                heZhiNine.setChecked(false);
                heZhiEleven.setChecked(false);
                heZhiThirteen.setChecked(false);
                heZhiFifteen.setChecked(false);
                heZhiSeventeen.setChecked(false);
                break;
            case R.id.qing:
                Qing.setTextColor(Color.RED);
                Xiao.setTextColor(Color.parseColor("#607D8B"));
                Da.setTextColor(Color.parseColor("#607D8B"));
                Dan.setTextColor(Color.parseColor("#607D8B"));
                Shuang.setTextColor(Color.parseColor("#607D8B"));
                Quan.setTextColor(Color.parseColor("#607D8B"));
                heZhiThree.setChecked(false);
                heZhiFour.setChecked(false);
                heZhiFive.setChecked(false);
                heZhiSix.setChecked(false);
                heZhiSeven.setChecked(false);
                heZhiEight.setChecked(false);
                heZhiNine.setChecked(false);
                heZhiTen.setChecked(false);
                heZhiEleven.setChecked(false);
                heZhiTwelve.setChecked(false);
                heZhiThirteen.setChecked(false);
                heZhiFourteen.setChecked(false);
                heZhiFifteen.setChecked(false);
                heZhiSixteen.setChecked(false);
                heZhiSeventeen.setChecked(false);
                heZhiEighteen.setChecked(false);
                break;
            case R.id.quan:
                Quan.setTextColor(Color.RED);
                Xiao.setTextColor(Color.parseColor("#607D8B"));
                Da.setTextColor(Color.parseColor("#607D8B"));
                Dan.setTextColor(Color.parseColor("#607D8B"));
                Shuang.setTextColor(Color.parseColor("#607D8B"));
                Qing.setTextColor(Color.parseColor("#607D8B"));
                heZhiThree.setChecked(true);
                heZhiFour.setChecked(true);
                heZhiFive.setChecked(true);
                heZhiSix.setChecked(true);
                heZhiSeven.setChecked(true);
                heZhiEight.setChecked(true);
                heZhiNine.setChecked(true);
                heZhiTen.setChecked(true);
                heZhiEleven.setChecked(true);
                heZhiTwelve.setChecked(true);
                heZhiThirteen.setChecked(true);
                heZhiFourteen.setChecked(true);
                heZhiFifteen.setChecked(true);
                heZhiSixteen.setChecked(true);
                heZhiSeventeen.setChecked(true);
                heZhiEighteen.setChecked(true);
                break;
            case R.id.confirm:
                //ArrayList重置
                arrayList.clear();
                if (modeName.equals("和值")) {
                    if (WEIGHT < 1) {
                        Utils.alertShort(this, "请先选择和值号码");
                    } else {
                        if (THREE != 0) {
                            arrayList.add("3");
                        }
                        if (FOUR != 0) {
                            arrayList.add("4");
                        }
                        if (FIVE != 0) {
                            arrayList.add("5");
                        }
                        if (SIX != 0) {
                            arrayList.add("6");
                        }
                        if (SEVEN != 0) {
                            arrayList.add("7");
                        }
                        if (EIGHT != 0) {
                            arrayList.add("8");
                        }
                        if (NINE != 0) {
                            arrayList.add("9");
                        }
                        if (TEN != 0) {
                            arrayList.add("10");
                        }
                        if (ELEVEN != 0) {
                            arrayList.add("11");
                        }
                        if (TWELVE != 0) {
                            arrayList.add("12");
                        }
                        if (THIRTEEN != 0) {
                            arrayList.add("13");
                        }
                        if (FOURTEEN != 0) {
                            arrayList.add("14");
                        }
                        if (FIFTEEN != 0) {
                            arrayList.add("15");
                        }
                        if (SIXTEEN != 0) {
                            arrayList.add("16");
                        }
                        if (SEVENTEEN != 0) {
                            arrayList.add("17");
                        }
                        if (EIGHTEEN != 0) {
                            arrayList.add("18");
                        }
                    }
                }
                if (modeName.equals("二不同号") || modeName.equals("三不同号")) {
                    if (list == null) {
                        Utils.alertShort(this, "请您先组合号码");
                    } else {
                        //提交号码到服务器
                    }
                }
                break;
            case R.id.add_num:
                //ArrayList 重置
                arrayList.clear();
                doubleList.clear();
                singleList.clear();

                if (modeName.equals("二同号单选")) {
                    if (WEIGHT == 0 && D_WEIGHT == 0) {
                        Utils.alertShort(this, "请选择至少一对二同号和单号");
                    } else {
                        if (D_ONE != 0) {
                            doubleList.add("1");
                        }
                        if (D_TWO != 0) {
                            doubleList.add("2");
                        }
                        if (D_THREE != 0) {
                            doubleList.add("3");
                        }
                        if (D_FOUR != 0) {
                            doubleList.add("4");
                        }
                        if (D_FIVE != 0) {
                            doubleList.add("5");
                        }
                        if (D_SIX != 0) {
                            doubleList.add("6");
                        }
                        if (ONE != 0) {
                            singleList.add("1");
                        }
                        if (TWO != 0) {
                            singleList.add("2");
                        }
                        if (THREE != 0) {
                            singleList.add("3");
                        }
                        if (FOUR != 0) {
                            singleList.add("4");
                        }
                        if (FIVE != 0) {
                            singleList.add("5");
                        }
                        if (SIX != 0) {
                            singleList.add("6");
                        }
                        for (String nub_d : doubleList) {
                            Log.i("doubleList----", nub_d);
                        }
                        for (String nub_s : singleList) {
                            Log.i("singleList----", nub_s);
                        }

                        list = ErTongHaoDanXuanMethod.MixNumber(doubleList.toArray(new String[doubleList.size()]), singleList.toArray(new String[singleList.size()]));
                        if (list.size() == 0) {
                            Utils.alertShort(this, "双号和单号选择冲突，请避开三同号");
                        }
                        recyclerAdapter.onrefresh(list);
                        N = list.size();
                        getResultWithN();
                    }
                } else if (modeName.equals("三不同号")) {
                    if (WEIGHT < 3) {
                        Utils.alertShort(this, "选择数字不能小于3个");
                    } else {
                        if (ONE != 0) {
                            arrayList.add("1");
                        }
                        if (TWO != 0) {
                            arrayList.add("2");
                        }
                        if (THREE != 0) {
                            arrayList.add("3");
                        }
                        if (FOUR != 0) {
                            arrayList.add("4");
                        }
                        if (FIVE != 0) {
                            arrayList.add("5");
                        }
                        if (SIX != 0) {
                            arrayList.add("6");
                        }

                        N = (int) ZuHeMethod.combination(arrayList.size(), 3);
                        list = ZuHeMethod.combinationSelect(arrayList.toArray(new String[arrayList.size()]), 3);
                        recyclerAdapter.onrefresh(list);
                        getResultWithN();
                    }


                } else if (modeName.equals("二不同号")) {
                    if (WEIGHT < 2) {
                        Utils.alertShort(this, "选择数字不能小于2个");
                    } else {
                        if (ONE != 0) {
                            arrayList.add("1");
                        }
                        if (TWO != 0) {
                            arrayList.add("2");
                        }
                        if (THREE != 0) {
                            arrayList.add("3");
                        }
                        if (FOUR != 0) {
                            arrayList.add("4");
                        }
                        if (FIVE != 0) {
                            arrayList.add("5");
                        }
                        if (SIX != 0) {
                            arrayList.add("6");
                        }
                        N = (int) ZuHeMethod.combination(arrayList.size(), 2);
                        list = ZuHeMethod.combinationSelect(arrayList.toArray(new String[arrayList.size()]), 2);
                        recyclerAdapter.onrefresh(list);
                        getResultWithN();
                    }
                }
                break;
            case R.id.zhui_hao:
                if (modeName.equals("二不同号") || modeName.equals("三不同号")) {
                    if (list == null) {
                        Utils.alertShort(this, "请您先组合号码,再追号");
                    }
                }
                break;
            case R.id.delete:
                if (list != null) {
                    new AlertDialog.Builder(this)
                            .setMessage("确定要清空所有 (组合号码) 吗？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    list = null;
                                    recyclerAdapter.onrefresh(list);
                                    zhuShu.setText("共" + 0 + "注");
                                    jinE.setText("金额" + 0.0 + "元");
                                }
                            }).show().getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);

                }
                break;
            case R.id.num_cart:
                Intent CartBag = new Intent(this, CartInforActivity.class);
                startActivity(CartBag);
        }
    }

    //不需要进行数字组合的情况下 计算 元 角 情况下不同的金额
    public void getResultWithWeight() {
        double result = Arith.mul(Arith.mul(WEIGHT, MonetaryUnit), AMOUNT);
        zhuShu.setText("共" + WEIGHT + "注");
        jinE.setText("金额" + result + "元");
    }

    //需要进行数字组合的情况下 计算 元 角 情况下不同的金额
    public void getResultWithN() {
        double result = Arith.mul(Arith.mul(N, MonetaryUnit), AMOUNT);
        zhuShu.setText("共" + N + "注");
        jinE.setText("金额" + result + "元");
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.yuan:
                if (b) {
                    MonetaryUnit = 2;
                    if (N == 0) {
                        double result = Arith.mul(Arith.mul(WEIGHT, MonetaryUnit), AMOUNT);
                        jinE.setText("金额" + result + "元");
                    } else {
                        double result = Arith.mul(Arith.mul(N, MonetaryUnit), AMOUNT);
                        jinE.setText("金额" + result + "元");
                    }
                }
                break;
            case R.id.jiao:
                if (b) {
                    MonetaryUnit = 0.2;
                    if (N == 0) {
                        double result = Arith.mul(Arith.mul(WEIGHT, MonetaryUnit), AMOUNT);
                        jinE.setText("金额" + result + "元");
                    } else {
                        double result = Arith.mul(Arith.mul(N, MonetaryUnit), AMOUNT);
                        jinE.setText("金额" + result + "元");
                    }
                }
                break;
            case R.id.san_lian_hao_tong:
                if (b) {
                    WEIGHT = 4;
                    getResultWithWeight();
                } else {
                    WEIGHT = 0;
                    getResultWithWeight();
                }
                break;
            case R.id.er_tong_hao_one:
                if (b) {
                    D_ONE = 1;
                    D_WEIGHT = D_WEIGHT + 1;
                } else {
                    D_ONE = 0;
                    D_WEIGHT = D_WEIGHT - 1;
                }
                break;
            case R.id.er_tong_hao_two:
                if (b) {
                    D_TWO = 2;
                    D_WEIGHT = D_WEIGHT + 1;
                } else {
                    D_TWO = 0;
                    D_WEIGHT = D_WEIGHT - 1;
                }
                break;
            case R.id.er_tong_hao_three:
                if (b) {
                    D_THREE = 3;
                    D_WEIGHT = D_WEIGHT + 1;
                } else {
                    D_THREE = 0;
                    D_WEIGHT = D_WEIGHT - 1;
                }
                break;
            case R.id.er_tong_hao_four:
                if (b) {
                    D_FOUR = 4;
                    D_WEIGHT = D_WEIGHT + 1;
                } else {
                    D_FOUR = 0;
                    D_WEIGHT = D_WEIGHT - 1;
                }
                break;
            case R.id.er_tong_hao_five:
                if (b) {
                    D_FIVE = 5;
                    D_WEIGHT = D_WEIGHT + 1;
                } else {
                    D_FIVE = 0;
                    D_WEIGHT = D_WEIGHT - 1;
                }
                break;
            case R.id.er_tong_hao_six:
                if (b) {
                    D_SIX = 6;
                    D_WEIGHT = D_WEIGHT + 1;
                } else {
                    D_SIX = 0;
                    D_WEIGHT = D_WEIGHT - 1;
                }
                break;

            case R.id.dan_xuan_one:
                if (b) {
                    ONE = 1;
                    WEIGHT = WEIGHT + 1;
                } else {
                    ONE = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.dan_xuan_two:
                if (b) {
                    TWO = 2;
                    WEIGHT = WEIGHT + 1;
                } else {
                    TWO = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.dan_xuan_three:
                if (b) {
                    THREE = 3;
                    WEIGHT = WEIGHT + 1;
                } else {
                    THREE = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.dan_xuan_four:
                if (b) {
                    FOUR = 4;
                    WEIGHT = WEIGHT + 1;
                } else {
                    FOUR = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.dan_xuan_five:
                if (b) {
                    FIVE = 5;
                    WEIGHT = WEIGHT + 1;
                } else {
                    FIVE = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.dan_xuan_six:
                if (b) {
                    SIX = 6;
                    WEIGHT = WEIGHT + 1;
                } else {
                    SIX = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;

            case R.id.he_zhi_three:
                if (b) {
                    THREE = 3;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));

                } else {
                    THREE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_four:
                if (b) {
                    FOUR = 4;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Dan.setTextColor(Color.parseColor("#607D8B"));
                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));

                } else {
                    FOUR = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));

                }
                break;
            case R.id.he_zhi_five:
                if (b) {
                    FIVE = 5;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    FIVE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_six:
                if (b) {
                    SIX = 6;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    SIX = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_seven:
                if (b) {
                    SEVEN = 7;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    SEVEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_eight:
                if (b) {
                    EIGHT = 8;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Dan.setTextColor(Color.parseColor("#607D8B"));
                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    EIGHT = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_nine:
                if (b) {
                    NINE = 9;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    NINE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_ten:
                if (b) {
                    TEN = 10;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Dan.setTextColor(Color.parseColor("#607D8B"));
                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    TEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_eleven:
                if (b) {
                    ELEVEN = 11;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    ELEVEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_twelve:
                if (b) {
                    TWELVE = 12;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    TWELVE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_thirteen:
                if (b) {
                    THIRTEEN = 13;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    THIRTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_fourteen:
                if (b) {
                    FOURTEEN = 14;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    FOURTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_fifteen:
                if (b) {
                    FIFTEEN = 15;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    FIFTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_sixteen:
                if (b) {
                    SIXTEEN = 16;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    SIXTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_seventeen:
                if (b) {
                    SEVENTEEN = 17;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    SEVENTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_eighteen:
                if (b) {
                    EIGHTEEN = 18;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    EIGHTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                    ;

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.fu_xuan_one:
                if (b) {
                    ONE = 1;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    ONE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.fu_xuan_two:
                if (b) {
                    TWO = 2;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    TWO = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.fu_xuan_three:
                if (b) {
                    THREE = 3;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    THREE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.fu_xuan_four:
                if (b) {
                    FOUR = 4;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    FOUR = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.fu_xuan_five:
                if (b) {
                    FIVE = 5;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    FIVE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.fu_xuan_six:
                if (b) {
                    SIX = 6;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    SIX = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;

            case R.id.san_tong_hao_one:
                if (b) {
                    ONE = 1;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    ONE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.san_tong_hao_two:
                if (b) {
                    TWO = 2;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    TWO = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.san_tong_hao_three:
                if (b) {
                    THREE = 3;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    THREE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.san_tong_hao_four:
                if (b) {
                    FOUR = 4;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    FOUR = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.san_tong_hao_five:
                if (b) {
                    FIVE = 5;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    FIVE = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;
            case R.id.san_tong_hao_six:
                if (b) {
                    SIX = 6;
                    WEIGHT = WEIGHT + 1;
                    getResultWithWeight();
                } else {
                    SIX = 0;
                    WEIGHT = WEIGHT - 1;
                    getResultWithWeight();
                }
                break;

            case R.id.bu_tong_hao_one:
                if (b) {
                    ONE = 1;
                    //选中就给权重变量加一
                    WEIGHT = WEIGHT + 1;
                } else {
                    ONE = 0;
                    //取消选中，就给权重变量减一
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.bu_tong_hao_two:
                if (b) {
                    TWO = 2;
                    WEIGHT = WEIGHT + 1;
                } else {
                    TWO = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.bu_tong_hao_three:
                if (b) {
                    THREE = 3;
                    WEIGHT = WEIGHT + 1;
                } else {
                    THREE = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.bu_tong_hao_four:
                if (b) {
                    FOUR = 4;
                    WEIGHT = WEIGHT + 1;
                } else {
                    FOUR = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.bu_tong_hao_five:
                if (b) {
                    FIVE = 5;
                    WEIGHT = WEIGHT + 1;
                } else {
                    FIVE = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
            case R.id.bu_tong_hao_six:
                if (b) {
                    SIX = 6;
                    WEIGHT = WEIGHT + 1;
                } else {
                    SIX = 0;
                    WEIGHT = WEIGHT - 1;
                }
                break;
        }
    }
}
