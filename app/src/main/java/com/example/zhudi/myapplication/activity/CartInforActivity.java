package com.example.zhudi.myapplication.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.adapter.CartRecyclerAdapter;
import com.example.zhudi.myapplication.utils.Constant;
import com.example.zhudi.myapplication.utils.GlobalParameters;
import com.example.zhudi.myapplication.utils.RequestServer;

import java.util.ArrayList;
import java.util.List;

public class CartInforActivity extends AppCompatActivity {

    //网络请求
    private static String urlFormat = Constant.serverURL +"/bjk/order?" + "userId=%s";
    private List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_infor);

        inintView();
    }

    public void inintView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("出票单");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView recycler_view_for_cart = findViewById(R.id.recycler_view_for_cart);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_for_cart.setLayoutManager(manager);

        new Thread(){
            @Override
            public void run() {
                String url = String.format(urlFormat, GlobalParameters.userID);
                String json = RequestServer.RequestServer(url);

                Log.e("zhu:","_"+json);

            }
        }.start();
        CartRecyclerAdapter adapter = new CartRecyclerAdapter(this,list);
        recycler_view_for_cart.setAdapter(adapter);

    }


}
