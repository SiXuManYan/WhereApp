package com.jcs.where.api.interceptor;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * create by zyf on 2020/12/21 9:13 PM
 */
public class BaseUrlInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取request
        Request request = chain.request();
        //从request中获取原有的HttpUrl实例oldHttpUrl
        HttpUrl oldHttpUrl = request.url();
        //获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        //从request中获取headers，通过给定的键url_name
        List<String> headerValues = request.headers("baseUrl");
        HttpUrl newBaseUrl = null;
        if (headerValues.size() > 0) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            builder.removeHeader("baseUrl");
            //匹配获得新的BaseUrl
            String headerValue = headerValues.get(0);
            Log.e("BaseUrlInterceptor", "intercept: " + "headerValue=" + headerValue);
            if ("google/map".equals(headerValue)) {
                newBaseUrl = HttpUrl.parse("https://maps.googleapis.com/");
            } else {
                newBaseUrl = HttpUrl.parse("https://api.jcstest.com/");
            }
        }
        //重建新的HttpUrl，修改需要修改的url部分
        HttpUrl newFullUrl = oldHttpUrl
                .newBuilder()
                .scheme("https")//更换网络协议
                .host(newBaseUrl.host())//更换主机名
                .port(newBaseUrl.port())//更换端口
                .removePathSegment(0)//移除第一个参数
                .build();
//        if (newBaseUrl != null) {
//            重建这个request，通过builder.url(newFullUrl).build()；
//             然后返回一个response至此结束修改
//            return chain.proceed(builder.url(newFullUrl).build());
//        } else {
//        }
        return chain.proceed(request);
    }
}


