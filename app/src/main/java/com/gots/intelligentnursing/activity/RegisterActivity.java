package com.gots.intelligentnursing.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.customview.RipplingFilletedButton;
import com.gots.intelligentnursing.presenter.activity.RegisterPresenter;
import com.gots.intelligentnursing.view.activity.IRegisterView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhqy
 * @date 2018/6/9
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegisterView {

    private static final String TOOLBAR_TITLE = "注册";
    private static final String TEXT_VERIFY_BUTTON = "获取验证码";
    private static final String HINT_ON_GET_VERIFY_SUCCESS = "验证码已发送，15分钟内有效";
    private static final String HINT_ON_REGISTER_SUCCESS = "注册成功";

    private RipplingFilletedButton mVerifyButton;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordText;
    private EditText mPhoneEditText;
    private EditText mVerifyEditText;

    private void initEditText() {
        mUsernameEditText = findViewById(R.id.et_register_username);
        mPasswordEditText = findViewById(R.id.et_register_password);
        mConfirmPasswordText = findViewById(R.id.et_register_confirm_password);
        mPhoneEditText = findViewById(R.id.et_register_phone);
        mVerifyEditText = findViewById(R.id.et_register_verify);
    }

    private void initButton() {
        mVerifyButton = findViewById(R.id.bt_register_verify);
        mVerifyButton.setOnClickListener(v -> {
            mPresenter.getVerify(mUsernameEditText.getText().toString(),
                    mPhoneEditText.getText().toString());
        });

        RipplingFilletedButton registerButton = findViewById(R.id.bt_register_register);
        registerButton.setOnClickListener(v -> {
            Map<String, String> input = new HashMap<>(5);
            input.put(RegisterPresenter.KEY_USERNAME, mUsernameEditText.getText().toString());
            input.put(RegisterPresenter.KEY_PASSWORD, mPasswordEditText.getText().toString());
            input.put(RegisterPresenter.KEY_CONFIRM, mConfirmPasswordText.getText().toString());
            input.put(RegisterPresenter.KEY_PHONE, mPhoneEditText.getText().toString());
            input.put(RegisterPresenter.KEY_VERIFY, mVerifyEditText.getText().toString());
            mPresenter.register(input);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setToolbarTitle(TOOLBAR_TITLE);
        initEditText();
        initButton();
        mPresenter.initVerifyTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.recycler();
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGettingVerifySuccess() {
        Toast.makeText(this, HINT_ON_GET_VERIFY_SUCCESS, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(this, HINT_ON_REGISTER_SUCCESS, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void updateVerifyButtonTime(int second) {
        if (second != 0) {
            mVerifyButton.setEnabled(false);
            mVerifyButton.setButtonText(String.valueOf(second));
        } else {
            mVerifyButton.setEnabled(true);
            mVerifyButton.setButtonText(TEXT_VERIFY_BUTTON);
        }
    }
}
