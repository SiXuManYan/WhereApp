package com.jcs.where.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.utils.LocalLanguageUtil;
import com.jcs.where.utils.ToastUtils;
import com.jcs.where.widget.JcsTitle;

public abstract class BaseActivity extends AppCompatActivity {


    protected JcsTitle mJcsTitle;
    private boolean mIsHasStatusBarColor = true;
    private CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mIsHasStatusBarColor) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        }
        fullScreen(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(getLayoutId());
        mJcsTitle = findViewById(R.id.jcsTitle);
        if (mJcsTitle != null) {
            setMarginTopForStatusBar(mJcsTitle);
            mJcsTitle.setBackIvClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    afterJcsBack();
                }
            });
        }
        initView();
        bindListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeStatusTextColor();
    }

    private void changeStatusTextColor() {
        View decor = getWindow().getDecorView();
        if (isStatusDark()) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void bindListener();

    protected abstract int getLayoutId();

    protected boolean hasJcsBack() {
        return true;
    }

    protected void afterJcsBack() {

    }

    protected void toActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    protected void toActivity(Class<?> clazz, IntentEntry... entrys) {
        Intent intent = new Intent(this, clazz);
        int length = entrys.length;
        for (int i = 0; i < length; i++) {
            IntentEntry entry = entrys[i];
            intent.putExtra(entry.key, entry.value);
        }
        startActivity(intent);
    }

    protected void setIsHasStatusBarColor(boolean b) {
        mIsHasStatusBarColor = b;
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public void fullScreen(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏为透明，否则在部分手机上会呈现系统默认的浅灰色
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以考虑设置为透明色
                //window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bg_color));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }


    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public void setMarginTopForStatusBar(View view) {
        setMargins(view, 0, getStatusBarHeight(), 0, 0);
    }

    protected void setAndroidNativeLightStatusBar(boolean dark) {

    }

    protected boolean isStatusDark() {
        return false;
    }

    protected void showNetError(ErrorResponse errorResponse) {
//        ToastUtils.showLong(this, getClass().getSimpleName() + ":" + errorResponse.getErrMsg());
    }

    public void showLoading(String msg) {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new CustomProgressDialog(this, msg);
            mProgressDialog.show();
        }
    }

    public void showLoading() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new CustomProgressDialog(this, "");
            mProgressDialog.show();
        }
    }

    public void stopLoading() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }

        } catch (Exception e) {

        }
    }

    protected void showToast(String msg) {
        ToastUtils.showLong(this, msg);
    }

    public void showComing(){
        showToast(getString(R.string.coming_soon));
    }

    protected int getPxFromDp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalLanguageUtil.getInstance().setLocal(newBase));
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //宽度 dm.widthPixels
        //高度 dm.heightPixels
        return dm.widthPixels;
    }



}
