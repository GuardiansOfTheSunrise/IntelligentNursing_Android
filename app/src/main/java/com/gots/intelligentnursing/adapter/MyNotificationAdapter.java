package com.gots.intelligentnursing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gots.intelligentnursing.entity.MyNotification;

import java.util.List;

/**
 * @author Accumulei
 * @date 2018/6/17.
 */
public class MyNotificationAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private List<MyNotification> mNotificationList;

    public MyNotificationAdapter(List<MyNotification> notificationList) {
        mNotificationList = notificationList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
