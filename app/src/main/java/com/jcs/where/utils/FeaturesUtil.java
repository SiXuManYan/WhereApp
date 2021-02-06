package com.jcs.where.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.blankj.utilcode.constant.RegexConstants;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jcs.where.R;
import com.jcs.where.frams.common.Html5Url;
import com.jcs.where.utils.image.Glide4Engine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by Wangsw  2021/1/29 11:47.
 * 业务逻辑相关工具类
 */
public class FeaturesUtil {

    /**
     * 验证手机号格式
     *
     * @param prefix  前缀
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


    /**
     * 格式化手机号，中间4位加密
     *
     * @param account
     * @return
     */
    public static String getFormatPhoneNumber(String account) {
        if (TextUtils.isEmpty(account) || account.length() < 10) {
            return account;
        }
        int length = account.length();
        int startCount = length - 8;
        String s = String.valueOf(startCount);
        return account.replaceAll("(\\d{" + s + "})\\d{4}(\\d{4})", "$1****$2");
    }


    /**
     * 密码格式校验
     * 6~20位 数字+字母组合
     */
    public static boolean isWrongPasswordFormat(String password) {

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort(StringUtils.getString(R.string.input_password_hint));
            return true;
        }
        if (password.length() < 6 || password.length() > 20) {
            ToastUtils.showShort(R.string.password_length_error_hint);
            return true;
        }

        if (!RegexUtils.isMatch(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]", password)) {
            ToastUtils.showShort(R.string.password_format_error_hint);
            return true;
        }
        return false;
    }


    /**
     * edit text 切换至明文
     */
    public static void editOpen(EditText editText, ImageView imageView) {
        editOpen(editText, imageView, null);
    }

    /**
     * edit text 切换至明文
     */
    public static void editOpen(EditText editText, ImageView imageView, @Nullable @DrawableRes Integer resId) {

        if (resId == null) {
            imageView.setImageResource(R.mipmap.ic_login_eye_open);
        } else {
            imageView.setImageResource(resId);
        }
        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editText.setSelection(editText.getText().toString().length());
    }


    /**
     * edit text 切换至密文
     */
    public static void editDismiss(EditText editText, ImageView imageView) {
        editDismiss(editText, imageView, null);
    }

    /**
     * edit text 切换至密文
     */
    public static void editDismiss(EditText editText, ImageView imageView, @Nullable @DrawableRes Integer resId) {
        if (resId == null) {
            imageView.setImageResource(R.mipmap.ic_login_eye_close);
        } else {
            imageView.setImageResource(resId);
        }
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setSelection(editText.getText().toString().length());
    }


    /**
     * 选择国家码
     *
     * @return
     */
    public static void getCountryPrefix(Context context, OnCountryCodeSelectListener onCountryCodeSelectListener) {

        String[] stringArray = StringUtils.getStringArray(R.array.country_prefix);
        // 默认菲律宾
        final String[] prefix = {stringArray[0]};

        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_country_prefix, null);
        dialog.setContentView(view);
        try {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.setBackgroundResource(android.R.color.transparent);
        } catch (Exception ignored) {

        }
        view.findViewById(R.id.philippines_tv).setOnClickListener(v -> {
            onCountryCodeSelectListener.onCountryCodeSelect(stringArray[0]);
            dialog.dismiss();
        });
        view.findViewById(R.id.china_tv).setOnClickListener(v13 -> {
            onCountryCodeSelectListener.onCountryCodeSelect(stringArray[1]);
            dialog.dismiss();
        });
        view.findViewById(R.id.confirm_tv).setOnClickListener(v1 -> {
            onCountryCodeSelectListener.onCountryCodeSelect(prefix[0]);
            dialog.dismiss();
        });
        dialog.show();


    }

    public interface OnCountryCodeSelectListener {

        void onCountryCodeSelect(String countryCode);
    }

    public static void handleMediaSelect(Activity activity, int mediaType, @IdRes int fromViewId) {

        PermissionUtils.permissionAny(activity, granted -> {

            Set<MimeType> mimeTypes;
            if (mediaType == 0) {
                mimeTypes = MimeType.ofAll();
            } else {
                mimeTypes = MimeType.ofImage();
            }
            mimeTypes.remove(MimeType.GIF);

            if (granted) {
                Matisse.from(activity)
                        .choose(mimeTypes)
                        .countable(true)
                        .maxSelectable(1)
                        .theme(R.style.Matisse_Dracula)
                        .thumbnailScale(0.87f)
                        .imageEngine(new Glide4Engine())
                        .forResult(Constant.REQUEST_MEDIA);

            } else {
                ToastUtils.showShort(R.string.open_permission);
            }


        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 用户协议
     */
    public static String getUserAgreement() {
        return String.format(Html5Url.USER_AGREEMENT_URL,  getLanguage());
    }

    /**
     * 隐私政策
     *
     * @return
     */
    public static String getPrivacyPolicy() {
        return String.format(Html5Url.PRIVACY_POLICY, getLanguage());
    }
    @NotNull
    private static String getLanguage() {
        String language = CacheUtil.getLanguageFromCache();
        if (language.equals("auto")) {
            language = "zh-CN";
        }
        return language;
    }


}
