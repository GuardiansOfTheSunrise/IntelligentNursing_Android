package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.User;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
        @GET("login")
        Flowable<ServerResponse<User>> login(@Query("username") String username, @Query("password") String password);
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
