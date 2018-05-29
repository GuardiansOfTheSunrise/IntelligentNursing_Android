package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.User;
import com.gots.intelligentnursing.entity.UserInfo;

import java.util.ArrayList;

/**
 * 登录的用户信息容器
 * 采用单例模式
 * @author zhqy
 * @date 2018/3/29
 */

public class UserContainer {

    private User mUser;

    private void testTokenInvalidatePasswordCorrectUser() {
        mUser.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5YW5nIiwiZXhwIjoxNTI2NDM5MjIzLCJpYXQiOjE1MjY0MzkxMjN9.5keExI8MoGkWMKhfvTMengzi-jSeduYil9agyt9xw_hAa7BUlrjaHxtaizFwjkHRtcm11Q_TNkq3-f3rjSHBnw");
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("yang");
        userInfo.setPassword("yang");
        userInfo.setLocationDataList(new ArrayList<>());
        mUser.setUserInfo(userInfo);
    }

    private void testTokenInvalidatePasswordErrorUser() {
        mUser.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5YW5nIiwiZXhwIjoxNTI2NDM5MjIzLCJpYXQiOjE1MjY0MzkxMjN9.5keExI8MoGkWMKhfvTMengzi-jSeduYil9agyt9xw_hAa7BUlrjaHxtaizFwjkHRtcm11Q_TNkq3-f3rjSHBnw");
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("yang");
        userInfo.setPassword("yang1");
        userInfo.setLocationDataList(new ArrayList<>());
        mUser.setUserInfo(userInfo);
    }

    private UserContainer(){
        mUser = new User();
        //testTokenInvalidatePasswordCorrectUser();
    }

    public static void setUser(User user) {
        InstanceHolder.sInstance.mUser = user;
    }

    public static User getUser() {
        return InstanceHolder.sInstance.mUser;
    }

    private static class InstanceHolder {
        private static UserContainer sInstance = new UserContainer();
    }
}
