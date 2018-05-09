package com.gots.intelligentnursing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.activity.LoginPresenter;
import com.gots.intelligentnursing.view.activity.ILoginView;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected boolean isDisplayToolbar() {
        return false;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
