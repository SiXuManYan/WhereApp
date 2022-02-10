package com.jcs.where.features.mine

import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jcs.where.R
import com.jcs.where.api.response.MerchantSettledInfoResponse
import com.jcs.where.api.response.UserInfoResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.customer.ExtendChatActivity
import com.jcs.where.features.address.AddressActivity
import com.jcs.where.features.collection.CollectionActivity
import com.jcs.where.features.coupon.CardCouponActivity
import com.jcs.where.features.daily.scoe.ScoreActivity
import com.jcs.where.features.daily.sign.SignInActivity
import com.jcs.where.features.footprint.FootprintUi
import com.jcs.where.features.merchant.MerchantSettledActivity
import com.jcs.where.features.message.MessageCenterActivity
import com.jcs.where.features.setting.SettingActivity
import com.jcs.where.features.setting.information.ModifyInfoActivity
import com.jcs.where.mine.activity.AboutActivity
import com.jcs.where.mine.activity.LanguageActivity
import com.jcs.where.mine.activity.merchant_settled.MerchantVerifyActivity
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import kotlinx.android.synthetic.main.fragment_mine_2.*

/**
 * Created by Wangsw  2021/8/13 14:14.
 * 用户页
 */
class MineFragment : BaseMvpFragment<MinePresenter>(), MineView {


    override fun getLayoutId() = R.layout.fragment_mine_2

    override fun isStatusDark() = true

    override fun needChangeStatusBarStatus() = true

    override fun initView(view: View) {
        BarUtils.addMarginTopEqualStatusBarHeight(top_ll)
        val topPadding = BarUtils.getStatusBarHeight() + SizeUtils.dp2px(40f)
        content_ll.setPaddingRelative(0, topPadding, 0, 0)

        initDefaultUi()
    }

    override fun initData() {
        presenter = MinePresenter(this)
        presenter.getUnreadMessageCount()
        presenter.getUserInfo()
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
            startActivityAfterLogin(FootprintUi::class.java)
        }
        vouchers_tv.setOnClickListener {
            startActivityAfterLogin(CardCouponActivity::class.java)
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

    }

    override fun bindUnreadMessageCount(totalCount: Int) {
        message_view.setMessageCount(totalCount)
    }

    override fun bindUserInfo(response: UserInfoResponse) {
        name_tv.text = response.nickname
        create_time_tv.apply {
            text = response.createdAt
            visibility = View.VISIBLE
        }
        val options = RequestOptions.bitmapTransform(GlideRoundedCornersTransform(30, GlideRoundedCornersTransform.CornerType.ALL))
            .error(R.drawable.ic_noheader)
            .fallback(R.drawable.ic_noheader)
            .placeholder(R.drawable.ic_noheader)

        Glide.with(requireContext()).load(response.avatar).apply(options).into(avatar_iv)

    }

    override fun handleMerchant(response: MerchantSettledInfoResponse) {
        MerchantVerifyActivity.go(context, response.isVerify)
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        when (baseEvent!!.code) {
            EventCode.EVENT_LOGIN_SUCCESS,
            EventCode.EVENT_REFRESH_USER_INFO,
            EventCode.EVENT_SIGN_IN_CHANGE_STATUS,
            -> presenter.getUserInfo()
            EventCode.EVENT_SIGN_OUT -> {
                presenter.alreadyConnectRongCloud = false
                initDefaultUi()
            }
            else -> {
            }
        }

    }

    private fun initDefaultUi() {
        name_tv.text = getString(R.string.mine_login_register)
        create_time_tv.apply {
            text = ""
            visibility = View.GONE
        }
        avatar_iv.setImageResource(R.drawable.ic_noheader)
        message_view.setMessageImageResource(R.mipmap.ic_mine_message)
        message_view.setMessageCount(0)
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            presenter.getUnreadMessageCount()
//            if (User.isLogon()) {
//                presenter?.connectRongCloud(User.getInstance().rongData.token)
//            }
        }
    }


}