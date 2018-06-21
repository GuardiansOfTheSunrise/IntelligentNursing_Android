package com.gots.intelligentnursing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.entity.NotificationData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Accumulei
 * @date 2018/6/17.
 */
public class MyNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationData> mNotificationList;

    private static final int TYPE_NOTIFICATION_LIST_ITEM = 0;
    private static final int TYPE_NOTIFICATION_LIST_SEPARATOR = 1;

    public MyNotificationAdapter(List<NotificationData> notificationList) {
        mNotificationList = notificationList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_NOTIFICATION_LIST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_notification_list, parent, false);
                return new NotificationItemViewHolder(view);
            case TYPE_NOTIFICATION_LIST_SEPARATOR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_notification_separator,parent,false);
                return new NotificationSeparatorViewHolder(view);
                default:
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationItemViewHolder) {
            if (position < 10) {
                NotificationData notificationData = mNotificationList.get(position);
                ((NotificationItemViewHolder) holder).notificationListImage.setImageResource(notificationData.getImageId());
                ((NotificationItemViewHolder) holder).notificationListEvent.setText(notificationData.getEvent());
                ((NotificationItemViewHolder) holder).notificationListDate.setText(notificationData.getDate());
            } else {
                NotificationData notificationData = mNotificationList.get(position);
                ((NotificationItemViewHolder) holder).notificationListImage.setImageResource(R.drawable.ic_page_mine_item_about);
                ((NotificationItemViewHolder) holder).notificationListEvent.setText(notificationData.getEvent());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 EEEE HH:mm", Locale.CHINA);
                Date date = new Date(System.currentTimeMillis());
                ((NotificationItemViewHolder) holder).notificationListDate.setText(simpleDateFormat.format(date));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2 == 0) {
            return TYPE_NOTIFICATION_LIST_ITEM;
        } else {
            return TYPE_NOTIFICATION_LIST_SEPARATOR;
        }
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size()-1;
    }

    public void addItem(int positon) {
        //mNotificationList.add();
        notifyItemInserted(positon);
    }

    private static class NotificationItemViewHolder extends RecyclerView.ViewHolder {

        ImageView notificationListImage;
        TextView notificationListDate;
        TextView notificationListEvent;
        View notificationItemView;

        NotificationItemViewHolder(View itemView) {
            super(itemView);
            notificationItemView = itemView;
            notificationListImage = itemView.findViewById(R.id.iv_my_notification);
            notificationListDate = itemView.findViewById(R.id.tv_my_notification_date);
            notificationListEvent = itemView.findViewById(R.id.tv_my_notification_event);
        }
    }

    private static class NotificationSeparatorViewHolder extends RecyclerView.ViewHolder {
        NotificationSeparatorViewHolder(View itemView) {
            super(itemView);
        }
    }

}
