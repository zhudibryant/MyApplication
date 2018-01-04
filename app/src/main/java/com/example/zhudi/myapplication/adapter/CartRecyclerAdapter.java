package com.example.zhudi.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhudi.myapplication.R;

import java.util.List;

/**
 * Created by ww on 2017/12/28.
 */

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.mViewHolder> {

    private List<String> mList;
    private Context context;




    public CartRecyclerAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
    }



    @Override
    public CartRecyclerAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewHolder holder = new mViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view, parent, false));
        Log.e("chen","22222");
        return holder;
    }

    @Override
    public void onBindViewHolder(final CartRecyclerAdapter.mViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size():0;
    }

    class mViewHolder extends RecyclerView.ViewHolder {


        public mViewHolder(View view) {
            super(view);

        }
    }

    public void onrefresh(List<String> list){
        this.mList = list;
        notifyDataSetChanged();

    }
}
