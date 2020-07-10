package com.jcs.where.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.jcs.where.bean.LoginBean;
import com.jcs.where.utils.PreferencesUtils;

public class TokenManager {
    private static volatile TokenManager sInstance;

    private LoginBean mLogin;

    private TokenManager() {

    }

    public static TokenManager get() {
        if (sInstance == null) {
            synchronized (TokenManager.class) {
                if (sInstance == null) {
                    sInstance = new TokenManager();
                }
            }
        }
        return sInstance;
    }

    public void login(Context context, LoginBean loginBean) {
        saveToken(context, loginBean);
    }


    private void saveToken(Context context, LoginBean login) {
        mLogin = login;

        Gson gson = new Gson();
        String json = gson.toJson(login);
        PreferencesUtils.setData(context, PreferencesUtils.KEY_TOKEN, json);
    }

    public void deleteToken(Context context) {
        // 删除用户信息
        mLogin = null;
        PreferencesUtils.removeData(context, PreferencesUtils.KEY_USER);
    }

    public String getToken(Context context) {
        check(context);
        return mLogin.token;
    }

    private void check(Context context) {
        if (mLogin == null) {
            mLogin = parseToken(context);
        }
        if (mLogin == null) {
            mLogin = new LoginBean();
        }
    }

    private static LoginBean parseToken(Context context) {
        String json = (String) PreferencesUtils.getData(context, PreferencesUtils.KEY_TOKEN, "");
        Gson gson = new Gson();
        return gson.fromJson(json, LoginBean.class);
    }

}
