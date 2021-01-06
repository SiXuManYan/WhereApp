package com.jcs.where.flash.activity;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.home.HomeActivity;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

/**
 * 闪屏页
 * create by zyf on 2021/1/6 10:17 上午
 */
public class FlashActivity extends BaseActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String cityId = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID);
        if (cityId.isEmpty()) {
            // 默认巴郎牙
            SPUtil.getInstance().saveString(SPKey.K_CURRENT_AREA_ID, "3");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    toHomeActivity();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    toHomeActivity();
                }
            }
        }).start();
    }

    private void toHomeActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toActivity(HomeActivity.class);
                finish();
            }
        });
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flash;
    }
}
