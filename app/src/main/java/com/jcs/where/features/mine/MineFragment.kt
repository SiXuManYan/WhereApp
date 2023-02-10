package com.jcs.where.features.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.UserInfoResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.address.AddressActivity
import com.jcs.where.features.collection.CollectionActivity
import com.jcs.where.features.com100.ExtendChatActivity
import com.jcs.where.features.coupon.user.MyCouponActivity
import com.jcs.where.features.daily.scoe.ScoreActivity
import com.jcs.where.features.daily.sign.SignInActivity
import com.jcs.where.features.feedback.home.FeedbackHomeActivity
import com.jcs.where.features.footprint.FootprintActivity
import com.jcs.where.features.integral.activitys.ActivityCenterActivity
import com.jcs.where.features.job.employer.EmployerActivity
import com.jcs.where.features.merchant.MerchantSettledActivity
import com.jcs.where.features.message.MessageCenterActivity
import com.jcs.where.features.setting.SettingActivity
import com.jcs.where.features.setting.information.ModifyInfoActivity
import com.jcs.where.mine.about.AboutActivity
import com.jcs.where.mine.activity.LanguageActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocalLanguageUtil
import com.jcs.where.utils.MobUtil
import com.jcs.where.utils.SPKey
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.utils.zxing.ZxingUtil
import kotlinx.android.synthetic.main.fragment_mine_2.*

/**
 * Created by Wangsw  2021/8/13 14:14.
 * 用户页
 */
class MineFragment : BaseMvpFragment<MinePresenter>(), MineView {

    /** 雇主申请状态 */
    private var isSendEmployer = false

    private lateinit var qrBgView: LinearLayout

    private var degreeDialog: BottomSheetDialog? = null

    override fun getLayoutId() = R.layout.fragment_mine_2

    override fun isStatusDark() = true

    override fun needChangeStatusBarStatus() = true

    override fun initView(view: View) {
        BarUtils.addMarginTopEqualStatusBarHeight(top_ll)
        val topPadding = BarUtils.getStatusBarHeight() + SizeUtils.dp2px(40f)
        content_ll.setPaddingRelative(0, topPadding, 0, 0)

        initDefaultUi()
        qrBgView = LayoutInflater.from(requireContext()).inflate(R.layout.view_qr, null) as LinearLayout

    }

    override fun initData() {
        presenter = MinePresenter(this)
    }


    override fun loadOnVisible() {
        presenter.getUserInfo()
        presenter.getUnreadMessageCount()
    }

    override fun bindListener() {

        setting_iv.setOnClickListener {
            startActivityAfterLogin(SettingActivity::class.java)
        }
        message_view.setOnClickListener {
            startActivityAfterLogin(MessageCenterActivity::class.java)
        }
        user_info_rl.setOnClickListener {
            startActivityAfterLogin(ModifyInfoActivity::class.java)
        }

        integral_iv.setOnClickListener {
            startActivityAfterLogin(SignInActivity::class.java)
        }
        integral_tv.setOnClickListener {
            startActivityAfterLogin(ScoreActivity::class.java)
        }
        collect_tv.setOnClickListener {
            startActivityAfterLogin(CollectionActivity::class.java)
        }
        viewed_tv.setOnClickListener {
            startActivityAfterLogin(FootprintActivity::class.java)
        }
        vouchers_tv.setOnClickListener {
            startActivityAfterLogin(MyCouponActivity::class.java)
        }
        merchant_rl.setOnClickListener {
            startActivityAfterLogin(MerchantSettledActivity::class.java)

        }
        language_rl.setOnClickListener {
            startActivity(LanguageActivity::class.java)
        }
        address_rl.setOnClickListener {
            startActivityAfterLogin(AddressActivity::class.java)
        }

        service_rl.setOnClickListener {
            startActivityAfterLogin(ExtendChatActivity::class.java)
        }
        about_rl.setOnClickListener {
            startActivity(AboutActivity::class.java)
        }
        share_rl.setOnClickListener {
            if (!User.isLogon()) {
                startActivity(LoginActivity::class.java)
                return@setOnClickListener
            }
            val inviteLink = SPUtils.getInstance().getString(SPKey.K_INVITE_LINK, "")
//



            showShareDialog(inviteLink)
        }

        activity_center_iv.setOnClickListener {
            startActivityAfterLogin(ActivityCenterActivity::class.java)
        }

        feedback_rl.setOnClickListener {
            startActivityAfterLogin(FeedbackHomeActivity::class.java)
        }

        employer_rl.setOnClickListener {
            startActivityAfterLogin(EmployerActivity::class.java, Bundle().apply {
                putBoolean(Constant.PARAM_STATUS, isSendEmployer)
            })
        }

    }

