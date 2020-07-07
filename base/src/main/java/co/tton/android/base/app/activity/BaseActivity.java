package co.tton.android.base.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.R;
import co.tton.android.base.app.presenter.BaseActivityPresenter;
import co.tton.android.base.app.presenter.linker.ActivityLinker;
import co.tton.android.base.dialog.CustomProgressDialog;
import co.tton.android.base.manager.CompositeSubscriptionHelper;
import co.tton.android.base.utils.V;
import co.tton.android.base.utils.ValueUtils;
import rx.Subscription;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    private TextView mTitleTv;

    private boolean mDestroyed;

    private ActivityLinker mLinker = new ActivityLinker();

    private CompositeSubscriptionHelper mCompositeSubscriptionHelper;

    private boolean mIsHasStatusBarColor = true;

    public static List<Activity> sActivityList = new ArrayList<>();


    public static CustomProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mLinker.register(this);
        mLinker.onCreate(savedInstanceState);

        mCompositeSubscriptionHelper = CompositeSubscriptionHelper.newInstance();

        if (mIsHasStatusBarColor) {
            StatusBarUtil.setColor(this, ValueUtils.getColor(this, R.color.colorPrimary), 0);
        }

        initToolbar();
        sActivityList.add(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLinker.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLinker.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLinker.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLinker.onStop();
    }

    @Override
    protected void onDestroy() {
        mDestroyed = true;
        super.onDestroy();
        mLinker.onDestroy();
        mCompositeSubscriptionHelper.unsubscribe();
        sActivityList.remove(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLinker.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            if (mLinker.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLinker.onActivityResult(requestCode, resultCode, data);
    }

    private void initToolbar() {
        mToolbar = V.f(this, R.id.toolbar);
        if (mToolbar != null) {
            mTitleTv = V.f(mToolbar, R.id.tv_toolbar_title);
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }

    public boolean isDestroyed() {
        return mDestroyed || isFinishing();
    }

    protected abstract int getLayoutId();

    protected void setIsHasStatusBarColor(boolean b) {
        mIsHasStatusBarColor = b;
    }

    public void addPresenter(BaseActivityPresenter presenter) {
        mLinker.addActivityCallbacks(presenter);
    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscriptionHelper.addSubscription(subscription);
    }

    public static boolean isAvailable(BaseActivity activity) {
        return activity != null && !activity.isDestroyed() && !activity.isFinishing();
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


    public void showLoading(String msg) {
        if (dialog != null && dialog.isShowing()) {
        } else {
            dialog = new CustomProgressDialog(this, msg);
//            dialog.setCancelable(isCancelable);
            dialog.show();
        }
    }

    public void showLoading() {
        if (dialog != null && dialog.isShowing()) {
        } else {
            dialog = new CustomProgressDialog(this, "");
//            dialog.setCancelable(isCancelable);
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

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if ((dialog != null && dialog.isShowing())) {
            this.finish();
        } else {
            this.finish();
            overridePendingTransition(R.anim.out_from_right, R.anim.in_from_left);
        }
    }
}