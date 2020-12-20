package com.jcs.where.api;

import android.content.Context;

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
    private final int timeout = 2;
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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        //添加请求头
        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder().header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                        .header("Locale", "zh-CN");
//                String token = TokenManager.get().getToken(context);
//                if (token != null) {
//                    requestBuilder.header("Authorization", token);
//                }
                requestBuilder.header("Authorization",
                        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYXBpLmpjc3Rlc3QuY29tXC91c2VyYXBpXC92MVwvbG9naW4iLCJpYXQiOjE2MDY5ODUwNTQsImV4cCI6MTYwOTU3NzA1NCwibmJmIjoxNjA2OTg1MDU0LCJqdGkiOiIwRnZLNW9NMUZtUVNIN25sIiwic3ViIjoxMSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.UrQy6Wi6qlTyAMPgtUVYreR7maSUpwPxO7vPFXm1MKE");
                Request build = requestBuilder.build();
                return chain.proceed(build);
            }
        });
        okBuilder.addInterceptor(interceptor);
        okBuilder.connectTimeout(timeout, TimeUnit.MINUTES);
        okBuilder.readTimeout(timeout, TimeUnit.MINUTES);
        okBuilder.writeTimeout(timeout, TimeUnit.MINUTES);


        Retrofit.Builder builder = new Retrofit.Builder();
        //请求链接前缀，所有接口不变的部分，字符串必须以/结尾
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
