package com.jcs.where.features.splash

import android.content.DialogInterface
import android.graphics.Color
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SpanUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.currency.WebViewActivity
import com.jcs.where.features.main.MainActivity
import com.jcs.where.utils.*
import com.mob.MobSDK
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by Wangsw  2021/3/18 15:36.
 */
class SplashActivity : BaseMvpActivity<SplashPresenter>(), SplashView {

    override fun getLayoutId() = R.layout.activity_splash

    override fun initView() {

        // 状态栏透明
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT, true)
        BarUtils.setStatusBarLightMode(this, false)
        BarUtils.subtractMarginTopEqualStatusBarHeight(findViewById(android.R.id.content))
        BarUtils.setNavBarVisibility(this, false)
        if (!isTaskRoot) {
            finish()
            return
        }
        point_view.apply {
            commonDrawableResId = R.drawable.shape_point_normal_e7e7e7
            selectedDrawableResId = R.drawable.shape_point_selected_377bff
            setPointCount(4)
        }
        initPager()
        initAnimation()
    }

    private fun initPager() {
        val adapter = SplashAdapter()
        pager_vp.adapter = adapter
        pager_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                point_view.onPageSelected(position)
                if (position == 3) {
                    start_tv.visibility = View.VISIBLE
                } else {
                    start_tv.visibility = View.GONE
                }
            }
        })
    }

    private fun initAnimation() {
        val animation = AlphaAnimation(0f, 1f)
        animation.duration = 1000
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) = Unit
            override fun onAnimationRepeat(animation: Animation) = Unit
            override fun onAnimationEnd(animation: Animation) = initUserAgreement()
        })
        splash_container_rl.startAnimation(animation)
    }

    private fun initUserAgreement() {
        val spanUtils = SpanUtils()
        val builder = spanUtils.append(getString(R.string.use_agreement_content_0))
            .append(getString(R.string.use_agreement_content_1))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) = WebViewActivity.goTo(this@SplashActivity, FeaturesUtil.getUserAgreement())
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            })
            .append(getString(R.string.use_agreement_content_2))
            .append(getString(R.string.use_agreement_content_3))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) = WebViewActivity.goTo(this@SplashActivity, FeaturesUtil.getPrivacyPolicy())

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            })
            .append(getString(R.string.use_agreement_content_4))
            .append(getString(R.string.use_agreement_content_5))
            .append(getString(R.string.use_agreement_content_6))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebViewActivity.goTo(this@SplashActivity, "https://www.mob.com/about/policy")
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            })
            .append(getString(R.string.use_agreement_content_8))
            .append(getString(R.string.use_agreement_content_9))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebViewActivity.goTo(this@SplashActivity, "https://www.comm100.com/platform/security/")
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            })
            .append(getString(R.string.use_agreement_content_10))
            .append(getString(R.string.use_agreement_content_11))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebViewActivity.goTo(this@SplashActivity, "https://www.jiguang.cn/license/privacy")
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            })
            .append(getString(R.string.use_agreement_content_7))
            .create()
        val isAgreeUserAgreement = CacheUtil.getShareDefault().getBoolean(Constant.SP_IS_AGREE_USER_AGREEMENT, false)
        if (isAgreeUserAgreement) {
            afterAnimation()
        } else {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.tips_0)
                .setCancelable(false)
                .setMessage(builder)
                .setPositiveButton(R.string.agree_and_continue) { dialog: DialogInterface, which: Int ->
                    afterAnimation()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.disagree) { dialog: DialogInterface, which: Int ->
                    handleDisagree()
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()


            // 富文本可点击
            val message = alertDialog.findViewById<TextView>(android.R.id.message)
            if (message != null) {
                message.movementMethod = LinkMovementMethod.getInstance()
                message.setLineSpacing(0f, 1.2f)
            }
        }
    }

    private fun handleDisagree() {
        AlertDialog.Builder(this)
            .setTitle(R.string.tips_1)
            .setCancelable(false)
            .setMessage(getString(R.string.disagree_privacy_policy_message))
            .setPositiveButton(getString(R.string.goto_agree)) { dialog: DialogInterface, which: Int ->
                initUserAgreement()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.not_yet)) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                AppUtils.exitApp()
            }
            .create()
            .show()
    }

    private fun afterAnimation() {
        CacheUtil.getShareDefault().put(Constant.SP_IS_AGREE_USER_AGREEMENT, true)
        MobSDK.submitPolicyGrantResult(true, null)
        val isFirstOpen = CacheUtil.getShareDefault().getBoolean(Constant.SP_IS_FIRST_OPEN, true)
        if (isFirstOpen) {
            pager_vp.visibility = View.VISIBLE
            point_view.visibility = View.VISIBLE
            first_iv.visibility = View.GONE
            CacheUtil.getShareDefault().put(Constant.SP_IS_FIRST_OPEN, false)
        } else {
            toHome()
        }
    }

    private fun toHome() {
        startActivity(MainActivity::class.java)
        finish()
    }

    override fun initData() {
        presenter = SplashPresenter(this)
        val cityId = SPUtil.getInstance().getString(SPKey.SELECT_AREA_ID)
        if (cityId.isEmpty()) {
            // 默认巴郎牙
            SPUtil.getInstance().saveString(SPKey.SELECT_AREA_ID, "3")
        }
        presenter.getYellowPageAllCategories()
    }

    override fun bindListener() {
        start_tv.setOnClickListener { toHome() }
    }
}