package com.gots.intelligentnursing.business;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.gots.intelligentnursing.business.IServerConnection.*;
import com.gots.intelligentnursing.tools.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author zhqy
 * @date 2018/5/9
 */

public class RetrofitHelper {

    private static final String TAG = "RetrofitHelper";

    private static final String BASE_URL = "http://120.78.149.248:8080/";

    private IUserOperate mUserOperate;
    private IDeviceOperate mDeviceOperate;
    private IThirdPartyOperate mThirdPartyOperate;

    private OkHttpClient initOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            try {
                LogUtil.i(TAG, URLDecoder.decode(message, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                LogUtil.i(TAG, message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    private RetrofitHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(initOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mUserOperate = retrofit.create(IUserOperate.class);
        mDeviceOperate = retrofit.create(IDeviceOperate.class);
        mThirdPartyOperate = retrofit.create(IThirdPartyOperate.class);
    }

    public IUserOperate user() {
        return mUserOperate;
    }

    public IDeviceOperate device() {
        return mDeviceOperate;
    }

    public IThirdPartyOperate thirdParty() {
        return mThirdPartyOperate;
    }

    public static RetrofitHelper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }
}
