package com.jcs.where.features.splash;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.currency.WebViewActivity;
import com.jcs.where.features.main.MainActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.widget.pager.IndicatorView;
import com.mob.MobSDK;

/**
 * Created by Wangsw  2021/3/18 15:36.
 */
public class SplashActivity extends BaseMvpActivity<SplashPresenter> implements SplashView {

    private RelativeLayout splashContainerRl;
    private ImageView firstIv;
    private ViewPager pagerVp;
    private IndicatorView pointView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        // 状态栏透明
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT, true);
        BarUtils.setStatusBarLightMode(this, false);
        BarUtils.subtractMarginTopEqualStatusBarHeight(findViewById(android.R.id.content));
        BarUtils.setNavBarVisibility(this, false);


        if (!isTaskRoot()) {
            finish();
            return;
        }
        splashContainerRl = findViewById(R.id.splash_container_rl);
        firstIv = findViewById(R.id.first_iv);
        pagerVp = findViewById(R.id.pager_vp);
        pointView = findViewById(R.id.point_view);


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
                initUserAgreement();


            }
        });

        splashContainerRl.startAnimation(animation);
    }

    private void initUserAgreement() {

        SpanUtils spanUtils = new SpanUtils();
        SpannableStringBuilder builder = spanUtils.append(getString(R.string.use_agreement_content_0))
                .append(getString(R.string.use_agreement_content_1))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.goTo(SplashActivity.this, FeaturesUtil.getUserAgreement());
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(getColor(R.color.blue_4C9EF2));
                        ds.setUnderlineText(true);
                    }
                })
                .append(getString(R.string.use_agreement_content_2))
                .append(getString(R.string.use_agreement_content_3))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.goTo(SplashActivity.this, FeaturesUtil.getPrivacyPolicy());
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(getColor(R.color.blue_4C9EF2));
                        ds.setUnderlineText(true);
                    }
                })
                .append(getString(R.string.use_agreement_content_4))
                .append(getString(R.string.use_agreement_content_5))
                .append(getString(R.string.use_agreement_content_6))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.goTo(SplashActivity.this, "https://www.mob.com/about/policy");
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(getColor(R.color.blue_4C9EF2));
                        ds.setUnderlineText(true);
                    }
                })
                .append(getString(R.string.use_agreement_content_8))
                .append(getString(R.string.use_agreement_content_9))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.goTo(SplashActivity.this, "https://www.comm100.com/platform/security/");
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(getColor(R.color.blue_4C9EF2));
                        ds.setUnderlineText(true);
                    }
                })
                .append(getString(R.string.use_agreement_content_10))
                .append(getString(R.string.use_agreement_content_11))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.goTo(SplashActivity.this, "https://www.jiguang.cn/license/privacy");
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(getColor(R.color.blue_4C9EF2));
                        ds.setUnderlineText(true);
                    }
                })
                .append(getString(R.string.use_agreement_content_7))
                .create();


        boolean isAgreeUserAgreement = CacheUtil.getShareDefault().getBoolean(Constant.SP_IS_AGREE_USER_AGREEMENT, false);
        if (isAgreeUserAgreement) {
            afterAnimation();
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.tips_0)
                    .setCancelable(false)
                    .setMessage(builder)
                    .setPositiveButton(R.string.agree_and_continue, (dialog, which) -> {
                        afterAnimation();
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.disagree, (dialog, which) -> {
                        handleDisagree();
                        dialog.dismiss();
                    })
                    .create();
            alertDialog.show();


            // 富文本可点击
            TextView message = alertDialog.findViewById(android.R.id.message);
            if (message != null) {
                message.setMovementMethod(LinkMovementMethod.getInstance());
                message.setLineSpacing(0f, 1.2f);
            }
        }

    }

    private void handleDisagree() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tips_1)
                .setCancelable(false)
                .setMessage(getString(R.string.disagree_privacy_policy_message))
                .setPositiveButton(getString(R.string.goto_agree), (dialog, which) -> {
                    initUserAgreement();
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.not_yet), (dialog, which) -> {
                    dialog.dismiss();
                    AppUtils.exitApp();
                })
                .create()
                .show();
    }

    private void afterAnimation() {
        CacheUtil.getShareDefault().put(Constant.SP_IS_AGREE_USER_AGREEMENT, true);
        MobSDK.submitPolicyGrantResult(true, null);
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

    private void toHome() {
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    protected void initData() {
        presenter = new SplashPresenter(this);
        String cityId = SPUtil.getInstance().getString(SPKey.SELECT_AREA_ID);
        if (cityId.isEmpty()) {
            // 默认巴郎牙
            SPUtil.getInstance().saveString(SPKey.SELECT_AREA_ID, "3");
        }
        presenter.getYellowPageAllCategories();
    }

    @Override
    protected void bindListener() {

        pagerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pointView.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
