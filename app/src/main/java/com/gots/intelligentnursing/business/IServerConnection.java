package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author zhqy
 * @date 2018/5/9
 */

public interface IServerConnection {
    /**
     * 用户相关操作
     */
    interface IUserOperate {
        /**
         * 用户登录接口
         * @param username 用户名
         * @param password 密码
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("auth2")
        Flowable<ServerResponse<String>> login(@Field("uname") String username, @Field("pwd") String password);

        /**
         * 用户登录后获取用户信息
         * @param token 登录返回的token
         * @return 包含服务器返回结果的被观察者对象
         */
        @GET("user")
        Flowable<ServerResponse<UserInfo>> getUserInfo(@Header("Authorization") String token);

        /**
         * 用户设置围栏接口
         * @param token 登录返回的token
         * @param jsonBody json字符串构造的body
         * @return 包含服务器返回结果的被观察者对象
         */
        @Headers({"Content-type:application/json;charset=UTF-8"})
        @POST("fence/addfence")
        Flowable<ServerResponse> fenceDrawing(@Header("Authorization") String token, @Body RequestBody jsonBody);
    }

    /**
     * 设备相关操作
     */
    interface IDeviceOperate {
        /**
         * 用户绑定设备接口
         * @param token 登录返回的token
         * @param userId 用户id
         * @param equipId 设备id
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("equipment/bind")
        Flowable<ServerResponse<String>> bind(@Header("Authorization") String token, @Field("uid") int userId, @Field("eid") String equipId);

        /**
         * 用户解除设备绑定接口
         * @param token 登录返回的token
         * @param userId 用户id
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("unbind")
        Flowable<ServerResponse> unbind(@Header("Authorization") String token, @Field("uid") int userId);

    }
}
