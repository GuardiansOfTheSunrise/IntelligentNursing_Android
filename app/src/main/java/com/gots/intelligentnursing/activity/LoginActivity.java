package com.gots.intelligentnursing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.customview.RipplingFilletedButton;
import com.gots.intelligentnursing.presenter.activity.LoginPresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.activity.ILoginView;
/**
 * @author zhqy
 * @date 2018/5/10
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    private static final String TAG = "LoginActivity";

    private String mActionActivityName;

    public static final int CODE_TPL_QQ = 0;
    public static final int CODE_TPL_SINA = 1;

    private static final String HINT_ON_LOGIN_SUCCESS = "，欢迎您";
    private static final String HINT_ON_LOGINING = "登录中，请稍候...";

    private static final String KEY_FROM_ACTIVITY = "activity";

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    private void initEditText() {
        mUsernameEditText = findViewById(R.id.et_login_username);
        mPasswordEditText = findViewById(R.id.et_login_password);
    }

    private void initLoginButton() {
        RipplingFilletedButton loginButton = findViewById(R.id.bt_login_submit);
        loginButton.setOnClickListener(v -> {
            showProgressBar();
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            mPresenter.onLoginButtonClicked(username, password);
        });
    }

    private void initTextView() {
        TextView registerTextView = findViewById(R.id.tv_login_register);
        registerTextView.setOnClickListener(v -> RegisterActivity.actionStart(this));
    }

    private void initThirdPartyLoginImageView() {
        ImageView qqLoginImageView = findViewById(R.id.iv_login_tpl_qq);
        qqLoginImageView.setTag(CODE_TPL_QQ);
        View.OnClickListener onTplImageViewClickListener = v -> mPresenter.onTplImageViewClicked((Integer) v.getTag());
        qqLoginImageView.setOnClickListener(onTplImageViewClickListener);

        ImageView sinaLoginImageView = findViewById(R.id.iv_login_tpl_sina);
        sinaLoginImageView.setTag(CODE_TPL_SINA);
        sinaLoginImageView.setOnClickListener(onTplImageViewClickListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setProgressBarHint(HINT_ON_LOGINING);

        mActionActivityName = getIntent().getStringExtra(KEY_FROM_ACTIVITY);

        initEditText();
        initLoginButton();
        initTextView();
        initThirdPartyLoginImageView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess(String username) {
        dismissProgressBar();
        LogUtil.i(TAG, UserContainer.getUser().toString());
        Toast.makeText(this, username + HINT_ON_LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
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
    public void onLogging() {
        showProgressBar();
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
