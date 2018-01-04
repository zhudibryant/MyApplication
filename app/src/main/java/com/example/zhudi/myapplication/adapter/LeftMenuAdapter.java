package com.example.zhudi.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhudi.myapplication.R;
import com.example.zhudi.myapplication.utils.Constant;

import java.util.List;

/**
 * Created by ww on 2017/12/15.
 */

public class LeftMenuAdapter extends BaseAdapter{
    private Context context;
    private String[] planetTitles;
    public LeftMenuAdapter(Context context, String[] planetTitles){
        this.context = context;
        this.planetTitles = planetTitles;
    }
    @Override
    public int getCount() {
        return planetTitles.length;
    }

    @Override
    public Object getItem(int i) {
        return planetTitles[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder =null;
        if(view==null){
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.drawer_list_item,null);
            holder.tv_item = view.findViewById(R.id.tv_item);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Log.e("chen","pos:"+i+"title:"+planetTitles[i]);
        holder.tv_item.setText(planetTitles[i]);
        return view;
    }
    static class ViewHolder{
        private TextView tv_item;
    }
}
