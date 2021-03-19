package com.jcs.where.features.splash;

import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.home.HomeActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.widget.pager.CirclePoint;
import com.mob.MobSDK;

/**
 * Created by Wangsw  2021/3/18 15:36.
 */
public class SplashActivity extends BaseMvpActivity<SplashPresenter> implements SplashView {

    private RelativeLayout splashContainerRl;
    private ImageView firstIv;
    private ViewPager pagerVp;
    private CirclePoint pointView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        splashContainerRl = findViewById(R.id.splash_container_rl);
        firstIv = findViewById(R.id.first_iv);
        pagerVp = findViewById(R.id.pager_vp);
        pointView = findViewById(R.id.point_view);

        // 状态栏透明
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT, true);
        BarUtils.setStatusBarLightMode(this, false);
        BarUtils.subtractMarginTopEqualStatusBarHeight(findViewById(android.R.id.content));
        BarUtils.setNavBarVisibility(this, false);

        initPager();

        initAnimation();
    }

    private void initPager() {
        SplashAdapter adapter = new SplashAdapter();
        adapter.onStartClickListener = v -> toHome();
        pagerVp.setAdapter(adapter);
    }

    private void initAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isFirstOpen = CacheUtil.getShareDefault().getBoolean(Constant.SP_IS_FIRST_OPEN, true);
                if (isFirstOpen) {
                    pagerVp.setVisibility(View.VISIBLE);
                    pointView.setVisibility(View.VISIBLE);
                    firstIv.setVisibility(View.GONE);
                    CacheUtil.getShareDefault().put(Constant.SP_IS_FIRST_OPEN, false);
                } else {
                    toHome();
                }



            }
        });

        splashContainerRl.startAnimation(animation);
    }

    private void toHome() {
        toActivity(HomeActivity.class);
        finish();
    }

    @Override
    protected void initData() {
        presenter = new SplashPresenter(this);
        String cityId = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID);
        if (cityId.isEmpty()) {
            // 默认巴郎牙
            SPUtil.getInstance().saveString(SPKey.K_CURRENT_AREA_ID, "3");
        }
        presenter.getYellowPageAllCategories();
    }

    @Override
    protected void bindListener() {
        MobSDK.submitPolicyGrantResult(true, null);
        pagerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pointView.setonPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
