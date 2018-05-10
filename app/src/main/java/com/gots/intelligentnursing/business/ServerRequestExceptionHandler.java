package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.exception.ServerException;
import com.gots.intelligentnursing.tools.LogUtil;

import java.net.SocketException;

import retrofit2.HttpException;

/**
 * @author zhqy
 * @date 2018/4/3
 */

public class ServerRequestExceptionHandler {

    private static final String TAG = "ServerRequestExceptionHandler";

    private static final String THROWABLE_MSG_ON_AUTHORIZATION_ERROR = "HTTP 401 ";

    private static final String HINT_SERVER_ERROR = "服务器错误";
    private static final String HINT_AUTHORIZATION_ERROR = "登录出错，请重新登录";
    private static final String HINT_NETWORK_ERROR = "网络异常";
    private static final String HINT_UNKNOWN_ERROR = "未知错误";


    public static String handle(Throwable throwable) {
        LogUtil.i(TAG, throwable.getClass().getName() + "---" + throwable.getMessage());
        if (throwable instanceof ServerException) {
            return throwable.getMessage();
        } else if (throwable instanceof SocketException) {
            return HINT_NETWORK_ERROR;
        } else if (throwable instanceof HttpException) {
            if (THROWABLE_MSG_ON_AUTHORIZATION_ERROR.equals(throwable.getMessage())) {
                return HINT_AUTHORIZATION_ERROR;
            }
            return HINT_SERVER_ERROR;
        } else {
            return HINT_UNKNOWN_ERROR;
        }
    }
}
