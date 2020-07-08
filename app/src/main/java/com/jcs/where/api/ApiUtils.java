package com.jcs.where.api;

import android.content.Context;

import com.jcs.where.R;

import co.tton.android.base.api.ApiResultException;
import co.tton.android.base.view.ToastUtils;

public class ApiUtils {

    public static void toastError(Context context, String message, Throwable e) {
        if (e != null && e.getCause() instanceof ApiResultException) {
            if (message == null) {
                String str = ((ApiResultException) e.getCause()).mMessage;
                ToastUtils.showShort(context, str);
            } else {
                ToastUtils.showShort(context, message);
            }
        } else {
            ToastUtils.showShort(context, context.getString(R.string.common_connection_error));
        }
    }

    public static void toastSuccess(Context context, String message) {
        ToastUtils.showShort(context, message);
    }

}
