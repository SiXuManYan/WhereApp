package com.jcs.where.features.mall.buy


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.api.response.mall.request.MallCommitResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.address.AddressActivity
import com.jcs.where.features.mall.buy.coupon.OrderCouponHomeFragment
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_mall_order_commit.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/15 13:56.
 * 商城下单
 */
class MallOrderCommitActivity : BaseMvpActivity<MallOrderCommitPresenter>(), MallOrderCommitView {


    private var totalPrice: BigDecimal = BigDecimal.ZERO

    private var data: ArrayList<MallCartGroup> = ArrayList()

    /** 收货地址 */
    var mSelectAddressData: AddressResponse? = null

    /** 总配送费 */
    var mTotalServiceDeliveryFee: BigDecimal? = BigDecimal.ZERO

    /** 总优惠券金额 */
    var mTotalCouponMoney: BigDecimal = BigDecimal.ZERO

    /**
     * 当前优惠券id
     * 包含以下来源
     *      1.用户从券包选择优惠券，
     *      2.进入页面，请求到默认优惠券
     *      3.从当前页弹窗选择优惠券
     *  值：
     *  0：未选择或没有匹配到优惠券
     */
    var currentCouponId = 0

    private lateinit var mAdapter: MallOrderCommitAdapter

    private lateinit var mSelectedCouponDialog: OrderCouponHomeFragment

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_order_commit

    override fun initView() {
        val bundle = intent.extras
        bundle?.let {
            data = it.getSerializable(Constant.PARAM_DATA) as ArrayList<MallCartGroup>
            currentCouponId = it.getInt(Constant.PARAM_COUPON_ID, 0)
        }
        initContent()
    }

    private fun initContent() {
        mAdapter = MallOrderCommitAdapter()
        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(10f), 0, 0))
        }
        mAdapter.setNewInstance(data)


    }

    /** 处理选择地址 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras ?: return@registerForActivityResult

            val selectData = bundle.getSerializable(Constant.PARAM_DATA) as AddressResponse
            mSelectAddressData = selectData
            select_address_tv.text = selectData.address
            address_value_ll.visibility = ViewGroup.VISIBLE
            val sex = if (selectData.sex == 1) {
                getString(R.string.sir)
            } else {
                getString(R.string.lady)
            }
            address_name_tv.text = getString(R.string.recipient_format, selectData.contact_name, sex, selectData.contact_number)
            phone_tv.text = selectData.contact_number

            presenter.getDelivery(mAdapter, selectData.city_id)

        }
    }


    override fun initData() {
        presenter = MallOrderCommitPresenter(this)
        handleTotalPrice()
        presenter.getDefaultCoupon(mAdapter, data, currentCouponId)
        mSelectedCouponDialog = OrderCouponHomeFragment().apply {
            specsIdsJsonStr = this@MallOrderCommitActivity.presenter.getSpecsIdsJsonString(data)
            goodsJsonStr = this@MallOrderCommitActivity.presenter.getGoodsJsonString(data)

        }

    }

    private fun handleTotalPrice() {

        totalPrice = presenter.handlePrice(mAdapter, mTotalServiceDeliveryFee, mTotalCouponMoney)


        // 支付价格
        total_price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())

        // ## 价格明细 ##
        // 扣除优惠前的总价
        val oldPrice = BigDecimalUtil.add(totalPrice, mTotalCouponMoney)
        total_price_old_tv.text = getString(R.string.price_unit_format, oldPrice.toPlainString())

        // 总运费
        val totalDeliveryFee = presenter.getTotalDeliveryFee(mAdapter)
        total_delivery_fee_tv.text = getString(R.string.price_unit_format, totalDeliveryFee.toPlainString())

        // 优惠金额
        if (mTotalCouponMoney == BigDecimal.ZERO) {
            coupon_tv.text = ""
        } else {
            coupon_tv.text = getString(R.string.price_unit_format, mTotalCouponMoney.toPlainString())
        }
        // 合计
        total_price_copy_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())
    }

    override fun bindListener() {
        address_ll.setOnClickListener {
            searchLauncher.launch(Intent(this, AddressActivity::class.java)
                .putExtra(Constant.PARAM_HANDLE_ADDRESS_SELECT, true))
        }
        submit_tv.setOnClickListener {
            if (mSelectAddressData == null) {
                ToastUtils.showShort(R.string.address_edit_hint)
                return@setOnClickListener
            }
            presenter.orderCommit(data, mSelectAddressData?.id, currentCouponId)
        }
        selected_coupon.setOnClickListener {
            mSelectedCouponDialog.alreadySelectedCouponId = currentCouponId
            mSelectedCouponDialog.show(supportFragmentManager, mSelectedCouponDialog.tag)
        }
    }

    override fun commitSuccess(response: MallCommitResponse) {

        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
            putDouble(Constant.PARAM_TOTAL_PRICE, response.total_price.toDouble())
            putIntegerArrayList(Constant.PARAM_ORDER_IDS, response.orders)
            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_MALL)
        })
    }


    override fun bindTotalDelivery(totalServiceDeliveryFee: BigDecimal?) {
        mTotalServiceDeliveryFee = totalServiceDeliveryFee
        handleTotalPrice()

    }

    override fun bindDefaultCoupon(couponId: Int, couponMoney: BigDecimal) {
        currentCouponId = couponId
        mTotalCouponMoney = couponMoney
        handleTotalPrice()
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_COUPON_SELECTED -> {
                val selectedCouponId = baseEvent.data as Int
                currentCouponId = selectedCouponId
                presenter.getDefaultCoupon(adapter = mAdapter, data, selectedCouponId)
            }
        }


    }

}