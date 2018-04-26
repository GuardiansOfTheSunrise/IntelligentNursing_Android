package com.gots.intelligentnursing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.GeographyFencePresenter;
import com.gots.intelligentnursing.view.IGeographyFenceView;

public class GeographyFenceActivity extends BaseActivity<GeographyFencePresenter> implements IGeographyFenceView {

    private static final String TOOLBAR_TITLE = "围栏设置";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geography_fence);
        setToolbarTitle(TOOLBAR_TITLE);
    }

    @Override
    protected GeographyFencePresenter createPresenter() {
        return new GeographyFencePresenter(this);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GeographyFenceActivity.class);
        context.startActivity(intent);
    }
}
