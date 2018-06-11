package com.gots.intelligentnursing.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.logined.DeviceManagementActivity;
import com.gots.intelligentnursing.entity.MineItem;
import com.gots.intelligentnursing.presenter.fragment.MinePagePresenter;
import com.gots.intelligentnursing.view.fragment.IMinePageView;

import java.net.Inet4Address;
import java.util.List;

/**
 * @author Accumulei
 * @date 2018/6/2.
 */
public class FragmentPagerMineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MineItem> mMineItemList;

    private static final int TYPE_MINE_LIST_HEADER = 0;
    private static final int TYPE_MINE_LIST_ITEM = 1;
    private static final int TYPE_MINE_LIST_SPACE = 2;

    private OnItemClickListener mOnItemClickListener;


    public FragmentPagerMineAdapter(List<MineItem> mineItemList) {
        mMineItemList = mineItemList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_MINE_LIST_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_list_header, parent, false);
                return new MineHeaderViewHolder(view);
            case TYPE_MINE_LIST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_list_item, parent, false);
                return new MineItemViewHolder(view);
            case TYPE_MINE_LIST_SPACE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_list_space, parent, false);
                return new MineSpaceViewHolder(view);
            default:
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MineItemViewHolder) {
            MineItem mineItem = mMineItemList.get(position);
            ((MineItemViewHolder) holder).mineListItem.setText(mineItem.getItem());
            ((MineItemViewHolder) holder).mineListImage.setImageResource(mineItem.getImageId());
            ((MineItemViewHolder) holder).mineItemView.setOnClickListener(view -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        } else if (holder instanceof MineHeaderViewHolder) {
            ((MineHeaderViewHolder) holder).mineImage.setImageResource(R.drawable.mine_list_header);
        }
    }

    @Override
    public int getItemCount() {
        return mMineItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MINE_LIST_HEADER;
        } else if (position % 4 == 0) {
            return TYPE_MINE_LIST_SPACE;
        } else {
            return TYPE_MINE_LIST_ITEM;
        }
    }

    private static class MineItemViewHolder extends RecyclerView.ViewHolder {

        View mineItemView;
        ImageView mineListImage;
        TextView mineListItem;

        MineItemViewHolder(View view) {
            super(view);
            mineItemView = view;
            mineListImage = view.findViewById(R.id.iv_page_mine_item);
            mineListItem = view.findViewById(R.id.tv_page_mine_item);
        }
    }

    private static class MineHeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView mineImage;

        MineHeaderViewHolder(View view) {
            super(view);
            mineImage = view.findViewById(R.id.iv_page_mine_header);
        }
    }

    private static class MineSpaceViewHolder extends RecyclerView.ViewHolder {

        MineSpaceViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemClickListener {
        /**
         * Item被点击的回调
         * @param position:Item在RecyclerView里的位置
         */
        void onItemClick(int position);
    }

}
