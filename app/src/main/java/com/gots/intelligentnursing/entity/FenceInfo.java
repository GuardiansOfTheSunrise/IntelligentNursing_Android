package com.gots.intelligentnursing.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/5/11
 */

public class FenceInfo {

    @SerializedName("fencePoints")
    private List<LocationData> mFencePointDataList;

    public List<LocationData> getFencePointDataList() {
        return mFencePointDataList;
    }

    public void setFencePointDataList(List<LocationData> fencePointDataList) {
        mFencePointDataList = fencePointDataList;
    }
}
