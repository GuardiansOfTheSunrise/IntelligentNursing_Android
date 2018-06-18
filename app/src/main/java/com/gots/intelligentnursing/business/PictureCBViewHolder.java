package com.gots.intelligentnursing.business;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * @author zhqy
 * @date 2018/6/17
 */

public class PictureCBViewHolder implements Holder<Integer> {

    private ImageView mImageView;

    @Override
    public View createView(Context context) {
        mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        mImageView.setImageResource(data);
    }
}
