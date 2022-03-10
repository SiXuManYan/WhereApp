package com.jcs.where.features.mall.buy.coupon.child

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.UserCoupon
import com.jcs.where.api.response.mall.request.MallOrderCoupon
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_order_coupon.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/3/9 15:05.
 *  订单选择优惠券
 */
class OrderCouponFragment : BaseMvpFragment<OrderCouponPresenter>(), OrderCouponView {

    /** type 类型  1可使用 2 不可使用  */
    var type = 0

    var specsIdsJsonStr = ""
    var goodsJsonStr = ""

    var selectedCouponId = 0

    private lateinit var mAdapter: OrderCouponAdapter
    private lateinit var emptyView: EmptyView
    private lateinit var headerView: View

    override fun getLayoutId() = R.layout.fragment_order_coupon

    override fun initView(view: View?) {
        emptyView = EmptyView(requireContext()).apply {
            initEmpty(R.mipmap.ic_empty_card_coupon, R.string.no_coupons_available)
        }
        headerView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_order_coupon_header, null, false)


        mAdapter = OrderCouponAdapter().apply {
            setEmptyView(emptyView)
        }

        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0))
        }
        if (type == 1) {
            mAdapter.addHeaderView(headerView)
            headerView.visibility = View.GONE
            mAdapter.setOnItemClickListener(this@OrderCouponFragment)
            confirm_tv.visibility = View.VISIBLE
        } else {
            confirm_tv.visibility = View.GONE
        }


    }

    override fun initData() {
        presenter = OrderCouponPresenter(this)
    }

    override fun loadOnVisible() {
        presenter.getData(type, specsIdsJsonStr, goodsJsonStr, selectedCouponId)
    }


    override fun bindListener() {
        confirm_tv.setOnClickListener {
            EventBus.getDefault().post(BaseEvent<Int>(EventCode.EVENT_COUPON_SELECTED, selectedCouponId))
        }
    }


    override fun bindData(data: MutableList<UserCoupon>) {
        if (data.isEmpty()) {
            mAdapter.setNewInstance(null)
            emptyView.showEmptyContainer()
            return
        }
        mAdapter.setNewInstance(data)
        data.forEach {
            if (it.nativeSelected) {
                headerView.visibility = View.VISIBLE
                val hint = headerView.findViewById<TextView>(R.id.coupon_price_hint_tv)
                hint.text = getString(R.string.coupon_price_format, it.money)
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        val userCoupon = mAdapter.data[position]
        if (type == 1) {
            mAdapter.data.forEach {
                it.nativeSelected = (it.id == userCoupon.id)
            }
            mAdapter.notifyDataSetChanged()

            headerView.visibility = View.VISIBLE
            val hint = headerView.findViewById<TextView>(R.id.coupon_price_hint_tv)
            hint.text = getString(R.string.coupon_price_format, userCoupon.money)

        } else {
            ToastUtils.showShort(R.string.no_coupons_available)
        }


    }
}

interface OrderCouponView : BaseMvpView, OnItemClickListener {
    fun bindData(data: MutableList<UserCoupon>)
}

class OrderCouponPresenter(private var view: OrderCouponView) : BaseMvpPresenter(view) {


    fun getData(currentType: Int, specsIdsJson: String, goodsJson: String, selectedCouponId: Int) {

        val apply = MallOrderCoupon().apply {
            type = currentType
            specsIds = specsIdsJson
            goods = goodsJson
        }

        requestApi(mRetrofit.getOrderCoupon(apply), object : BaseMvpObserver<ArrayList<UserCoupon>>(view) {

            override fun onSuccess(response: ArrayList<UserCoupon>) {

                if (selectedCouponId != 0) {

                    response.forEach {
                        if (it.id == selectedCouponId) {
                            it.nativeSelected = true
                        }
                    }
                }

                view.bindData(response.toMutableList())
            }
        })
    }

}
