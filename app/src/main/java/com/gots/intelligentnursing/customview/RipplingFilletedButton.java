package com.gots.intelligentnursing.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;

import com.andexert.library.RippleView;
import com.gots.intelligentnursing.R;

/**
 * @author zhqy
 * @date 2018/5/27
 */

public class RipplingFilletedButton extends RippleView {

    private Button mButton;

    private void initButton(Context context) {
        mButton = new Button(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mButton.setLayoutParams(lp);
        mButton.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_button_filleted_corner));
        addView(mButton);
    }

    private void initButtonFromAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RipplingFilletedButton);
        String text = ta.getString(R.styleable.RipplingFilletedButton_buttonText);
        mButton.setText(text);
        float textSize = ta.getDimensionPixelSize(R.styleable.RipplingFilletedButton_buttonTextSize, 12);
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int textColor = ta.getColor(R.styleable.RipplingFilletedButton_buttonTextColor, Color.BLACK);
        mButton.setTextColor(textColor);
        int backgroundColor = ta.getColor(R.styleable.RipplingFilletedButton_buttonBackgroundColor, Color.GRAY);
        GradientDrawable drawable = (GradientDrawable) mButton.getBackground();
        drawable.setColor(backgroundColor);
        ta.recycle();
    }

    public RipplingFilletedButton(Context context) {
        super(context);
        initButton(context);
    }

    public RipplingFilletedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton(context);
        initButtonFromAttrs(context, attrs);
    }

    public RipplingFilletedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initButton(context);
        initButtonFromAttrs(context, attrs);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mButton.setOnClickListener(l);
    }

    @Override
    public void setTag(Object tag) {
        mButton.setTag(tag);
    }

    @Override
    public Object getTag() {
        return mButton.getTag();
    }

    public void setButtonText(String text) {
        mButton.setText(text);
    }

    public void setButtonTextSize(float size) {
        mButton.setTextSize(size);
    }

    public void setButtonTextSize(int unit, float size) {
        mButton.setTextSize(unit, size);
    }

    public void setButtonTextColor(int color) {
        mButton.setTextColor(color);
    }

    public void setButtonBackgroundColor(int color) {
        GradientDrawable drawable = (GradientDrawable) mButton.getBackground();
        drawable.setColor(color);
    }
}
