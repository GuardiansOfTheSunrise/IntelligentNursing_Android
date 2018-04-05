package com.gots.intelligentnursing.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gots.intelligentnursing.R;

/**
 * @author zhqy
 * @date 2018/4/3
 */

public class TitleCenterToolbar extends Toolbar {

    private TextView mTextView;

    public TitleCenterToolbar(Context context) {
        super(context);
        createTextView(context);
        setTitle("");
    }

    public TitleCenterToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createTextView(context);
        initTextViewFromAttrs(context, attrs);
        setTitle("");
    }

    public TitleCenterToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createTextView(context);
        initTextViewFromAttrs(context, attrs);
        setTitle("");
    }

    private void createTextView(Context context){
        mTextView = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        mTextView.setLayoutParams(layoutParams);
        addView(mTextView);
    }

    private void initTextViewFromAttrs(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleCenterToolbar);
        String text = ta.getString(R.styleable.TitleCenterToolbar_text);
        mTextView.setText(text);
        float textSize = ta.getDimensionPixelSize(R.styleable.TitleCenterToolbar_textSize, 12);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int textColor = ta.getColor(R.styleable.TitleCenterToolbar_textColor, Color.BLACK);
        mTextView.setTextColor(textColor);
        ta.recycle();
    }

    @Override
    public void setTitle(int resId) {
        super.setTitle("");
        mTextView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
        mTextView.setText(title);
    }
}
