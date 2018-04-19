package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.MapPagePresenter;
import com.gots.intelligentnursing.view.IMapPageView;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class MapPageFragment extends BaseFragment<MapPagePresenter> implements IMapPageView {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_map, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected MapPagePresenter createPresenter() {
        return new MapPagePresenter(this);
    }
}
