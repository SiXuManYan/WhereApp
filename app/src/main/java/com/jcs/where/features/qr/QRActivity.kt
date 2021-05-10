package com.jcs.where.features.qr

import com.jcs.where.R
import com.jcs.where.api.response.gourmet.qr.QrResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_qr.*

/**
 * Created by Wangsw  2021/4/23 16:28.
 * 二维码
 */
class QRActivity : BaseMvpActivity<QRPresenter>(), QRView {

    private var orderId = "";

    override fun getLayoutId() = R.layout.activity_qr

    override fun initView() {

        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        orderId = bundle.getString(Constant.PARAM_ORDER_ID, "")

    }

    override fun initData() {
        presenter = QRPresenter(this);
        presenter.getQrDetail(orderId)
    }

    override fun bindListener() {

    }

    override fun bindDetail(it: QrResponse) {
        name_tv.text = it.restaurant_name
        good_name_tv.text = it.good_name
        expire_date_tv.text = getString(R.string.expire_date, it.expire_date)
        coupon_no_tv.text = it.coupon_no
        GlideUtil.load(this, it.coupon_qr_code, qr_iv)

    }


}
