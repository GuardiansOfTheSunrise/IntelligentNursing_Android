package com.gots.intelligentnursing.activity.logined;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.BaseActivity;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.customview.RipplingFilletedButton;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.presenter.activity.ElderInfoPresenter;
import com.gots.intelligentnursing.view.activity.IElderInfoView;

/**
 * @author zhqy
 * @date 2018/6/19
 */

public class ElderInfoActivity extends BaseActivity<ElderInfoPresenter> implements IElderInfoView {

    private static final String TOOLBAR_TITLE = "老人信息设置";
    private static final String HINT_ON_SUCCESS = "保存成功";

    private EditText mAgeEditText;
    private EditText mHeightEditText;
    private EditText mWeightEditText;
    private EditText mAddressEditText;
    private EditText mPhoneEditText;
    private EditText mRemarksEditText;

    private void initEditText() {
        mAgeEditText = findViewById(R.id.et_elder_info_age);
        mHeightEditText = findViewById(R.id.et_elder_info_height);
        mWeightEditText = findViewById(R.id.et_elder_info_weight);
        mAddressEditText = findViewById(R.id.et_elder_info_address);
        mPhoneEditText = findViewById(R.id.et_elder_info_phone);
        mRemarksEditText = findViewById(R.id.et_elder_info_remarks);

        UserInfo userInfo = UserContainer.getUser().getUserInfo();
        mAgeEditText.setText(String.valueOf(userInfo.getAge()));
        if (userInfo.getHeight() != null) {
            mHeightEditText.setText(userInfo.getHeight());
        }
        if (userInfo.getWeight() != null) {
            mWeightEditText.setText(userInfo.getWeight());
        }
        if (userInfo.getAddress() != null) {
            mAddressEditText.setText(userInfo.getAddress());
        }
        if (userInfo.getTelephone() != null) {
            mPhoneEditText.setText(userInfo.getTelephone());
        }
        if (userInfo.getRemarks() != null) {
            mRemarksEditText.setText(userInfo.getRemarks());
        }
    }

    private void initButton() {
        RipplingFilletedButton button = findViewById(R.id.bt_elder_info_save);
        button.setOnClickListener(v ->
            mPresenter.onSaveButtonClick(mAgeEditText.getText().toString(), mHeightEditText.getText().toString(),
                    mWeightEditText.getText().toString(), mAddressEditText.getText().toString(),
                    mPhoneEditText.getText().toString(), mRemarksEditText.getText().toString()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_info);
        setToolbarTitle(TOOLBAR_TITLE);
        initEditText();
        initButton();
    }

    @Override
    public void onSaveSuccess() {
        Toast.makeText(this, HINT_ON_SUCCESS, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected ElderInfoPresenter createPresenter() {
        return new ElderInfoPresenter(this);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ElderInfoActivity.class);
        context.startActivity(intent);
    }
}
