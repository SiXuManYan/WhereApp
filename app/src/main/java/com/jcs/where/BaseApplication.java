package com.jcs.where;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.jcs.where.api.RetrofitManager;

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.getManager().initRetrofit(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
