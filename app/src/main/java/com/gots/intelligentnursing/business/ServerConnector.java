package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.User;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 服务器连接器
 * @author zhqy
 * @date 2018/3/21
 */

public class ServerConnector {

    /**
     * 用户相关操作
     */
    public interface IUserOperate {
        /**
         * 用户登录接口
         * @param username 用户名
         * @param password 密码
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("login")
        Flowable<ServerResponse<User>> login(@Field("username") String username, @Field("password") String password);

        /**
         * 用户设置围栏接口
         * @param jsonBody json字符串构造的body
         * @return 包含服务器返回结果的被观察者对象
         */
        @Headers({"Content-type:application/json;charset=UTF-8"})
        @POST("fence/addfence")
        Flowable<ServerResponse> fenceDrawing(@Body RequestBody jsonBody);
    }

    public interface IDeviceOperate {
        /**
         * 用户绑定设备接口
         * @param username 用户名
         * @param id 设备id
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("bind")
        Flowable<ServerResponse> bind(@Field("username") String username, @Field("id") String id);

        /**
         * 用户解除设备绑定接口
         * @param username 用户名
         * @param id 设备id
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("unbind")
        Flowable<ServerResponse> unbind(@Field("username") String username, @Field("id") String id);
    }

    /**
     * 服务器地址
     */
    private static final String BASE_URL = "http://120.78.149.248:8080/";

    private static Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static Retrofit getRetrofitInstance() {
        return sRetrofit;
    }
}
