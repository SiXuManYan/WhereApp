package com.jcs.where.features.merchant.recult

import android.view.View
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.merchant.MerchantSettledData
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragemnt_settled_result.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/11/19 18:15.
 * 商家入驻审核中、审核结果
 */
class SettledResultFragment : BaseMvpFragment<SettledResultPresenter>(), SettledResultView {


    /**
     * 审核状态（1：待审核，2：审核通过，3：审核未通过）
     */
    var type = 0


    override fun getLayoutId() = R.layout.fragemnt_settled_result

    override fun initView(view: View) {
        changeContent(type)
    }

    override fun initData() {


    }

    override fun bindListener() = Unit


    /**
     * 审核状态（1：待审核，2：审核通过，3：审核未通过）
     */
    private fun changeContent(type: Int) {
        when (type) {
            2 -> {
                type_image_iv.setImageResource(R.mipmap.ic_audit_success)
                type_text_tv.text = getString(R.string.merchant_verify_success_title)
                type_desc_tv.text = getString(R.string.merchant_verify_success_desc)
                // 再次提交暂时隐藏
                recommit_tv.visibility = View.GONE
//                recommit_tv.text = getString(R.string.commit_again)
//                recommit_tv.setOnClickListener {
//                    EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_MERCHANT_COMMIT_NEW))
//                }
            }
            3 -> {
                type_image_iv.setImageResource(R.mipmap.ic_audit_fail)
                type_text_tv.text = getString(R.string.merchant_verify_faild_title)
                type_desc_tv.text = getString(R.string.merchant_verify_faild_desc)
                recommit_tv.visibility = View.VISIBLE
                recommit_tv.text = getString(R.string.recommit)
                recommit_tv.setOnClickListener {
                    EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_MERCHANT_RECOMMIT))
                }
            }
            else -> {
                type_image_iv.setImageResource(R.mipmap.ic_auditing)
                type_text_tv.text = getString(R.string.merchant_verify_ing_title)
                type_desc_tv.text = getString(R.string.merchant_verify_ing_desc)
                recommit_tv.visibility = View.GONE

            }
        }

    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        when (baseEvent.code) {
            EventCode.EVENT_MERCHANT_POST_SUCCESS -> {
                type = 1
                changeContent(type)
            }
            EventCode.EVENT_MERCHANT_CHANGE_TYPE -> {
                type = if (baseEvent.data is MerchantSettledData) {
                    (baseEvent.data as MerchantSettledData).is_verify
                } else {
                    (baseEvent.data as Int)
                }
                changeContent(type)
            }
            else -> {

            }
        }
    }


}

interface SettledResultView : BaseMvpView {

}

class SettledResultPresenter(private var view: SettledResultView) : BaseMvpPresenter(view) {

}