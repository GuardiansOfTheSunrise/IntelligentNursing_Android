package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.User;

/**
 * 登录的用户信息容器
 * 采用单例模式
 * @author zhqy
 * @date 2018/3/29
 */

public class UserContainer {

    private User mUser;

    private UserContainer(){
        mUser = new User();
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
