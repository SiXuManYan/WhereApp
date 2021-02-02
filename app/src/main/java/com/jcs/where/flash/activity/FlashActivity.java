package com.jcs.where.flash.activity;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.flash.model.FlashModel;
import com.jcs.where.home.HomeActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.yellow_page.model.YellowPageModel;
import com.mob.MobSDK;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 闪屏页
 * create by zyf on 2021/1/6 10:17 上午
 */
public class FlashActivity extends BaseActivity {
    private boolean isAlive = true;
    private FlashModel mModel;

    @Override
    protected void initView() {
        MobSDK.submitPolicyGrantResult(true,null);
    }

    @Override
    protected void initData() {
        mModel = new FlashModel();
        String cityId = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID);
        if (cityId.isEmpty()) {
            // 默认巴郎牙
            SPUtil.getInstance().saveString(SPKey.K_CURRENT_AREA_ID, "3");
        }

        mModel.getYellowPageAllCategories(new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {

            }

            @Override
            public void onSuccess(@NotNull List<CategoryResponse> categoryResponses) {
                if (CacheUtil.needUpdateBySpKey(SPKey.K_YELLOW_PAGE_CATEGORIES).equals("") && categoryResponses.size() > 0) {
                    CacheUtil.cacheWithCurrentTime(SPKey.K_YELLOW_PAGE_CATEGORIES, categoryResponses);
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    try {
                        if (isAlive) {
                            Thread.sleep(50);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        toHomeActivity();
                    }
                }
                toHomeActivity();
            }
        }).start();
    }

    private void toHomeActivity() {
        if (isAlive) {
            runOnUiThread(() -> {
                toActivity(HomeActivity.class);
                finish();
            });
        }
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flash;
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        super.onDestroy();
    }
}
