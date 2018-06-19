package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.adapter.FragmentPagerMineAdapter;
import com.gots.intelligentnursing.entity.MineItem;
import com.gots.intelligentnursing.presenter.fragment.MinePagePresenter;
import com.gots.intelligentnursing.view.fragment.IMinePageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Accumulei
 * @date 2018/4/17.
 */
public class MinePageFragment extends BaseFragment<MinePagePresenter> implements IMinePageView {

    private static final String HINT_ON_LOGOUT_SUCCESS = "您已退出登录";
    private List<MineItem> mMineItemList = new ArrayList<>();

    private void initMineItem() {
        MineItem blank = new MineItem();
        mMineItemList.add(blank);
        for (int i = 0; i < 2; i++) {
            MineItem deviceManagement = new MineItem(R.drawable.ic_page_mine_item_manage, "设备管理");
            mMineItemList.add(deviceManagement);
            MineItem logout = new MineItem(R.drawable.ic_page_mine_item_quit, "退出登录");
            mMineItemList.add(logout);
            MineItem checkUpdate = new MineItem(R.drawable.ic_page_mine_item_update,"检查更新");
            mMineItemList.add(checkUpdate);
            mMineItemList.add(blank);
        }
        MineItem about = new MineItem(R.drawable.ic_page_mine_item_about,"关于");
        mMineItemList.add(about);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_mine, container, false);
        initMineItem();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_page_mine);
        //RecyclerView.ItemDecoration
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FragmentPagerMineAdapter adapter = new FragmentPagerMineAdapter(mMineItemList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> mPresenter.onItemClicked(position));
        return view;
    }

    @Override
    public void onLogoutSuccess() {
        Toast.makeText(getActivity(), HINT_ON_LOGOUT_SUCCESS, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected MinePagePresenter createPresenter() {
        return new MinePagePresenter(this);
    }
}
