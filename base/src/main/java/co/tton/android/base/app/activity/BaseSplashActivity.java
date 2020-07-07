package co.tton.android.base.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import co.tton.android.base.R;

public abstract class BaseSplashActivity extends BaseActivity {

    protected MyHandler mHandler;

    protected static class MyHandler extends Handler {
        private WeakReference<BaseSplashActivity> mActivityRef;

        public MyHandler(BaseSplashActivity activity) {
            mActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseSplashActivity activity = mActivityRef.get();
            if (activity != null) {
                activity.goToNext();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setIsHasStatusBarColor(false);
        setTheme(R.style.FullScreenTheme);
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(this);
        mHandler.sendEmptyMessageDelayed(0, getDelayMillis());
    }

    protected long getDelayMillis() {
        return 3 * 1000; // 默认停留3秒
    }

    protected abstract void goToNext();

    @Override
    public void onBackPressed() {
        // do nothing
    }
}