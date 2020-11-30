package com.jcs.where;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.jcs.where.api.RetrofitManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
