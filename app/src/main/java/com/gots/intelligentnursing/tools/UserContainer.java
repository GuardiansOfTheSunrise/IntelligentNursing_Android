package com.gots.intelligentnursing.tools;

import com.gots.intelligentnursing.entity.User;

/**
 * 登录的用户信息容器
 * 采用单例模式
 * @author zhqy
 * @date 2018/3/29
 */

public class UserContainer {

    private User mUser;

    private UserContainer(){}

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public static UserContainer getInstance(){
        return InstanceHolder.sInstance;
    }

    private static class InstanceHolder{
        private static UserContainer sInstance = new UserContainer();
    }
}
