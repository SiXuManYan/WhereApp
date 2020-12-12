package com.jcs.where.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class NullOrEmptyConvertFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NotNull Type type, @NotNull Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegate  = retrofit.nextResponseBodyConverter(this, type, annotations);
        return new Converter<ResponseBody, Object>() {
            @Nullable
            @Override
            public Object convert(@NotNull ResponseBody value) throws IOException {
                if(value.contentLength() == 0) return null;
                return delegate.convert(value);
            }
        };
    }
}