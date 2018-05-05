package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.entity.User;

import java.util.ArrayList;
import java.util.List;

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
        mUser.setId(1);
        mUser.setUsername("admin");
        mUser.setBindingDeviceId("6cb871e1231b");
        mUser.setBindingDevicePassword("a64f");
        List<LocationData> locationDataList = new ArrayList<>();
        locationDataList.add(new LocationData(40.025227091303584, 116.39679558757584));
        locationDataList.add(new LocationData(39.99854254455436, 116.27485061459633));
        locationDataList.add(new LocationData(39.90085373092943, 116.32809318217274));
        locationDataList.add(new LocationData(39.95637017643415, 116.26045077727139));
        locationDataList.add(new LocationData(39.84982050827932, 116.22565042181485));
        locationDataList.add(new LocationData(39.711646098499, 116.33979810296775));
        locationDataList.add(new LocationData(39.729010360895444, 116.45225696976188));
        locationDataList.add(new LocationData(39.790453992659586, 116.40070321654424));
        locationDataList.add(new LocationData(39.87117841270767, 116.38580931118881));
        locationDataList.add(new LocationData(39.91511237702834, 116.40395508249037));
        locationDataList.add(new LocationData(39.972041013267216, 116.55919125766965));
        locationDataList.add(new LocationData(40.085844711599435, 116.60876873876533));
        mUser.setFencePointDataList(locationDataList);
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
