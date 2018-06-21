package com.gots.intelligentnursing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.gots.intelligentnursing.R;

import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.activity.PublicFenceActivity;
import com.gots.intelligentnursing.activity.WebActivity;
import com.gots.intelligentnursing.activity.logined.MyNotificationActivity;
import com.gots.intelligentnursing.business.EventPoster;
import com.gots.intelligentnursing.business.NewsCBViewHolder;
import com.gots.intelligentnursing.business.PictureCBViewHolder;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.entity.NewsInfo;
import com.gots.intelligentnursing.presenter.fragment.HomePagePresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.fragment.IHomePageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class HomePageFragment extends BaseFragment<HomePagePresenter> implements IHomePageView {

    private List<NewsInfo> mNewsInfoList;

    private ConvenientBanner mPictureConvenientBanner;
    private ConvenientBanner mNewsConvenientBanner;

    private ImageView mImageViewMyNursing;

    private static final String TAG = "HomePageFragment";
    private static final int ELDER_STATE_NORMAL = 0;
    private static final int ELDER_STATE_ABNORMAL= 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_page_home, container, false);
        EventBus.getDefault().register(this);
        mImageViewMyNursing = view.findViewById(R.id.iv_page_home_my_nursing);
        mImageViewMyNursing.setOnClickListener(v -> {
            if (UserContainer.getUser().getUserInfo() == null) {
                LoginActivity.actionStart(getContext(),null);
            } else {
                if (HomePagePresenter.elderState == ELDER_STATE_ABNORMAL) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("请注意！");
                    builder.setMessage("您的老人状态异常，请及时联系确认。如已确认老人安全，请点击”危险解除“");
                    builder.setCancelable(false);
                    builder.setNegativeButton("明白", null);
                    builder.setPositiveButton("危险解除", (dialogInterface, i) -> {
                        mImageViewMyNursing.setImageResource(R.drawable.bt_page_home_my_nursing_normal);
                        HomePagePresenter.elderState = ELDER_STATE_NORMAL;
                    });
                    builder.show();
                } else {
                    Toast.makeText(getActivity(), "老人状态一切正常，请您放心", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView imageViewMyNotification = view.findViewById(R.id.iv_page_home_my_notification);
        ImageView imageViewPublicFence = view.findViewById(R.id.iv_page_home_public_fence);
        imageViewPublicFence.setOnClickListener(v -> PublicFenceActivity.actionStart(getContext()));
        imageViewMyNotification.setOnClickListener(v -> MyNotificationActivity.actionStart(getContext()));

        mPictureConvenientBanner = view.findViewById(R.id.convenient_banner_page_home_picture);
        mNewsConvenientBanner = view.findViewById(R.id.convenient_banner_page_home_news);
        mPictureConvenientBanner.setScrollDuration(1500);
        mPresenter.getPictureData();
        mPresenter.getNewsData();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent (DataEvent event) {
        String action = event.getAction();
        if (action.equals(EventPoster.ACTION_UPUSH_GET_NOTIFICATION)) {
            mImageViewMyNursing.setImageResource(R.drawable.bt_page_home_my_nursing_abnormal);
            HomePagePresenter.elderState = ELDER_STATE_ABNORMAL;
            LogUtil.i(TAG,"EventBus Received");
        }
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetNewsSuccess(List<NewsInfo> newsInfoList) {
        mNewsInfoList = newsInfoList;
        List<String> titleList = new ArrayList<>(newsInfoList.size());
        for (NewsInfo newsInfo : newsInfoList) {
            titleList.add(newsInfo.getTitle());
        }
        mNewsConvenientBanner.setPages(NewsCBViewHolder::new, titleList)
                .setOnItemClickListener(position ->
                    WebActivity.actionStart(getActivity(), mNewsInfoList.get(position).getUrl()));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
