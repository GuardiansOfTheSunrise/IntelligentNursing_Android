package com.gots.intelligentnursing.activity.logined;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.BaseActivity;
import com.gots.intelligentnursing.adapter.MyNotificationAdapter;
import com.gots.intelligentnursing.business.EventPoster;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.entity.MyNotification;
import com.gots.intelligentnursing.presenter.activity.MyNotificationPresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.activity.IActivityView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MyNotificationActivity extends BaseActivity<MyNotificationPresenter> implements IActivityView {

    private List<MyNotification> mNotificationList = new ArrayList<>();
    private static final String TAG = "MyNotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_my_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyNotificationAdapter adapter = new MyNotificationAdapter(mNotificationList);
        recyclerView.setAdapter(adapter);
    }



    @Override
    protected MyNotificationPresenter createPresenter() {
        return new MyNotificationPresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent (DataEvent event) {
        String action = event.getAction();
        if (action.equals(EventPoster.ACTION_UPUSH_GET_NOTIFICATION)) {
            LogUtil.i(TAG, "getNotification");
        }
    }

}
