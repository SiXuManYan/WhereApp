package com.jcs.where.utils;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.constant.RegexConstants;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;

/**
 * Created by Wangsw  2021/1/29 11:47.
 * 业务逻辑相关工具类
 */
public class FeaturesUtil {

    /**
     * 验证手机号格式
     * @param prefix 前缀
     * @param account 账号
     * @return
     */
    public static boolean isWrongPhoneNumber(String prefix, String account) {
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShort(StringUtils.getString(R.string.login_phone_input));
            return true;
        }
        // 中国手机号
        if (prefix.contains("86") && !RegexUtils.isMatch(RegexConstants.REGEX_MOBILE_EXACT, account)) {
            com.blankj.utilcode.util.ToastUtils.showShort(R.string.wrong_phone_hint);
            return true;
        }
        // 菲律宾手机号
        if (prefix.contains("63") && account.length() < 10) {
            ToastUtils.showShort(R.string.wrong_phone_hint);
            return true;
        }
        return false;
    }



    public static void editOpen(EditText editText, ImageView imageView) {
        imageView.setImageResource(R.mipmap.ic_login_eye_open);
        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editText.setSelection(editText.getText().toString().length());
    }


    public static void editDismiss(EditText editText, ImageView imageView) {
        imageView.setImageResource(R.mipmap.ic_login_eye_close);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setSelection(editText.getText().toString().length());
    }





}
