package com.gots.intelligentnursing.business;

import android.content.Context;
import android.content.SharedPreferences;

import com.gots.intelligentnursing.tools.EncryptUtil;
import com.gots.intelligentnursing.tools.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhqy
 * @date 2018/5/11
 */

public class FileCacheManager {

    private static final String TAG = "FileCacheManager";

    private static final String FILE_NAME = "cache";
    public static final String KEY_USERNAME = "824EC59323936251";
    public static final String KEY_PASSWORD = "5AA765D61D8327DE";

    private static FileCacheManager sFileCacheManager;

    private SharedPreferences mSharedPreferences;

    private FileCacheManager(Context context) {
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveUsernameAndPassword(String username, String password) {
        String encryptedUsername;
        String encryptedPassword;
        LogUtil.i(TAG, "The username before encrypt is : " + username);
        LogUtil.i(TAG, "The password before encrypt is : " + password);
        try {
            encryptedUsername = EncryptUtil.encrypt(username);
            encryptedPassword = EncryptUtil.encrypt(password);
            LogUtil.i(TAG, "The username after encrypt is : " + encryptedUsername);
            LogUtil.i(TAG, "The password after encrypt is : " + encryptedPassword);

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(KEY_USERNAME, encryptedUsername);
            editor.putString(KEY_PASSWORD, encryptedPassword);
            editor.apply();
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            LogUtil.e(TAG, e.getStackTrace().toString());
        }
    }

    public Map<String, String> readUsernameAndPassword() {
        String encryptedUsername = mSharedPreferences.getString(KEY_USERNAME, null);
        String encryptedPassword = mSharedPreferences.getString(KEY_PASSWORD, null);
        if (encryptedUsername != null && encryptedPassword != null) {
            LogUtil.i(TAG, "The encrypted username read from cache is : " + encryptedUsername);
            LogUtil.i(TAG, "The encrypted password read from cache is : " + encryptedPassword);
            String username = encryptedUsername;
            String password = encryptedPassword;
            try {
                username = EncryptUtil.decrypt(encryptedUsername);
                password = EncryptUtil.decrypt(encryptedPassword);
                LogUtil.i(TAG,  "The username after decrypt is : " + username);
                LogUtil.i(TAG,  "The password after decrypt is : " + password);
            } catch (Exception e) {
                LogUtil.e(TAG, e.toString());
                LogUtil.e(TAG, e.getStackTrace().toString());
            }
            Map<String, String> map = new HashMap<>(2);
            map.put(KEY_USERNAME, username);
            map.put(KEY_PASSWORD, password);
            return map;
        }
        return null;
    }

    public void clearUsernameAndPassword() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    public static FileCacheManager getInstance(Context context) {
        if (sFileCacheManager == null) {
            synchronized (FileCacheManager.class) {
                if (sFileCacheManager == null) {
                    if (context == null) {
                        throw new IllegalArgumentException();
                    }
                    sFileCacheManager = new FileCacheManager(context);
                }
            }
        }
        return sFileCacheManager;
    }
}
