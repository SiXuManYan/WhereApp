package com.jcs.where.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.constant.RegexConstants;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jcs.where.BuildConfig;
import com.jcs.where.R;
import com.jcs.where.frames.common.Html5Url;
import com.jcs.where.utils.image.Glide4Engine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (TextUtils.isEmpty(password) ||
                password.length() < 8 || password.length() > 16 ||
                !RegexUtils.isMatch(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]", password)) {

            ToastUtils.showShort(R.string.password_length_error_hint);
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
            imageView.setImageResource(R.mipmap.ic_login_eye_open_white);
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
            imageView.setImageResource(R.mipmap.ic_login_eye_close_white);
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

    public static void handleMediaSelect(Activity activity, int mediaType, int max) {

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
                        .maxSelectable(max)
                        .theme(R.style.Matisse_Dracula)
                        .thumbnailScale(0.87f)
                        .imageEngine(new Glide4Engine())
                        .forResult(Constant.REQUEST_MEDIA);

            } else {
                ToastUtils.showShort(R.string.open_permission);
            }


        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    public static void handleMediaSelect4Fragment(Fragment fragment, int mediaType, int max) {

        PermissionUtils.permissionAny(fragment.getActivity(), granted -> {

            Set<MimeType> mimeTypes;
            if (mediaType == 0) {
                mimeTypes = MimeType.ofAll();
            } else {
                mimeTypes = MimeType.ofImage();
            }
            mimeTypes.remove(MimeType.GIF);

            if (granted) {
                Matisse.from(fragment)
                        .choose(mimeTypes)
                        .countable(true)
                        .maxSelectable(max)
                        .theme(R.style.Matisse_Dracula)
                        .thumbnailScale(0.87f)
                        .imageEngine(new Glide4Engine())
                        .forResult(Constant.REQUEST_MEDIA);

            } else {
                ToastUtils.showShort(R.string.open_permission);
            }


        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 用户协议
     */
    public static String getUserAgreement() {
        return String.format(Html5Url.USER_AGREEMENT_URL, getLanguage());
    }

    /**
     * 隐私政策
     *
     * @return
     */
    public static String getPrivacyPolicy() {
        return String.format(Html5Url.PRIVACY_POLICY, getLanguage());
    }


    /**
     * 条款和条件
     *
     * @return
     */
    public static String getConditionAgreement() {
        return String.format(Html5Url.CONDITION_AGREEMENT, getLanguage());
    }


    @NotNull
    private static String getLanguage() {
        String language = CacheUtil.getLanguageFromCache();
        if (language.equals("auto") || language.equals("zh")) {
            language = "zh_cn";
        }
        return language;
    }


    /**
     * @param color   关键字颜色
     * @param text    文本
     * @param keyword 关键字
     * @return
     */
    public static SpannableString getHighLightKeyWord(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * @param color   关键字颜色
     * @param text    文本
     * @param keyword 多个关键字
     * @return
     */
    public static SpannableString getHighLightKeyWord(int color, String text, String[] keyword) {
        SpannableString s = new SpannableString(text);
        for (int i = 0; i < keyword.length; i++) {
            Pattern p = Pattern.compile(keyword[i]);
            Matcher m = p.matcher(s);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                s.setSpan(new ForegroundColorSpan(color), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return s;
    }

    /**
     * 根据评分返回修饰词
     */
    public static String getGradeRetouchString(float grade) {

        String retouch = "";

        if (grade >= 3 && grade < 3.5) {
            retouch = StringUtils.getString(R.string.grade_0);
        } else if (grade >= 3.5 && grade < 4) {
            retouch = StringUtils.getString(R.string.grade_1);
        } else if (grade >= 4 && grade < 4.5) {
            retouch = StringUtils.getString(R.string.grade_2);
        } else if (grade >= 4.5) {
            retouch = StringUtils.getString(R.string.grade_3);
        }
        return retouch;
    }


    public static void gotoGooglePlay(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(Constant.GOOGLE_PLAY_APP_STORE_PACKAGE_NAME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ActivityNotFoundException) {
                ToastUtils.showShort(R.string.is_client_valid);
            }

        }
    }


    /**
     * 获取美食订单状态
     * 美食： 订单状态（1：待付款，2：已取消，3：待使用，4：已完成，5：支付失败，6：退款中，7：已退款，8：退款失败，9：待评价）
     */
    public static void bindFoodOrderStatus(int status, @NotNull TextView textView) {
        String text = "";
//        textView.setTextColor(ColorUtils.getColor(R.color.black_333333));
        switch (status) {
            case 1:
                // 待付款
                text = StringUtils.getString(R.string.mine_unpaid);
//                textView.setTextColor(ColorUtils.getColor(R.color.orange_EF4814));
                break;
            case 2:
                // 已取消
                text = StringUtils.getString(R.string.cancelled);
                break;

            case 3:
                // 待使用
                text = StringUtils.getString(R.string.mine_booked);
//                textView.setTextColor(ColorUtils.getColor(R.color.yellow_FEAF26));
                break;
            case 4:
                // 已完成
                text = StringUtils.getString(R.string.completed);
                break;
            case 5:
                // 支付失败
                text = StringUtils.getString(R.string.payment_failed);
                break;
            case 6:
                // 退款中
                text = StringUtils.getString(R.string.refunding);
//                textView.setTextColor(ColorUtils.getColor(R.color.yellow_FEAF26));
                break;
            case 7:
                // 已退款
                text = StringUtils.getString(R.string.refunded);
                break;
            case 8:
                // 退款失败
                text = StringUtils.getString(R.string.refund_failed);
//                textView.setTextColor(ColorUtils.getColor(R.color.orange_EF4814));
                break;
            case 9:
                // 待评价
                text = StringUtils.getString(R.string.mine_reviews);
//                textView.setTextColor(ColorUtils.getColor(R.color.yellow_FEAF26));
                break;


            default:
                break;
        }
        textView.setText(text);
    }

    /**
     * 获取外卖订单状态
     * 外卖： 订单状态（1：待支付，2：未接单，3：已接单，4：已取消，5：已完成，6：支付失败，7：退款中，8：已退款，9：退款失败，10：待评价）
     */
    public static void bindTakeawayOrderStatus(int status, @NotNull TextView textView) {
        String text = "";
        textView.setTextColor(ColorUtils.getColor(R.color.black_333333));
        switch (status) {
            case 1:
                // 待支付
                text = StringUtils.getString(R.string.mine_unpaid);
                break;
            case 2:
                // 未接单
                text = StringUtils.getString(R.string.missed_orders);
                break;
            case 3:
                // 已接单
                text = StringUtils.getString(R.string.mine_booked);
                break;
            case 4:
                // 已取消
                text = StringUtils.getString(R.string.cancelled);
                break;
            case 5:
                // 已完成
                text = StringUtils.getString(R.string.completed);
                break;
            case 6:
                // 支付失败
                text = StringUtils.getString(R.string.payment_failed);
                break;
            case 7:
                // 退款中
                text = StringUtils.getString(R.string.refunding);
                break;
            case 8:
                // 已退款
                text = StringUtils.getString(R.string.refunded);
                break;
            case 9:
                // 退款失败
                text = StringUtils.getString(R.string.refund_failed);
                break;
            case 10:
                // 待评价
                text = StringUtils.getString(R.string.mine_reviews);
                break;
            default:
                break;
        }
        textView.setText(text);
    }


    /**
     * 获取商城订单状态
     *
     * @param status        自提时：（1：待付款，2：支付审核中，           4：待使用，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10：退款审核中（商家），11:商家待收货，12：商家拒绝退货 13交易失败），
     *                      配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货 13交易失败）
     * @param delivery_type 配送方式（1:自提，2:商家配送）
     */
    public static void bindStoreOrderStatus(int status, int delivery_type, @NotNull TextView textView) {
        String text = "";
        int textColor = ColorUtils.getColor(R.color.black_333333);
        switch (status) {
            case 1:
                text = StringUtils.getString(R.string.store_status_1);
                textColor = ColorUtils.getColor(R.color.orange_FD6431);
                break;
            case 2:
                text = StringUtils.getString(R.string.store_status_2);
                break;
            case 3:
                if (delivery_type == 1) {
                    text = "";
                } else {
                    text = StringUtils.getString(R.string.store_status_3);
                }
                break;
            case 4:
                if (delivery_type == 1) {
                    text = StringUtils.getString(R.string.store_status_4_1);
                } else {
                    text = StringUtils.getString(R.string.store_status_4_2);
                }
                break;
            case 5:
                text = StringUtils.getString(R.string.store_status_5);
                textColor = ColorUtils.getColor(R.color.orange_FD6431);
                break;
            case 6:
                text = StringUtils.getString(R.string.store_status_6);
                break;
            case 7:
                text = StringUtils.getString(R.string.store_status_7);
                break;
            case 8:
                text = StringUtils.getString(R.string.store_status_8);
                break;
            case 9:
                text = StringUtils.getString(R.string.store_status_9);
                break;
            case 10:
                text = StringUtils.getString(R.string.store_status_10);
                break;
            case 11:
                // 商家待收货
                text = StringUtils.getString(R.string.store_status_11);
                break;
            case 12:
                // 商家拒绝退货
                text = StringUtils.getString(R.string.store_status_12);
                textColor = ColorUtils.getColor(R.color.orange_FD6431);
                break;
            case 13:
                text = StringUtils.getString(R.string.store_status_13);
                break;
            default:
                break;
        }
        textView.setText(text);
        textView.setTextColor(textColor);
    }


    public static float getSafeStarLevel(String levelStr) {
        float level = 0f;
        if (!TextUtils.isEmpty(levelStr)) {
            try {
                level = Float.parseFloat(levelStr);
            } catch (NumberFormatException ignored) {
            }
        }
        return level;
    }

    public static void saveHistory(String input) {

        SPUtils shareDefault = CacheUtil.getShareDefault();

        String history = shareDefault.getString(SPKey.K_SEARCH_HISTORY, "");

        shareDefault.put(SPKey.K_SEARCH_HISTORY, history + input + ",");
    }

    public static ArrayList<String> getSearchHistory() {

        SPUtils shareDefault = CacheUtil.getShareDefault();
        String history = shareDefault.getString(SPKey.K_SEARCH_HISTORY, "");

        ArrayList<String> strings = new ArrayList<>();

        if (history.contains(",")) {

            String[] split = history.split(",");
            strings.addAll(Arrays.asList(split));
        }
        return strings;
    }

    public static void clearSearchHistory() {
        CacheUtil.getShareDefault().put(SPKey.K_SEARCH_HISTORY, "");
    }

    public static String getNoWrapString(String old) {

        return old.replaceAll("\n", " ");
    }


    public static void startNaviGoogle(Context context, Float lat, Float lng) {

        if (isAvilible(context, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } else {
            ToastUtils.showShort(R.string.no_install_google_map_prompt);
        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    //假设拍照片保存在DCIM公共目录,为了查看方便，尽可能的将逻辑写在一个方法里
    public static Uri takePicture(Activity activity, int requestCode) {

        Uri mImageUri;

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String name = System.currentTimeMillis() + ".jpg";
        File file = new File(path, name);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        /**
         *  主要适配的点在mImageUri赋值这里
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android Q得用MediaStore先存一下
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, name);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            mImageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Android N版本需要用FileProvider
            mImageUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + "fileProvider", file);
        } else {
            // 古老的版本用这个
            mImageUri = Uri.fromFile(file);
        }
        // 指定图片保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        activity.startActivityForResult(intent, requestCode);

        return mImageUri;
    }

}
