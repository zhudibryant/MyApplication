package com.example.zhudi.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.adapter.RecyclerAdapter;
import com.example.zhudi.myapplication.utils.Constant;
import com.example.zhudi.myapplication.utils.Utils;
import com.example.zhudi.myapplication.utils.ZuHeMethod;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //网络请求
    private static String urlFormat = Constant.serverURL + "/bjk/start";

    private Spinner spinner;
    private LinearLayout sanTongHao;
    private LinearLayout buTongHao;
    private LinearLayout erTongHaoDanXuan;
    private LinearLayout erTongHaoFuXuan;
    private LinearLayout heZhi, quickChoice;
    private LinearLayout sanLianHaoTongXuan;
    private LinearLayout withoutAddNum;

    private CheckBox buTongHaoOne, buTongHaoTwo, buTongHaoThree, buTongHaoFour, buTongHaoFive, buTongHaoSix;
    private CheckBox heZhiThree, heZhiFour, heZhiFive, heZhiSix, heZhiSeven, heZhiEight, heZhiNine, heZhiTen, heZhiEleven, heZhiTwelve, heZhiThirteen, heZhiFourteen, heZhiFifteen, heZhiSixteen, heZhiSeventeen, heZhiEighteen;

    private TextView Xiao, Da, Dan, Shuang, Quan, Qing;
    private TextView zhuShu;
    private TextView jinE;
    private RecyclerView recyclerView;
    private TextView tipForRecycler;

    private Button AddNum, ZhuiHao, Confirm;

    private ImageView Delete, Cart;

    //每个checkbox对应的静态数字变量
    static int ONE;
    static int TWO;
    static int THREE;
    static int FOUR;
    static int FIVE;
    static int SIX;
    static int SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE, THIRTEEN, FOURTEEN, FIFTEEN, SIXTEEN, SEVENTEEN, EIGHTEEN;

    //单个checkbox对应的权重
    static int WEIGHT;

    static long N;

    private String modeName;

    static final String[] MODE = new String[]{"和值", "三同号(单选/通选)", "二同号复选", "二同号单选", "三不同号", "二不同号", "三连号通选"};
    static ArrayList<String> arrayList = new ArrayList<String>();
    List<String> list;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar_without_back_icon);
        //toolbar.setTitle("首页");
        //toolbar.setBackgroundColor(Color.parseColor("#2b566e"));
        //此方法放在所有标题栏按钮事件函数的前面
        setSupportActionBar(toolbar);
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/


        spinner = findViewById(R.id.spinner);
        initSpnner();

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

        //获取“添加号码”控件的父控件
        withoutAddNum = findViewById(R.id.without_add_num);

        zhuShu = findViewById(R.id.zhu_shu);
        jinE = findViewById(R.id.jin_e);

        //使用RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        //获取RecyclerView操作提示控件
        tipForRecycler = findViewById(R.id.tip_for_recycler);
        //设置布局管理容器
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
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
                                zhuShu.setText("共" + N + "注");
                                jinE.setText("金额" + N * 2 + "元");
                            }
                        }).show().getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
            }
        });
    }


    //标题栏右侧的用户按钮和规则按钮
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            //String msg = "";
            switch (item.getItemId()) {
                case R.id.userInfo:
                    Log.e("show", "fuck");
                    seeUserInfo();
                    break;
                case R.id.rules:
                    seeRules();
                    break;

            }
            return true;
        }
    };

    //在Toolbar显示menu必须声明的方法
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void seeRules() {
        Intent seeRules = new Intent(this, RulesActivity.class);
        startActivity(seeRules);
    }

    private void seeUserInfo() {
        Intent seeUserInfo = new Intent(this, UserInfoActivity.class);
        startActivity(seeUserInfo);
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
                    //重置WEIGHT
                    WEIGHT = 0;
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
                    jinE.setText("金额0元");

                    Log.i("weight","weight -- " +WEIGHT);
                    sanTongHao.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.VISIBLE);
                    heZhi.setVisibility(View.VISIBLE);
                    buTongHao.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 1) {
                    sanTongHao.setVisibility(View.VISIBLE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    buTongHao.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 2) {
                    erTongHaoFuXuan.setVisibility(View.VISIBLE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    buTongHao.setVisibility(View.GONE);
                    sanTongHao.setVisibility(View.GONE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 3) {
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
                    jinE.setText("金额0元");
                    buTongHao.setVisibility(View.VISIBLE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    sanTongHao.setVisibility(View.GONE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                    sanLianHaoTongXuan.setVisibility(View.GONE);
                } else if (i == 6) {
                    sanLianHaoTongXuan.setVisibility(View.VISIBLE);
                    erTongHaoDanXuan.setVisibility(View.GONE);
                    sanTongHao.setVisibility(View.GONE);
                    buTongHao.setVisibility(View.GONE);
                    heZhi.setVisibility(View.GONE);
                    quickChoice.setVisibility(View.GONE);
                    erTongHaoFuXuan.setVisibility(View.GONE);
                }
                //在二不同号和三不同号的情况下，显示tip TextView和AddNum Button
                if (i == 4 || i == 5) {
                    tipForRecycler.setVisibility(View.VISIBLE);
                    withoutAddNum.setVisibility(View.VISIBLE);
                } else {
                    tipForRecycler.setVisibility(View.GONE);
                    withoutAddNum.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                    }
                }
                break;
            case R.id.add_num:
                //ArrayList 重置
                arrayList.clear();

                if (modeName.equals("三不同号")) {
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
                    }
                    N = ZuHeMethod.combination(arrayList.size(), 3);
                    list = ZuHeMethod.combinationSelect(arrayList.toArray(new String[arrayList.size()]), 3);
                    recyclerAdapter.onrefresh(list);
                    zhuShu.setText("共" + N + "注");
                    jinE.setText("金额" + N * 2 + "元");


                }
                if (modeName.equals("二不同号")) {
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
                    }
                    N = ZuHeMethod.combination(arrayList.size(), 2);
                    list = ZuHeMethod.combinationSelect(arrayList.toArray(new String[arrayList.size()]), 2);
                    recyclerAdapter.onrefresh(list);
                    zhuShu.setText("共" + N + "注");
                    jinE.setText("金额" + N * 2 + "元");

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
                                    jinE.setText("金额" + 0 + "元");
                                }
                            }).show().getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);

                }
                break;
            case R.id.num_cart:
                Intent CartBag = new Intent(this, CartInforActivity.class);
                startActivity(CartBag);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.he_zhi_three:
                if (b) {
                    THREE = 3;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));

                } else {
                    THREE = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_four:
                if (b) {
                    FOUR = 4;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Dan.setTextColor(Color.parseColor("#607D8B"));
                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));

                } else {
                    FOUR = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));

                }
                break;
            case R.id.he_zhi_five:
                if (b) {
                    FIVE = 5;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    FIVE = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_six:
                if (b) {
                    SIX = 6;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Dan.setTextColor(Color.parseColor("#607D8B"));
                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    SIX = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_seven:
                if (b) {
                    SEVEN = 7;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    SEVEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_eight:
                if (b) {
                    EIGHT = 8;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Dan.setTextColor(Color.parseColor("#607D8B"));
                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    EIGHT = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_nine:
                if (b) {
                    NINE = 9;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    NINE = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_ten:
                if (b) {
                    TEN = 10;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Dan.setTextColor(Color.parseColor("#607D8B"));
                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    TEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_eleven:
                if (b) {
                    ELEVEN = 11;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    ELEVEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_twelve:
                if (b) {
                    TWELVE = 12;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    TWELVE = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_thirteen:
                if (b) {
                    THIRTEEN = 13;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    THIRTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_fourteen:
                if (b) {
                    FOURTEEN = 14;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    FOURTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_fifteen:
                if (b) {
                    FIFTEEN = 15;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    FIFTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_sixteen:
                if (b) {
                    SIXTEEN = 16;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    SIXTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_seventeen:
                if (b) {
                    SEVENTEEN = 17;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    SEVENTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                }
                break;
            case R.id.he_zhi_eighteen:
                if (b) {
                    EIGHTEEN = 18;
                    WEIGHT = WEIGHT + 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Xiao.setTextColor(Color.parseColor("#607D8B"));
                    Qing.setTextColor(Color.parseColor("#607D8B"));
                    Dan.setTextColor(Color.parseColor("#607D8B"));
                } else {
                    EIGHTEEN = 0;
                    WEIGHT = WEIGHT - 1;
                    zhuShu.setText("共" + WEIGHT + "注");
                    jinE.setText("金额" + WEIGHT * 2 + "元");

                    Da.setTextColor(Color.parseColor("#607D8B"));
                    Shuang.setTextColor(Color.parseColor("#607D8B"));
                    Quan.setTextColor(Color.parseColor("#607D8B"));
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
