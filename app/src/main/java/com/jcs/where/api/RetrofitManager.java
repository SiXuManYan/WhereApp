package com.jcs.where.api;

import android.content.Context;

import com.jcs.where.BuildConfig;
import com.jcs.where.api.convert.NullOrEmptyConvertFactory;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static RetrofitManager manager;
    private final int timeout = 10;
    private Retrofit retrofit;

    public static synchronized RetrofitManager getManager() {
        if (manager == null) {
            manager = new RetrofitManager();
        }
        return manager;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void initRetrofit(Context context) {
        if (retrofit == null) {
            create(context);
        }
    }

    private void create(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        // 四秒请求超时
        okBuilder.callTimeout(4, TimeUnit.SECONDS);
        // 添加请求头
        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder().header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                        .header("device","android");
                String language = CacheUtil.getLanguageFromCache();
                if (language.equals("auto")) {
                    language = "zh-CN";
                }
                requestBuilder.header("Locale", language);
                String jsonStr = CacheUtil.needUpdateBySpKey(SPKey.K_TOKEN);
                if (!jsonStr.equals("")) {
                    requestBuilder.header("Authorization", "Bearer " + jsonStr);
                }
                Request build = requestBuilder.build();
                return chain.proceed(build);
            }
        });
        if (BuildConfig.DEBUG) {
            okBuilder.addInterceptor(loggingInterceptor);
        }
//        okBuilder.addInterceptor(new BaseUrlInterceptor());
//        okBuilder.connectTimeout(timeout, TimeUnit.MINUTES);
//        okBuilder.readTimeout(timeout, TimeUnit.MINUTES);
//        okBuilder.writeTimeout(timeout, TimeUnit.MINUTES);


        Retrofit.Builder builder = new Retrofit.Builder();
        //请求链接前缀，所有接口不变的部分，字符串必须以/结尾
        // TODO baseurl 应该放在gradle中，没时间
        // 正式版
//        builder.baseUrl("https://appapi.wheretech.ph/");
        // 测试版
        builder.baseUrl("https://api.jcstest.com/");
        //用于做网络请求到客户端（okHttp3）
        builder.client(okBuilder.build());
        builder.addConverterFactory(new NullOrEmptyConvertFactory());
        //解析工程，可以帮我们把数据直接解析成对象
        builder.addConverterFactory(GsonConverterFactory.create());
        //异步回调适配工程
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        retrofit = builder.build();
    }
}
