package com.jcs.where.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

public abstract class BaseFragment extends Fragment {

    public CustomProgressDialog dialog;


    protected boolean isViewCreated = false;
    protected boolean isViewVisible = false;
    protected boolean hasLoad = false;

    public void setMargins(View v, int left, int top, int right, int bottom, int barColor) {
        setMargins(v, left, top, right, bottom);
        if (barColor != 0) {

            ConstraintLayout parent = (ConstraintLayout) v.getParent();
            View bar = new View(getContext());
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, top);
            bar.setBackgroundResource(barColor);
            lp.topToTop = parent.getTop();
            bar.setLayoutParams(lp);
            parent.addView(bar);
        }
    }

    public void setMargins(View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        bindListener();
        isViewCreated = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        changeStatusTextColor();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        changeStatusTextColor();
        isViewVisible = isVisibleToUser;

        if (!hasLoad && isViewVisible && isViewCreated) {
            hasLoad = true;
            loadOnVisible();
        }

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        changeStatusTextColor();
    }

    private void changeStatusTextColor() {
        setAndroidNativeLightStatusBar(getActivity(), isStatusDark());
    }

    protected boolean isStatusDark() {
        return false;
    }

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract void bindListener();

    protected abstract int getLayoutId();

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
    public void fullScreen(Activity activity) {
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


    public void showLoading(String msg) {
        if (dialog != null && dialog.isShowing()) {
        } else {
            dialog = new CustomProgressDialog(getContext(), msg);
            dialog.show();
        }
    }

    public void showLoading() {
        if (dialog != null && dialog.isShowing()) {
        } else {
            dialog = new CustomProgressDialog(getContext(), "");
            dialog.show();
        }
    }

    public void stopLoading() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

        } catch (Exception e) {

        }
    }

    protected void toActivity(Class<?> clazz) {
        startActivity(new Intent(getContext(), clazz));
    }

    protected void toActivity(Class<?> clazz, IntentEntry... entrys) {
        Intent intent = new Intent(getContext(), clazz);
        int length = entrys.length;
        for (int i = 0; i < length; i++) {
            IntentEntry entry = entrys[i];
            intent.putExtra(entry.key, entry.value);
        }
        startActivity(intent);
    }

    protected void showToast(String msg) {
        ToastUtils.showLong(getContext(), msg);
    }

    public void showComing() {
        showToast(getString(R.string.coming_soon));
    }

    protected void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        if (needChangeStatusBarStatus()) {
            View decor = activity.getWindow().getDecorView();
            if (dark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    protected boolean needChangeStatusBarStatus() {
        return false;
    }

    protected void showNetError(ErrorResponse errorResponse) {
        ToastUtils.showLong(getContext(), getClass().getSimpleName() + ":" + errorResponse.getErrMsg());
    }

    protected int getPxFromDp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    /**
     * 延时加载
     */
    protected void loadOnVisible() {

    }


    protected final void startActivity(@NotNull Class<?> target) {
        startActivity(new Intent(requireContext(), target));
    }

    protected final void startActivity(@NotNull Class<?> target, @NotNull Bundle bundle) {
        startActivity((new Intent(requireContext(), target)).putExtras(bundle));
    }


    protected final void startActivityClearTop(@NotNull Class<?> target, @Nullable Bundle bundle) {
        if (bundle == null) {
            startActivity((new Intent(requireContext(), target)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            startActivity((new Intent(requireContext(), target)).putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

    }

    protected final void startActivityWithArgument(@NotNull Class<?> target, @NotNull String key, @NotNull Object value) {
        Bundle bundle = new Bundle();
        if (value instanceof String) {
            bundle.putString(key, (String)value);
        } else if (value instanceof Long) {
            bundle.putLong(key, ((Number)value).longValue());
        } else if (value instanceof Integer) {
            bundle.putInt(key, ((Number)value).intValue());
        }

        startActivity(target, bundle);
    }

    protected final void startActivityForResult(@NotNull Class<?>  target, int requestCode, @Nullable Bundle bundle) {
        if (bundle == null) {
            startActivityForResult(new Intent(requireContext(), target), requestCode);
        } else {
            startActivityForResult((new Intent(requireContext(), target)).putExtras(bundle), requestCode);
        }

    }

}
