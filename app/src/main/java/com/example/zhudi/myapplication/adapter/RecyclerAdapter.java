package com.example.zhudi.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhudi.myapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ww on 2017/12/28.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.mViewHolder> {

    private List<String> mList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemLongClick(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void removeDate(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewHolder holder = new mViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_tv_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.mViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));
        //对点击事件进行监听，并回调监听
        if (onItemClickListener != null){
            holder.tv.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.tv,position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size():0;
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public mViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.tv_item);
        }
    }

    public void onrefresh(List<String> list){
        this.mList = list;
        notifyDataSetChanged();

    }
}
