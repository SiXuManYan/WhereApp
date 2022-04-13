package com.jcs.where.features.mall.buy


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

    /** 平台优惠券总金额 */
    private var mPlatformCouponTotalMoney: BigDecimal = BigDecimal.ZERO

    /** 店铺优惠券总金额 */
    private var mShopCouponTotalMoney: BigDecimal = BigDecimal.ZERO

    /**
     * 当前平台优惠券id
     * 包含以下来源
     *      1.进入页面，请求到默认优惠券
     *      2.从当前页弹窗选择优惠券
     *  值：
     *  0：未选择或没有匹配到优惠券
     */
    private var currentPlatformCouponId = 0

    /** 当前进行更改店铺券的 店铺id */
    private var currentHandleShopId = 0



    private lateinit var mAdapter: MallOrderCommitAdapter

    private lateinit var mSelectedCouponDialog: OrderCouponHomeFragment

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_order_commit

    override fun initView() {
        val bundle = intent.extras
        bundle?.let {
            data = it.getSerializable(Constant.PARAM_DATA) as ArrayList<MallCartGroup>
        }
        initContent()
    }

    private fun initContent() {
        mAdapter = MallOrderCommitAdapter().apply {
            addChildClickViewIds(R.id.select_shop_coupon_rl)
            setOnItemChildClickListener { adapter, view, position ->

                val mallCartGroup = mAdapter.data[position]

                currentHandleShopId = mallCartGroup.shop_id

                if (view.id == R.id.select_shop_coupon_rl) {

                    // 选择店铺优惠券 需要额外传 shop_goods shop_id
                    mSelectedCouponDialog.apply {

                        alreadySelectedCouponId = mallCartGroup.nativeShopCouponId
                        goodsJsonStr = null

                        shopId = mallCartGroup.shop_id
                        shopGoodsJson =
                            this@MallOrderCommitActivity.presenter.getShopGoodsJsonString(
                                this@MallOrderCommitActivity.data,
                                mallCartGroup.shop_id)

                    }

                    mSelectedCouponDialog.show(supportFragmentManager, mSelectedCouponDialog.tag)

                }


            }

        }
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
        presenter.getDefaultCoupon(mAdapter, data,isFirstRequest = true)
        mSelectedCouponDialog = OrderCouponHomeFragment()

    }

    private fun handleTotalPrice() {

        // 最终支付价格
        totalPrice = presenter.handlePrice(mAdapter, mTotalServiceDeliveryFee, mPlatformCouponTotalMoney, mShopCouponTotalMoney)


        // 支付价格
        total_price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())

        // ## 价格明细 ##
        // 商品价格
        val oldPrice = presenter.getAllGoodPrice(mAdapter)
        total_price_old_tv.text = getString(R.string.price_unit_format, oldPrice.toPlainString())

        // 总运费
        val totalDeliveryFee = presenter.getTotalDeliveryFee(mAdapter)
        total_delivery_fee_tv.text = getString(R.string.price_unit_format, totalDeliveryFee.toPlainString())

        // 商家优惠券金额
        shop_offers_tv.text = getString(R.string.price_unit_format, mShopCouponTotalMoney.toPlainString())

        // 平台优惠券金额
        if (mPlatformCouponTotalMoney == BigDecimal.ZERO) {
            coupon_tv.text = ""
        } else {
            coupon_tv.text = getString(R.string.price_unit_format, mPlatformCouponTotalMoney.toPlainString())
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
            presenter.orderCommit(data, mSelectAddressData?.id, currentPlatformCouponId)
        }
        selected_coupon.setOnClickListener {
            mSelectedCouponDialog.apply {
                shopGoodsJson = null
                shopId = null

                goodsJsonStr = this@MallOrderCommitActivity.presenter.getGoodsJsonString(data)
                alreadySelectedCouponId = currentPlatformCouponId
            }
            mSelectedCouponDialog.show(supportFragmentManager, mSelectedCouponDialog.tag)
            selected_coupon.isClickable = false
            Handler(mainLooper).postDelayed({
                selected_coupon.isClickable = true
            }, 1000)
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

    override fun bindDefaultCoupon(platformCouponId: Int, platformCouponTotalMoney: BigDecimal, shopCouponTotalMoney: BigDecimal) {
        currentPlatformCouponId = platformCouponId
        mPlatformCouponTotalMoney = platformCouponTotalMoney
        mShopCouponTotalMoney = shopCouponTotalMoney

        handleTotalPrice()
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_SELECTED_PLATFORM_COUPON -> {
                val selectedPlatformCouponId = baseEvent.data as Int
                currentPlatformCouponId = selectedPlatformCouponId
                presenter.getDefaultCoupon(mAdapter, data, currentPlatformCouponId)
            }
            EventCode.EVENT_SELECTED_SHOP_COUPON -> {
                val selectedShopCouponId = baseEvent.data as Int
                // 更换店铺优惠券后，将平台券置空
                currentPlatformCouponId = 0
                // 更新店铺item中的 nativeShopCouponId
                presenter.updateItemShopCouponId(mAdapter,currentHandleShopId,selectedShopCouponId)
                // 获取默认优惠券
                presenter.getDefaultCoupon(mAdapter, data, currentPlatformCouponId)
            }

        }
    }

}