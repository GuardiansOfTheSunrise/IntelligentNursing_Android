package com.gots.intelligentnursing.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.adapter.MainFragmentPagerAdapter;

import com.gots.intelligentnursing.fragment.HomePageFragment;
import com.gots.intelligentnursing.fragment.MapPageFragment;
import com.gots.intelligentnursing.fragment.MinePageFragment;
import com.gots.intelligentnursing.fragment.NursingPageFragment;
import com.gots.intelligentnursing.presenter.activity.MainPresenter;
import com.gots.intelligentnursing.view.activity.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public class MainActivity extends BaseActivity<MainPresenter> implements IMainView {

    private static final String TOOLBAR_HOME = "智护";
    private static final String TOOLBAR_NURSING = "我的看护";
    private static final String TOOLBAR_MAP = "地图";
    private static final String TOOLBAR_MINE = "我的";

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    private ViewPager mViewPager;

    private List<Fragment> mFragmentList;

    private RadioButton mHomeRadioButton;
    private RadioButton mNursingRadioButton;
    private RadioButton mMapRadioButton;
    private RadioButton mMineRadioButton;

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HomePageFragment());
        mFragmentList.add(new NursingPageFragment());
        mFragmentList.add(new MapPageFragment());
        mFragmentList.add(new MinePageFragment());
    }

    private void initRadioView() {
        mHomeRadioButton = findViewById(R.id.rb_main_home);
        mNursingRadioButton = findViewById(R.id.rb_main_nursing);
        mMapRadioButton = findViewById(R.id.rb_main_map);
        mMineRadioButton = findViewById(R.id.rb_main_mine);

        RadioGroup radioGroup = findViewById(R.id.rg_main_tab_bar);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_main_home:
                    mViewPager.setCurrentItem(PAGE_ONE);
                    setToolbarTitle(TOOLBAR_HOME);
                    break;
                case R.id.rb_main_nursing:
                    mViewPager.setCurrentItem(PAGE_TWO);
                    setToolbarTitle(TOOLBAR_NURSING);
                    break;
                case R.id.rb_main_map:
                    mViewPager.setCurrentItem(PAGE_THREE);
                    setToolbarTitle(TOOLBAR_MAP);
                    break;
                case R.id.rb_main_mine:
                    mViewPager.setCurrentItem(PAGE_FOUR);
                    setToolbarTitle(TOOLBAR_MINE);
                    break;
                default:
            }
        });
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.vp_main);

        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    switch (mViewPager.getCurrentItem()) {
                        case PAGE_ONE:
                            mHomeRadioButton.setChecked(true);
                            break;
                        case PAGE_TWO:
                            mNursingRadioButton.setChecked(true);
                            break;
                        case PAGE_THREE:
                            mMapRadioButton.setChecked(true);
                            break;
                        case PAGE_FOUR:
                            mMineRadioButton.setChecked(true);
                            break;
                        default:
                    }
                }
            }
        });
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarTitle(TOOLBAR_HOME);
        initFragment();
        initRadioView();
        initViewPager();
        mHomeRadioButton.setChecked(true);
    }

    @Override
    protected boolean isDisplayBackButton() {
        return false;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }


}
