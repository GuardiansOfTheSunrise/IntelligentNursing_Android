package com.gots.intelligentnursing.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;

    public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
