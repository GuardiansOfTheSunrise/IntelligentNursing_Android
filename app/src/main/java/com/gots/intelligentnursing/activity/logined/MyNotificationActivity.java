package com.gots.intelligentnursing.activity.logined;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.BaseActivity;
import com.gots.intelligentnursing.presenter.activity.MyNotificationPresenter;
import com.gots.intelligentnursing.view.activity.IActivityView;

public class MyNotificationActivity extends BaseActivity<MyNotificationPresenter> implements IActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);
    }

    @Override
    protected MyNotificationPresenter createPresenter() {
        return new MyNotificationPresenter(this);
    }

    @Override
    public void onException(String msg) {

    }
}
