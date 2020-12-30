package com.jcs.where;

import android.app.Application;
import android.content.Context;

import com.jcs.where.api.RetrofitManager;
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
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
