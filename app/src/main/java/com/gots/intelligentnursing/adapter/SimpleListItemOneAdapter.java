package com.gots.intelligentnursing.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/16
 * SimpleListItemOneAdapter适用于RecyclerView
 * 采用的布局为android.R.layout.simple_list_item_1
 * 布局仅有一个TextView
 * 对于简单的item显示可采用该布局与适配器
 */

public class SimpleListItemOneAdapter extends RecyclerView.Adapter<SimpleListItemOneAdapter.ViewHolder> {

    private List<String> mList;
    private OnItemClickedListener mOnItemClickedListener;

    public SimpleListItemOneAdapter(List<String> list) {
        mList = list;
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mOnItemClickedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.mView.setBackgroundColor(Color.parseColor("#F2F2F2"));
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
                holder.mView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            return false;
        });
        holder.mTextView.setText(mList.get(position));
        holder.mTextView.setOnClickListener( view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextView = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnItemClickedListener {
        /**
         * 子项被点击的回调接口
         * @param pos 被点击的子项位置
         */
        void onItemClicked(int pos);
    }
}
