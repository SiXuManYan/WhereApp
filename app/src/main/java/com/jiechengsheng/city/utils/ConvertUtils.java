package com.jiechengsheng.city.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ConvertUtils {

    public static <T> Map<String, Object> convertToMap(T bean) {
        Map<String, Object> returnMap = new HashMap<>();
        Class type = bean.getClass();
        Field[] fields = type.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                String key = field.getName();
                Object value = null;
                try {
                    value = field.get(bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value != null) {
                    if (value instanceof File) {
                        value = convertToPartBody(key, (File) value);
                    }
                    returnMap.put(key, value);
                }
            }
        }
        return returnMap;
    }

    public static MultipartBody.Part convertToPartBody(String key, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }
}
