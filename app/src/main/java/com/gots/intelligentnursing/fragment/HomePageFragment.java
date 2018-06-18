package com.gots.intelligentnursing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.business.NewsCBViewHolder;
import com.gots.intelligentnursing.business.PictureCBViewHolder;
import com.gots.intelligentnursing.entity.NewsInfo;
import com.gots.intelligentnursing.presenter.fragment.HomePagePresenter;
import com.gots.intelligentnursing.view.fragment.IHomePageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class HomePageFragment extends BaseFragment<HomePagePresenter> implements IHomePageView {

    private ConvenientBanner mPictureConvenientBanner;
    private ConvenientBanner mNewsConvenientBanner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_page_home, container, false);
        mPictureConvenientBanner = view.findViewById(R.id.convenient_banner_page_home_picture);
        mNewsConvenientBanner = view.findViewById(R.id.convenient_banner_page_home_news);
        mPictureConvenientBanner.setScrollDuration(1500);
        mPresenter.getPictureData();
        mPresenter.getNewsData();
        return view;
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetNewsSuccess(List<NewsInfo> newsInfoList) {
        List<String> titleList = new ArrayList<>(newsInfoList.size());
        for (NewsInfo newsInfo : newsInfoList) {
            titleList.add(newsInfo.getTitle());
        }
        mNewsConvenientBanner.setPages(NewsCBViewHolder::new, titleList);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPictureConvenientBanner.startTurning(4000);
        mNewsConvenientBanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPictureConvenientBanner.stopTurning();
        mNewsConvenientBanner.stopTurning();
    }

    @Override
    public void onGetPictureSuccess(List<Integer> pictureResList) {
        mPictureConvenientBanner.setPages(PictureCBViewHolder::new, pictureResList)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageTransformer(new ZoomInTransformer());
    }

    @Override
    protected HomePagePresenter createPresenter() {
        return new HomePagePresenter(this);
    }
}