    override fun bindUnreadMessageCount(totalCount: Int) {
        message_view.setMessageCount(totalCount)
    }

    override fun bindUserInfo(response: UserInfoResponse) {
        updateUserData(response.nickname, response.createdAt, response.avatar)
        isSendEmployer = response.is_send_employer
    }


    private fun updateUserData(nickname: String, createdAt: String, avatar: String) {
        name_tv.text = nickname
        create_time_tv.apply {
            text = createdAt
            visibility = View.VISIBLE
        }
        val options = RequestOptions.bitmapTransform(GlideRoundedCornersTransform(30, GlideRoundedCornersTransform.CornerType.ALL))
            .error(R.drawable.ic_noheader)
            .fallback(R.drawable.ic_noheader)
            .placeholder(R.drawable.ic_noheader)

        Glide.with(requireContext()).load(avatar).apply(options).into(avatar_iv)
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        when (baseEvent!!.code) {
            EventCode.EVENT_LOGIN_SUCCESS -> {
                initDefaultUi()
                presenter.getUserInfo()

            }
            EventCode.EVENT_REFRESH_USER_INFO,
            EventCode.EVENT_SIGN_IN_CHANGE_STATUS,
            ->
                presenter.getUserInfo()
            EventCode.EVENT_SIGN_OUT -> {
                presenter.alreadyConnectRongCloud = false
                initDefaultUi()
            }
            EventCode.EVENT_EMPLOYER_SUBMIT -> {
                isSendEmployer = true
            }
            else -> {
            }
        }

    }

    private fun initDefaultUi() {
        if (User.isLogon()) {
            val user = User.getInstance()
            updateUserData(user.nickName, user.createdAt, user.avatar)
        } else {
            updateUserData(getString(R.string.mine_login_register), "", "")
            create_time_tv.visibility = View.GONE
            message_view.setMessageCount(0)
        }
        message_view.setMessageImageResource(R.mipmap.ic_mine_message)

        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(context)
        if (languageLocale.language == "zh") {
            activity_center_iv.setImageResource(R.mipmap.ic_mine_activity)
        } else {
            activity_center_iv.setImageResource(R.mipmap.ic_mine_activity_en)
        }

    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            presenter.getUnreadMessageCount()
        }

    }

    private fun showShareDialog(inviteLink: String) {


        val timeDialog = BottomSheetDialog(requireContext())
        this.degreeDialog = timeDialog
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.layout_share_choose, null)
        timeDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        view.findViewById<LinearLayout>(R.id.save_qr_ll).setOnClickListener {
            ZxingUtil.saveShareQr(inviteLink, qrBgView)
            ToastUtils.showShort(R.string.save_success)
            timeDialog.dismiss()
        }
        view.findViewById<LinearLayout>(R.id.share_facebook_ll).setOnClickListener {
            MobUtil.shareFacebookWebPage(inviteLink, activity)
            timeDialog.dismiss()
        }
        view.findViewById<LinearLayout>(R.id.copy_link_ll).setOnClickListener {
            ClipboardUtils.copyText(inviteLink)
            ToastUtils.showShort(getString(R.string.copy_successfully))
            timeDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.cancel_tv).setOnClickListener {
            timeDialog.dismiss()
        }
        timeDialog.show()
    }


}