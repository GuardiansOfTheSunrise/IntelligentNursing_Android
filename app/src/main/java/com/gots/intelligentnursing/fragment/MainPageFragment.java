package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.MainPagePresenter;
import com.gots.intelligentnursing.view.IMainPageView;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class MainPageFragment extends BaseFragment<MainPagePresenter> implements IMainPageView {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_page_main, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected MainPagePresenter createPresenter() {
        return new MainPagePresenter(this);
    }
}
