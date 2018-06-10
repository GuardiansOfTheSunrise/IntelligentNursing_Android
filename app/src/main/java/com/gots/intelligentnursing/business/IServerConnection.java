package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.SinaUserInfo;
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
import retrofit2.http.Query;

/**
 * @author zhqy
 * @date 2018/5/9
 */

public interface IServerConnection {

    String TOKEN_HEADER_KEY = "Authorization";
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
         * 获取验证码接口
         * @param username 用户名
         * @param phone 手机号
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("getCode")
        Flowable<ServerResponse> getVerify(@Field("uname") String username, @Field("phone") String phone);

        /**
         * 用户注册接口
         * @param username 用户名
         * @param password 密码
         * @param phone 手机号
         * @param verify 验证码
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("user/reg")
        Flowable<ServerResponse> register(@Field("uname") String username, @Field("pwd") String password,
                                          @Field("tel") String phone, @Field("code") String verify);

        /**
         * 用户登录后获取用户信息
         * @param token 登录返回的token
         * @return 包含服务器返回结果的被观察者对象
         */
        @GET("user")
        Flowable<ServerResponse<UserInfo>> getUserInfo(@Header(TOKEN_HEADER_KEY) String token);

        /**
         * 用户设置围栏接口
         * @param token 登录返回的token
         * @param jsonBody json字符串构造的body
         * @return 包含服务器返回结果的被观察者对象
         */
        @Headers({"Content-type:application/json;charset=UTF-8"})
        @POST("fence/addfence")
        Flowable<ServerResponse> fenceDrawing(@Header(TOKEN_HEADER_KEY) String token, @Body RequestBody jsonBody);
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
        Flowable<ServerResponse<String>> bind(@Header(TOKEN_HEADER_KEY) String token, @Field("uid") int userId, @Field("eid") String equipId);

        // TODO: 2018/5/29 接口尚未与服务器连通
        /**
         * 用户解除设备绑定接口
         * @param token 登录返回的token
         * @param userId 用户id
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("equipment/unbind")
        Flowable<ServerResponse> unbind(@Header(TOKEN_HEADER_KEY) String token, @Field("uid") int userId);

        /**
         * 获取绑定设备位置接口
         * @param token 登录返回的token
         * @param deviceId 设备id
         * @return 包含服务器返回结果的被观察者对象
         */
        @GET("location/getLocation")
        Flowable<ServerResponse> getDeviceLocation(@Header(TOKEN_HEADER_KEY) String token, @Query("eid") String deviceId);
    }


    interface IThirdPartyOperate {
        /**
         * 新浪授权后获取用户信息接口
         * @param token 授权成功获取到的token
         * @param uid 授权成功获取到的uid
         * @return 包含服务器返回结果的被观察者对象
         */
        @GET("https://api.weibo.com/2/users/show.json")
        Flowable<SinaUserInfo> getUserInfoFromSina(@Query("access_token") String token, @Query("uid") long uid);
    }
}
