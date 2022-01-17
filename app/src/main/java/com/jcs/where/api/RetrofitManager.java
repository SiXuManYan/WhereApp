package com.jcs.where.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.jcs.where.BuildConfig;
import com.jcs.where.api.convert.NullOrEmptyConvertFactory;
import com.jcs.where.api.util.StringConverter;
import com.jcs.where.utils.CacheUtil;
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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Log.d("请求日志", message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        // 连接超时
        okBuilder.connectTimeout(30,TimeUnit.SECONDS);
        okBuilder.readTimeout(30,TimeUnit.SECONDS);
        okBuilder.writeTimeout(30,TimeUnit.SECONDS);

        // 请求超时
        okBuilder.callTimeout(30, TimeUnit.SECONDS);
        // 添加请求头
        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder().header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                        .header("device", "android")
                        .header("version", BuildConfig.VERSION_NAME);

                String language = CacheUtil.getLanguageFromCache();
                if (language.equals("auto")) {
                    language = "zh-CN";
                }
                requestBuilder.header("Locale", language);
                String jsonStr = CacheUtil.getToken();
                if (!jsonStr.equals("")) {
                    requestBuilder.header("Authorization", "Bearer " + jsonStr);
                }

                Request build = requestBuilder.build();
                return chain.proceed(build);
            }
        });
//        if (BuildConfig.DEBUG) {
            okBuilder.addInterceptor(loggingInterceptor);
//        }
//        okBuilder.addInterceptor(new BaseUrlInterceptor());
//        okBuilder.connectTimeout(timeout, TimeUnit.MINUTES);
//        okBuilder.readTimeout(timeout, TimeUnit.MINUTES);
//        okBuilder.writeTimeout(timeout, TimeUnit.MINUTES);


        Retrofit.Builder builder = new Retrofit.Builder();
        //请求链接前缀，所有接口不变的部分，字符串必须以/结尾

        builder.baseUrl(BuildConfig.SERVER_HOST);


        //用于做网络请求到客户端（okHttp3）
        builder.client(okBuilder.build());
        builder.addConverterFactory(new NullOrEmptyConvertFactory());
        //解析工程，可以帮我们把数据直接解析成对象
        builder.addConverterFactory(GsonConverterFactory.create(buildGson()));
        //异步回调适配工程
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        retrofit = builder.build();
    }

    /**
     * 增加后台返回"null"的处理
     * 转换成 "" 字符串
     */
    public Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(JsonElement.class, new StringConverter())
                .create();
    }
}
