package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.NewsInfo;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.entity.VersionInfo;

import java.util.List;

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
         *
         * @param username 用户名
         * @param password 密码
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("auth2")
        Flowable<ServerResponse<String>> login(@Field("uname") String username, @Field("pwd") String password);

        /**
         * 获取验证码接口
         *
         * @param username 用户名
         * @param phone    手机号
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("getCode")
        Flowable<ServerResponse> getVerify(@Field("uname") String username, @Field("phone") String phone);

        /**
         * 用户注册接口
         *
         * @param username 用户名
         * @param password 密码
         * @param phone    手机号
         * @param verify   验证码
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("user/reg")
        Flowable<ServerResponse> register(@Field("uname") String username, @Field("pwd") String password,
                                          @Field("tel") String phone, @Field("code") String verify);


        /**
         * 老人信息补全接口
         *
         * @param token   登录后返回的token
         * @param age     老人年龄
         * @param height  老人身高
         * @param weight  老人体重
         * @param address 地址
         * @param phone   联系方式
         * @param remarks 备注
         * @param uid     用户id
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("updateUserInfo")
        Flowable<ServerResponse> elderInfoComplete(@Header(TOKEN_HEADER_KEY) String token, @Field("age") int age,
                                                   @Field("weight") String height, @Field("height") String weight,
                                                   @Field("addr") String address, @Field("phone") String phone,
                                                   @Field("remark") String remarks, @Field("uid") int uid);

        /**
         * qq登录
         *
         * @param openId qq的openId
         * @return 包含服务器返回结果的被观察者对象，如果用户信息已补全，则data中包含token，否则code不为0
         */
        @GET("qauth/qIsOurUser")
        Flowable<ServerResponse<String>> tencentAuth(@Query("openId") String openId);

        /**
         * qq登录信息补全接口
         *
         * @param username 用户名
         * @param password 密码
         * @param phone    手机
         * @param verify   验证码
         * @param openid   qq登录返回的openid
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("qauth/qreg")
        Flowable<ServerResponse<String>> tencentInfoCompleting(
                @Field("uname") String username, @Field("pwd") String password, @Field("tel") String phone,
                @Field("code") String verify, @Field("openId") String openid);

        /**
         * 微博登录
         *
         * @param openId 微博的openId
         * @return 包含服务器返回结果的被观察者对象，如果用户信息已补全，则data中包含token，否则code不为0
         */
        @GET("wauth/wIsOurUser")
        Flowable<ServerResponse<String>> sinaAuth(@Query("wid") String openId);

        /**
         * 微博登录信息补全接口
         *
         * @param username 用户名
         * @param password 密码
         * @param phone    手机
         * @param verify   验证码
         * @param openid   qq登录返回的openid
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("wauth/wreg")
        Flowable<ServerResponse<String>> sinaInfoCompleting(
                @Field("uname") String username, @Field("pwd") String password, @Field("tel") String phone,
                @Field("code") String verify, @Field("wid") String openid);


        /**
         * 用户登录后获取用户信息
         *
         * @param token 登录返回的token
         * @return 包含服务器返回结果的被观察者对象
         */
        @GET("user")
        Flowable<ServerResponse<UserInfo>> getUserInfo(@Header(TOKEN_HEADER_KEY) String token);

        /**
         * 用户设置围栏接口
         *
         * @param token    登录返回的token
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
         *
         * @param token   登录返回的token
         * @param userId  用户id
         * @param equipId 设备id
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("equipment/bind")
        Flowable<ServerResponse<String>> bind(@Header(TOKEN_HEADER_KEY) String token, @Field("uid") int userId, @Field("eid") String equipId);

        // TODO: 2018/5/29 接口尚未与服务器连通

        /**
         * 用户解除设备绑定接口
         *
         * @param token  登录返回的token
         * @param userId 用户id
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("equipment/unbind")
        Flowable<ServerResponse> unbind(@Header(TOKEN_HEADER_KEY) String token, @Field("uid") int userId);

        /**
         * 获取绑定设备位置接口
         *
         * @param token    登录返回的token
         * @param deviceId 设备id
         * @return 包含服务器返回结果的被观察者对象
         */
        @GET("location/getLocation")
        Flowable<ServerResponse> getDeviceLocation(@Header(TOKEN_HEADER_KEY) String token, @Query("eid") String deviceId);
    }

    /**
     * 应用相关操作
     */
    interface ISystemOperate {
        /**
         * 将崩溃日志上传至服务器
         *
         * @param log 崩溃日志
         * @return 包含服务器返回结果的被观察者对象
         */
        @FormUrlEncoded
        @POST("exception")
        Flowable<ServerResponse> uploadExceptionLog(@Field("text") String log);

        /**
         * 获取新闻信息
         *
         * @return 包含服务器返回结果的被观察者对象
         */
        @GET("news")
        Flowable<ServerResponse<List<NewsInfo>>> getNewsData();

        /**
         * 检查更新
         *
         * @return 包含服务器返回结果的被观察者对象
         */
        @GET("checkUpdate")
        Flowable<ServerResponse<VersionInfo>> checkUpdate();
    }
}
