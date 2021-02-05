package com.jcs.where.api;

import android.util.Log;

import com.jcs.where.BaseApplication;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<JcsResponse<T>> {


    private final CompositeDisposable mCompositeDisposable;

    public BaseObserver() {
        this.mCompositeDisposable = new CompositeDisposable();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.mCompositeDisposable.add(d);
    }

    @Override
    public void onComplete() {
        this.mCompositeDisposable.clear();
    }

    @Override
    public void onNext(@NotNull JcsResponse<T> tJcsResponse) {
        int code = tJcsResponse.getCode();
        if (code == 200) {
            onSuccess(tJcsResponse.getData());
        } else {
            ErrorResponse errorResponse = deployErrorResponse(String.valueOf(code));
            if (errorResponse == null){
                errorResponse = new ErrorResponse();
            }
            errorResponse.errMsg = tJcsResponse.getMessage();
            onError(errorResponse);
            if (code == 401) {
                BaseApplication.toLogin();
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        String message = e.getMessage();
        ErrorResponse errorResponse = deployErrorResponse(message);
        if (errorResponse == null) return;

        errorResponse.errMsg = "";
        onError(errorResponse);
    }

    @Nullable
    private ErrorResponse deployErrorResponse(String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        if (message == null) {
            errorResponse.errMsg = "空的错误信息";
            onError(errorResponse);
            return null;
        }
        if (message.contains("400")) {
            errorResponse.errCode = 400;
            errorResponse.errMsg = "客户端请求错误（建议弹出提示）";
        } else if (message.contains("401")) {
            errorResponse.errCode = 401;
            errorResponse.errMsg = "用户未授权（调起登录或刷新token）";
        } else if (message.contains("403")) {
            errorResponse.errCode = 403;
            errorResponse.errMsg = "暂无权限";
        } else if (message.contains("404")) {
            errorResponse.errCode = 404;
            errorResponse.errMsg = "资源未找到";
        } else if (message.contains("405")) {
            errorResponse.errCode = 405;
            errorResponse.errMsg = "方法不允许";
        } else if (message.contains("408")) {
            errorResponse.errCode = 408;
            errorResponse.errMsg = "请求超时";
        } else if (message.contains("422")) {
            errorResponse.errCode = 422;
            errorResponse.errMsg = "表单验证失败";
        } else if (message.contains("429")) {
            errorResponse.errCode = 429;
            errorResponse.errMsg = "请求次数过多";
        } else if (message.contains("500")) {
            errorResponse.errCode = 500;
            errorResponse.errMsg = "系统异常";
        } else {
            errorResponse.errMsg = message;
            Log.e("BaseObserver", "----onError---" + message);
        }
        return errorResponse;
    }

    protected abstract void onError(ErrorResponse errorResponse);

    protected abstract void onSuccess(T response);
}
