package com.gots.intelligentnursing.business;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * @author zhqy
 * @date 2018/6/17
 */

public class NewsCBViewHolder implements Holder<String> {

    private TextView mTextView;

    @Override
    public View createView(Context context) {
        mTextView = new TextView(context);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setGravity(Gravity.CENTER_VERTICAL);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        return mTextView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        mTextView.setText(data);
    }
}
