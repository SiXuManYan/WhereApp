package com.jcs.where.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.dialog.LoadingView;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.view.empty.EmptyView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class BaseFragment extends Fragment {

    protected boolean isViewCreated = false;
    protected boolean isViewVisible = false;
    protected boolean hasLoad = false;
    protected ArrayList<EmptyView> emptyViewList = new ArrayList<>();
    private LoadingView loadingDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        initView(view);
        initData();
        bindListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        changeStatusTextColor();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        changeStatusTextColor();
        isViewVisible = isVisibleToUser;

        lazyLoad();

    }

    private void lazyLoad() {
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

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract void bindListener();


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
        ToastUtils.showShort(msg);
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

    protected final void startActivityAfterLogin(@NotNull Class<?> target) {
        String token = CacheUtil.getToken();
        if (TextUtils.isEmpty(token)) {
            startActivity(LoginActivity.class);
        } else {
            toActivity(target);
        }
    }

    protected final void startActivityAfterLogin(@NotNull Class<?> target, Bundle bundle) {
        String token = CacheUtil.getToken();
        if (TextUtils.isEmpty(token)) {
            startActivity(LoginActivity.class);
        } else {
            startActivity(target, bundle);
        }
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
            bundle.putString(key, (String) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, ((Number) value).longValue());
        } else if (value instanceof Integer) {
            bundle.putInt(key, ((Number) value).intValue());
        }

        startActivity(target, bundle);
    }

    protected final void startActivityForResult(@NotNull Class<?> target, int requestCode, @Nullable Bundle bundle) {
        if (bundle == null) {
            startActivityForResult(new Intent(requireContext(), target), requestCode);
        } else {
            startActivityForResult((new Intent(requireContext(), target)).putExtras(bundle), requestCode);
        }

    }

    protected void addEmptyList(EmptyView view) {
        this.emptyViewList.add(view);
    }

    protected void showLoadingDialog() {

        try {
            if (loadingDialog == null) {
                loadingDialog = new LoadingView.Builder(getContext()).setCancelable(true).create();
            }

            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }
        } catch (Exception e) {

        }


    }


    protected void showLoadingDialog(boolean cancelable) {
        try {
            if (loadingDialog == null) {
                loadingDialog = new LoadingView.Builder(getContext()).setCancelable(cancelable).create();
            }
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }
        } catch (Exception ignored) {

        }

    }

    protected void dismissLoadingDialog() {
        try {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }


}
