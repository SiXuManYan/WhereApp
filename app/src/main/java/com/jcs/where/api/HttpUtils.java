package com.jcs.where.api;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.google.gson.Gson;
import com.jcs.where.Const;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpUtils {

    //线程池
    private static final ExecutorService executor;
    private static final Handler mHandler;

    static {
        executor = Executors.newFixedThreadPool(5);
        mHandler = new Handler();
    }

    /**
     * 执行网络请求操作,返回数据会解析成字符串String
     *
     * @param method 请求方式(需要传入String类型的参数:"GET","POST")
     * @param url    请求的url
     * @param params 请求的参数
     */
    public static String doHttpReqeust(final String method, final String url,
                                       final Map<String, String> params, final String local, final String tooken, final StringCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    URL u = new URL(Const.API_ENDPOINT + url);
                    connection = (HttpURLConnection) u.openConnection();
                    // 设置输入可用
                    connection.setDoInput(true);
                    if (method.equals("POST")) {
                        // 设置输出可用
                        connection.setDoOutput(true);
                    }
                    if (method.equals("DEL")) {
                        // 设置输出可用
                        connection.setDoOutput(true);
                    }
                    // 设置请求方式
                    connection.setRequestMethod(method);
                    //设置请求头
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Locale", local);
                    connection.setRequestProperty("Authorization", "Bearer" + " " + tooken);
                    // 设置连接超时
                    connection.setConnectTimeout(40000);
                    // 设置读取超时
                    connection.setReadTimeout(40000);
                    // 设置缓存不可用
                    connection.setUseCaches(false);
                    // 开始连接
                    connection.connect();

                    // 只有当POST请求时才会执行此代码段
                    if (params != null) {
                        // 获取输出流,connection.getOutputStream已经包含了connect方法的调用
                        outputStream = connection.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, String>> sets = params.entrySet();
                        // 将Hashmap转换为string
                        for (Map.Entry<String, String> entry : sets) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);
                        // 使用输出流将string类型的参数写到服务器
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String result = inputStream2String(inputStream);
                        if (result != null && callback != null) {
                            postSuccessString(callback, responseCode, result);
                        }
                    } else if (responseCode == 400) {
                        InputStream inputStream = connection.getErrorStream();
                        String result = inputStream2String(inputStream);
                        if (result != null && callback != null) {
                            postSuccessString(callback, responseCode, result);
                        }
                    } else {
                        if (callback != null) {
                            postFailed(callback, responseCode, new Exception("网络错误，请重新尝试"));
                        }
//                        InputStream inputStream = connection.getErrorStream();
//                        String result = inputStream2String(inputStream);
//                        if (result != null && callback != null) {
//                            postSuccessString(callback, responseCode, result);
//                        }
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        postFailed(callback, 0, e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return null;
    }

    public static String doGoogleMapReqeust(final String method, final String url,
                                            final Map<String, String> params, final StringCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    URL u = new URL(url);
                    connection = (HttpURLConnection) u.openConnection();
                    // 设置输入可用
                    connection.setDoInput(true);
                    if (method.equals("POST")) {
                        // 设置输出可用
                        connection.setDoOutput(true);
                    }
                    if (method.equals("DEL")) {
                        // 设置输出可用
                        connection.setDoOutput(true);
                    }
                    // 设置请求方式
                    connection.setRequestMethod(method);
                    // 设置连接超时
                    connection.setConnectTimeout(40000);
                    // 设置读取超时
                    connection.setReadTimeout(40000);
                    // 设置缓存不可用
                    connection.setUseCaches(false);
                    // 开始连接
                    connection.connect();

                    // 只有当POST请求时才会执行此代码段
                    if (params != null) {
                        // 获取输出流,connection.getOutputStream已经包含了connect方法的调用
                        outputStream = connection.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, String>> sets = params.entrySet();
                        // 将Hashmap转换为string
                        for (Map.Entry<String, String> entry : sets) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);
                        // 使用输出流将string类型的参数写到服务器
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String result = inputStream2String(inputStream);
                        if (result != null && callback != null) {
                            postSuccessString(callback, responseCode, result);
                        }
                    } else if (responseCode == 400) {
                        InputStream inputStream = connection.getErrorStream();
                        String result = inputStream2String(inputStream);
                        if (result != null && callback != null) {
                            postSuccessString(callback, responseCode, result);
                        }
                    } else {
                        if (callback != null) {
                            postFailed(callback, responseCode, new Exception("网络错误，请重新尝试"));
                        }
//                        InputStream inputStream = connection.getErrorStream();
//                        String result = inputStream2String(inputStream);
//                        if (result != null && callback != null) {
//                            postSuccessString(callback, responseCode, result);
//                        }
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        postFailed(callback, 0, e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return null;
    }


    public static String doHttpintReqeust(final String method, final String url,
                                          final Map<String, Integer> params, final String local, final String tooken, final StringCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    URL u = new URL(Const.API_ENDPOINT + url);
                    connection = (HttpURLConnection) u.openConnection();
                    // 设置输入可用
                    connection.setDoInput(true);
                    if (method.equals("POST")) {
                        // 设置输出可用
                        connection.setDoOutput(true);
                    }
                    if (method.equals("DEL")) {
                        // 设置输出可用
                        connection.setDoOutput(true);
                    }
                    // 设置请求方式
                    connection.setRequestMethod(method);
                    //设置请求头
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Locale", local);
                    connection.setRequestProperty("Authorization", "Bearer" + " " + tooken);
                    // 设置连接超时
                    connection.setConnectTimeout(40000);
                    // 设置读取超时
                    connection.setReadTimeout(40000);
                    // 设置缓存不可用
                    connection.setUseCaches(false);
                    // 开始连接
                    connection.connect();

                    // 只有当POST请求时才会执行此代码段
                    if (params != null) {
                        // 获取输出流,connection.getOutputStream已经包含了connect方法的调用
                        outputStream = connection.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, Integer>> sets = params.entrySet();
                        // 将Hashmap转换为string
                        for (Map.Entry<String, Integer> entry : sets) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);
                        // 使用输出流将string类型的参数写到服务器
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String result = inputStream2String(inputStream);
                        if (result != null && callback != null) {
                            postSuccessString(callback, responseCode, result);
                        }
                    } else if (responseCode == 400) {
                        InputStream inputStream = connection.getErrorStream();
                        String result = inputStream2String(inputStream);
                        if (result != null && callback != null) {
                            postSuccessString(callback, responseCode, result);
                        }
                    } else {
                        if (callback != null) {
                            postFailed(callback, responseCode, new Exception("网络错误，请重新尝试"));
                        }
//                        InputStream inputStream = connection.getErrorStream();
//                        String result = inputStream2String(inputStream);
//                        if (result != null && callback != null) {
//                            postSuccessString(callback, responseCode, result);
//                        }
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        postFailed(callback, 0, e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return null;
    }


    /**
     * 执行网络请求操作,返回数据是对象
     *
     * @param method 请求方式(需要传入String类型的参数:"GET","POST")
     * @param url    请求的url
     * @param params 请求的参数
     */
    public static <T> void doHttpReqeust(final String method, final String url,
                                         final Map<String, String> params, final Class<T> cls, final ObjectCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    URL u = new URL(url);
                    connection = (HttpURLConnection) u.openConnection();
                    // 设置输入可用
                    connection.setDoInput(true);
                    // 设置输出可用
                    connection.setDoOutput(true);
                    // 设置请求方式
                    connection.setRequestMethod(method);
                    // 设置连接超时
                    connection.setConnectTimeout(5000);
                    // 设置读取超时
                    connection.setReadTimeout(5000);
                    // 设置缓存不可用
                    connection.setUseCaches(false);
                    // 开始连接
                    connection.connect();

                    // 只有当POST请求时才会执行此代码段
                    if (params != null) {
                        // 获取输出流,connection.getOutputStream已经包含了connect方法的调用
                        outputStream = connection.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, String>> sets = params.entrySet();
                        // 将Hashmap转换为string
                        for (Map.Entry<String, String> entry : sets) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);
                        // 使用输出流将string类型的参数写到服务器
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String result = inputStream2String(inputStream);
                        if (result != null && callback != null) {
                            postSuccessObject(callback, new Gson().fromJson(result, cls));
                        }
                    } else {
                        if (callback != null) {
                            postFailed(callback, responseCode, new Exception("请求数据失败：" + responseCode));
                        }
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        postFailed(callback, 0, e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    private static void postSuccessString(final StringCallback callback, final int code, final String result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(code, result);
            }
        });
    }

    private static void postSuccessBitmap(final Callback callback, final Bitmap bitmap) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                BitmapCallback bitmapCallback = (BitmapCallback) callback;
                bitmapCallback.onSuccess(bitmap);
            }
        });
    }

    private static void postSuccessByte(final Callback callback, final byte[] bytes) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ByteArrayCallback byteArrayCallback = (ByteArrayCallback) callback;
                byteArrayCallback.onSuccess(bytes);
            }
        });
    }

    private static <T> void postSuccessObject(final ObjectCallback callback, final T t) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ObjectCallback objectCallback = (ObjectCallback) callback;
                objectCallback.onSuccess(t);
            }
        });
    }

    private static void postFailed(final Callback callback, final int code, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFaileure(code, e);
            }
        });
    }

    /**
     * 字节流转换成字符串
     *
     * @param inputStream
     * @return
     */
    private static String inputStream2String(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 字节流转换成字节数组
     *
     * @param inputStream 输入流
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream inputStream) {
        byte[] result = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 缓冲区
        byte[] bytes = new byte[1024];
        int len = -1;
        try {
            // 使用字节数据输出流来保存数据
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            result = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 判断是否联网
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public interface Callback {
        void onFaileure(int code, Exception e);
    }

    public interface StringCallback extends Callback {
        void onSuccess(int code, String result);
    }

    public interface BitmapCallback extends Callback {
        void onSuccess(Bitmap bitmap);
    }

    public interface ByteArrayCallback extends Callback {
        void onSuccess(byte[] bytes);
    }

    public interface ObjectCallback<T> extends Callback {
        void onSuccess(T t);
    }
}

