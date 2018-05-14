package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.presenter.fragment.HomePagePresenter;
import com.gots.intelligentnursing.view.fragment.IHomePageView;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class HomePageFragment extends BaseFragment<HomePagePresenter> implements IHomePageView {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_page_home, container, false);
        return view;
    }

    @Override
    protected HomePagePresenter createPresenter() {
        return new HomePagePresenter(this);
    }
}
