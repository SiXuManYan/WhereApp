package com.jcs.where.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jcs.where.bean.UserBean;
import com.jcs.where.login.LoginActivity;
import com.jcs.where.utils.PreferencesUtils;

public class UserManager {

    private static volatile UserManager sInstance;

    private UserBean mUser;

    private UserManager() {

    }

    public static UserManager get() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                }
            }
        }
        return sInstance;
    }

    public void login(Context context, UserBean userBean) {
        saveUser(context, userBean);
    }

    private void saveUser(Context context, UserBean user) {
        mUser = user;

        Gson gson = new Gson();
        String json = gson.toJson(user);
        PreferencesUtils.setData(context, PreferencesUtils.KEY_USER, json);
    }

    public void logout(Context context) {
        // 删除用户信息
        mUser = null;
        PreferencesUtils.removeData(context, PreferencesUtils.KEY_USER);
    }

    public boolean isLogin(Context context) {
//        String token = getToken(context);
//        L.e(">>> " + token);
        return !TextUtils.isEmpty(getUserId(context));
    }

    public String getUserId(Context context) {
        check(context);
        return mUser.id;
    }

    public UserBean getUser(Context context) {
        check(context);
        return mUser;
    }

    private void check(Context context) {
        if (mUser == null) {
            mUser = parseUser(context);
        }
        if (mUser == null) {
            mUser = new UserBean();
        }
    }

    private static UserBean parseUser(Context context) {
        String json = (String) PreferencesUtils.getData(context, PreferencesUtils.KEY_USER, "");
        Gson gson = new Gson();
        return gson.fromJson(json, UserBean.class);
    }

    public static boolean checkLogin(Context context) {
        boolean isLogin = UserManager.get().isLogin(context);
        if (!isLogin) {
            LoginActivity.goTo(context);
        }
        return isLogin;
    }


}
