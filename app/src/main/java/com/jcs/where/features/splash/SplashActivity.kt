package com.jcs.where.features.splash

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import bolts.AppLinks
import cn.jiguang.api.utils.JCollectionAuth
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SpanUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.main.MainActivity
import com.jcs.where.features.web.WebViewActivity
import com.jcs.where.utils.*
import com.jcs.where.widget.pager.OnIndicatorClickListener
import com.mob.MobSDK
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by Wangsw  2021/3/18 15:36.
 *
 * Facebook 深度链接 App Link 打开app
 * https://github.com/BoltsFramework/Bolts-Android?fbclid=IwAR2UEuo8f3OCQHvVnWtzbkgI-SpiacW6Dt24ZVHPPXaNVTUodH4tstKvpmA
 */
class SplashActivity : BaseMvpActivity<SplashPresenter>(), SplashView {


    private var facebookIntent: Intent? = null

    override fun getLayoutId() = R.layout.activity_splash

    override fun initView() {


        // 状态栏透明
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT, true)
        BarUtils.setStatusBarLightMode(this, false)
        BarUtils.subtractMarginTopEqualStatusBarHeight(findViewById(android.R.id.content))
        BarUtils.setNavBarVisibility(this, false)


        handleWebOpen()

        if (!isTaskRoot) {
            // 栈底为MainActivity ，如为facebook启动，打开目标页面
            if (facebookIntent != null) {
                startActivity(facebookIntent)
            }
            finish()
            return
        }
        point_view.apply {
            commonDrawableResId = R.drawable.shape_point_normal_e7e7e7
            selectedDrawableResId = R.drawable.shape_point_selected_377bff
            setPointCount(4, 15, 15, 15)
            onClickListener = object : OnIndicatorClickListener {
                override fun onIndicatorClick(index: Int) {
                    pager_vp.setCurrentItem(index, true)
                }
            }
        }
        initPager()
        initAnimation()
    }

    private fun handleWebOpen() {

        // 处理 Facebook DeepAppLink
        val targetUrl: Uri? = AppLinks.getTargetUrlFromInboundIntent(this, intent)
        if (targetUrl != null) {
            // 被app link启动

            Log.d("AppLink", "targetUrl == $targetUrl")
            val module = targetUrl.getQueryParameter("module")
            val moduleId = targetUrl.getQueryParameter("id")

            if (!module.isNullOrBlank()) {

                handleFaceBookIntent(module, moduleId)
            }
        } else {

            // 不是 applink启动. 获取邀请码
            if (intent.hasCategory(Intent.CATEGORY_BROWSABLE) || intent.hasCategory(Intent.CATEGORY_LAUNCHER)) {

                val uri = intent.data ?: return

                val whereCode = uri.getQueryParameter("whereCode")
                if (whereCode.isNullOrBlank()) {

                    // 参数为空，读取剪切板中的内容
                    Handler(Looper.myLooper()!!).postDelayed({
                        presenter.handleClipboard()
                    }, 1000)
                } else {
                    SPUtils.getInstance().put(SPKey.K_INVITE_CODE, whereCode)
                }
            }

        }


    }


    private fun initPager() {
        val adapter = SplashAdapter()
        pager_vp.adapter = adapter
        pager_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                if (position < 3) {
                    start_tv.text = getString(R.string.splash_next)
                } else {
                    start_tv.text = getString(R.string.start_now)
                }

                point_view.onPageSelected(position)

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
                override fun onClick(widget: View) = WebViewActivity.navigation(this@SplashActivity, FeaturesUtil.getUserAgreement())
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            })
            .append(getString(R.string.use_agreement_content_2))
            .append(getString(R.string.use_agreement_content_3))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) = WebViewActivity.navigation(this@SplashActivity, FeaturesUtil.getPrivacyPolicy())

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
                    WebViewActivity.navigation(this@SplashActivity, "https://www.mob.com/about/policy")
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
                    WebViewActivity.navigation(this@SplashActivity, "https://www.comm100.com/platform/security/")
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
                    WebViewActivity.navigation(this@SplashActivity, "https://www.jiguang.cn/license/privacy")
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            })

            .append(getString(R.string.use_agreement_content_12))
            .append(getString(R.string.use_agreement_content_13))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebViewActivity.navigation(this@SplashActivity, "https://www.umeng.com/page/policy")
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = true
                }
            })
            .append(getString(R.string.use_agreement_content_last))
            .create()
        val isAgreeUserAgreement = CacheUtil.isAgreeUserAgreement()
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
                message.textSize = 14f
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

                // 友盟隐私合规授权结果上传
//                UMConfigure.submitPolicyGrantResult(applicationContext, false);

                // 极光推送隐私确认
                JCollectionAuth.setAuth(this, false)
                dialog.dismiss()
                AppUtils.exitApp()
            }
            .create()
            .show()
    }

    private fun afterAnimation() {
        CacheUtil.putIsAgreeUserAgreement(true);
        // mob share sdk
        MobSDK.submitPolicyGrantResult(true)

        // 友盟隐私合规授权结果上传
//        UMConfigure.submitPolicyGrantResult(applicationContext, true)
        // 友盟真正注册
//        UMConfigure.init(this, BuildConfig.UMENG_APP_KEY, BusinessUtils.getUmengAppChannel(), UMConfigure.DEVICE_TYPE_PHONE, "")

        // 极光推送隐私确认
        JCollectionAuth.setAuth(this, true)
        JPushInterface.init(this)


        val isFirstOpen = CacheUtil.getShareDefault().getBoolean(Constant.SP_IS_FIRST_OPEN, true)
        if (isFirstOpen) {
            pager_vp.visibility = View.VISIBLE
            point_view.visibility = View.VISIBLE
            start_tv.visibility = View.VISIBLE
            first_iv.visibility = View.GONE
            CacheUtil.getShareDefault().put(Constant.SP_IS_FIRST_OPEN, false)
        } else {
            toHome()
        }
    }

    private fun toHome() {
        startActivity(MainActivity::class.java)
        facebookIntent?.let {
            startActivity(facebookIntent)
        }
        finish()
    }

    override fun initData() {
        presenter = SplashPresenter(this)
        val cityId = SPUtil.getInstance().getString(SPKey.SELECT_AREA_ID)
        if (cityId.isEmpty()) {
            // 默认巴郎牙
            SPUtil.getInstance().saveString(SPKey.SELECT_AREA_ID, "3")
        }
        presenter.yellowPageAllCategories
    }

    override fun bindListener() {
        start_tv.setOnClickListener {
            val currentItem = pager_vp.currentItem
            if (currentItem < 3) {
                pager_vp.currentItem = currentItem + 1
            } else {
                toHome()
            }
        }
    }

    /**
     *   通过Facebook 深度链接打开 ，处理目标页面
     */
    private fun handleFaceBookIntent(module: String?, moduleId: String?) {
        if (module.isNullOrBlank() || moduleId.isNullOrBlank()) {
            return
        }
        facebookIntent = BusinessUtils.getDeepLinksTargetIntent(module, moduleId, this)
    }


}