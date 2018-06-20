package com.gots.intelligentnursing.view.fragment;

import com.gots.intelligentnursing.entity.NewsInfo;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public interface IHomePageView extends IFragmentView {

    /**
     * 获取新闻数据成功回调
     *
     * @param newsInfoList 新闻数据列表
     */
    void onGetNewsSuccess(List<NewsInfo> newsInfoList);

    /**
     * 获取图片数据成功回调
     *
     * @param pictureResList 图片数据列表
     */
    void onGetPictureSuccess(List<Integer> pictureResList);
}
