package com.gots.intelligentnursing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.presenter.activity.LoginPresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.activity.ILoginView;

/**
 * @author zhqy
 * @date 2018/5/10
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    private String mActionActivityName;

    private static final String HINT_ON_LOGINING = "登录中，请稍候...";

    private static final String KEY_FROM_ACTIVITY = "activity";

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    private void initEditText() {
        mUsernameEditText = findViewById(R.id.et_login_username);
        mPasswordEditText = findViewById(R.id.et_login_password);
    }

    private void initLoginButton() {
        Button loginButton = findViewById(R.id.bt_login_login);
        loginButton.setOnClickListener(v -> {
            showProgressBar();
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            mPresenter.onLoginButtonClicked(username, password);
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setProgressBarHint(HINT_ON_LOGINING);

        mActionActivityName = getIntent().getStringExtra(KEY_FROM_ACTIVITY);

        initEditText();
        initLoginButton();
    }

    @Override
    public void onLoginSuccess() {
        dismissProgressBar();
        Gson gson = new Gson();
        LogUtil.i("LoginActivity", gson.toJson(UserContainer.getUser()));
        finish();
        if (mActionActivityName != null) {
            Intent intent = new Intent();
            intent.setClassName(this, mActionActivityName);
            startActivity(intent);
        }
    }

    @Override
    public void onException(String msg) {
        dismissProgressBar();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected boolean isDisplayToolbar() {
        return false;
    }

    @Override
    protected boolean isDisplayProgressBar() {
        return true;
    }

    public static void actionStart(Context context, String toActivity) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(KEY_FROM_ACTIVITY, toActivity);
        context.startActivity(intent);
    }
}
