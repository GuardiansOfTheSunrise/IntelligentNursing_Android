package com.gots.intelligentnursing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.activity.BaseActivityPresenter;
import com.gots.intelligentnursing.presenter.activity.WebPresenter;
import com.gots.intelligentnursing.view.activity.IWebView;

/**
 * @author zhqy
 * @date 2018/6/18
 */

public class WebActivity extends BaseActivity<BaseActivityPresenter> implements IWebView {

    private static final String KEY_URL = "url";
    private static final String HINT_ON_LOADING = "网页加载中，请稍候...";
    private static final int PROGRESS_FINISH = 100;
    private static final int MAX_TITLE_LENGTH = 10;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setProgressBarHint(HINT_ON_LOADING);
        initWebView();
    }

    private void initWebView() {
        mWebView = findViewById(R.id.web_view_message_card);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == PROGRESS_FINISH) {
                    dismissProgressBar();
                } else {
                    showProgressBar();
                    setProgressBarProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (title.length() >= MAX_TITLE_LENGTH) {
                    title = title.substring(0, MAX_TITLE_LENGTH) + "...";
                }
                setToolbarTitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(getIntent().getStringExtra(KEY_URL));
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected boolean isDisplayProgressBar() {
        return true;
    }

    @Override
    protected boolean isDisplayToolbar() {
        return true;
    }

    @Override
    protected boolean isDisplayBackButton() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected BaseActivityPresenter createPresenter() {
        return new WebPresenter(this);
    }

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }
}

