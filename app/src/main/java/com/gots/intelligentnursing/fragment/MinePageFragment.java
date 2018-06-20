package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.adapter.FragmentPagerMineAdapter;
import com.gots.intelligentnursing.presenter.fragment.MinePagePresenter;
import com.gots.intelligentnursing.view.fragment.IMinePageView;

/**
 * @author Accumulei
 * @date 2018/4/17.
 */
public class MinePageFragment extends BaseFragment<MinePagePresenter> implements IMinePageView {

    private static final String HINT_ON_LOGOUT_SUCCESS = "您已退出登录";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_mine, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_page_mine);
        //RecyclerView.ItemDecoration
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FragmentPagerMineAdapter adapter = new FragmentPagerMineAdapter(mPresenter.getDataList());
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
