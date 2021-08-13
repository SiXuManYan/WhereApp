package com.jcs.where.features.mine

import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.MerchantSettledInfoResponse
import com.jcs.where.api.response.UserInfoResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.customer.ExtendChatActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.address.AddressActivity
import com.jcs.where.features.message.MessageCenterActivity
import com.jcs.where.features.setting.SettingActivity
import com.jcs.where.features.setting.information.ModifyInfoActivity
import com.jcs.where.integral.IntegralActivity
import com.jcs.where.mine.activity.AboutActivity
import com.jcs.where.mine.activity.CollectionListActivity
import com.jcs.where.mine.activity.FootprintActivity
import com.jcs.where.mine.activity.LanguageActivity
import com.jcs.where.mine.activity.merchant_settled.MerchantSettledActivity
import com.jcs.where.mine.activity.merchant_settled.MerchantVerifyActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.SPKey
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
        message_view.setMessageImageResource(R.mipmap.ic_mine_message)
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
            if (CacheUtil.needUpdateBySpKey(SPKey.K_TOKEN).isEmpty()) {
                startActivity(LoginActivity::class.java)
            } else {
                startActivity(ModifyInfoActivity::class.java)
            }
        }


        integral_iv.setOnClickListener {
            startActivityAfterLogin(IntegralActivity::class.java)
        }
        integral_tv.setOnClickListener {
            startActivityAfterLogin(IntegralActivity::class.java)
        }
        collect_tv.setOnClickListener {
            startActivityAfterLogin(CollectionListActivity::class.java)
        }
        viewed_tv.setOnClickListener {
            startActivityAfterLogin(FootprintActivity::class.java)
        }
        vouchers_tv.setOnClickListener {
            // todo 卡券页
        }
        merchant_rl.setOnClickListener {
            if (!User.isLogon()) {
                startActivity(LoginActivity::class.java)
                return@setOnClickListener
            }
            val user = User.getInstance()
            if (user.merchantApplyStatus != 1) {
                startActivity(MerchantSettledActivity::class.java)
                return@setOnClickListener
            }
            presenter.getMerchantSettledInfo()


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
        GlideUtil.load(context, response.avatar, avatar_iv, 30)
    }

    override fun handleMerchant(response: MerchantSettledInfoResponse) {
        MerchantVerifyActivity.go(context, response.isVerify)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            presenter.getUnreadMessageCount()
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        when (baseEvent!!.code) {
            EventCode.EVENT_LOGIN_SUCCESS,
            EventCode.EVENT_REFRESH_USER_INFO,
            EventCode.EVENT_SIGN_IN_CHANGE_STATUS -> presenter.getUserInfo()
            else -> {
            }
        }

    }

}