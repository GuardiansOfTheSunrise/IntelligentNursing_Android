package com.gots.intelligentnursing.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.activity.BaseActivityPresenter;
import com.gots.intelligentnursing.presenter.activity.MessageCardPresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.activity.IActivityView;

public class MessgeCardActivity extends BaseActivity<BaseActivityPresenter> implements IActivityView {

    private final static String TAG = "MessageCardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messge_card);
        setToolbarTitle("老人信息卡");
        initWebView();
    }

    private void initWebView() {
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("message_card_uri"));
        WebView webView = findViewById(R.id.web_view_message_card);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        LogUtil.i(TAG, "uri.toString(): " + uri.toString());
        LogUtil.i(TAG, "String.valueOf(uri): " + String.valueOf(uri));
        webView.loadUrl(String.valueOf(uri));
    }

    @Override
    protected BaseActivityPresenter createPresenter() {
        return new MessageCardPresenter(this);
    }
}

