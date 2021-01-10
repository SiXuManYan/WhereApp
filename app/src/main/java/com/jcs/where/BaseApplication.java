package com.jcs.where;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.LanguageUtils;
import com.jcs.where.api.RetrofitManager;
import com.jcs.where.utils.LocalLanguageUtil;
import com.jcs.where.utils.LocationUtil;
import com.jcs.where.utils.SPUtil;

import androidx.multidex.MultiDex;

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.getManager().initRetrofit(this);
        LocationUtil.initInstance(this);
        SPUtil.initInstance(this);
        changeLanguage();
    }

    public void changeLanguage(){
        Log.e("BaseApplication", "changeLanguage: "+"-----");
        LocalLanguageUtil.getInstance().setApplicationLanguage(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        SPUtil.initInstance(base);
        super.attachBaseContext(LocalLanguageUtil.getInstance().setLocal(base));
        MultiDex.install(this);
    }
}
