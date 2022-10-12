package com.jcs.where.features.bills.place.coupon

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillsCoupon
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
 *  平台优惠券或店铺优惠券
 */
class BillCouponChild : BaseMvpFragment<BillCouponChildPresenter>(), BillCouponChildView {

    /** type 类型  1可使用 2 不可使用  */
    var listType = 0

    /** 1-话费，2-水费，3-电费，4-网费 */
    var module = 0

    /** 金额 */
    var price = ""

    /** 账号 */
    var account = ""

    /** 选中的优惠券id */
    var selectedCouponId = 0


    private lateinit var mAdapter: BillsCouponAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.fragment_order_coupon

    override fun initView(view: View?) {


        emptyView = EmptyView(requireContext()).apply {
            setEmptyImage(R.mipmap.ic_empty_coupon)
            setEmptyHint(R.string.no_coupon)
            addEmptyList(this)
        }


        mAdapter = BillsCouponAdapter().apply {
            listType = this@BillCouponChild.listType
            setEmptyView(emptyView)
        }

        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0))
        }

        // 区分列表类型（可用、不可用）
        if (listType == 1) {
            mAdapter.setOnItemClickListener(this@BillCouponChild)
        }


    }

    override fun initData() {
        presenter = BillCouponChildPresenter(this)
    }

    override fun loadOnVisible() {
        presenter.getData(listType, module, price, account, selectedCouponId)
    }


    override fun bindListener() {

        confirm_tv.setOnClickListener {
            EventBus.getDefault().post(BaseEvent<Int>(EventCode.EVENT_SELECTED_PLATFORM_COUPON, selectedCouponId))
        }
    }


    override fun bindData(data: MutableList<BillsCoupon>) {
        if (data.isEmpty()) {
            mAdapter.setNewInstance(null)
            emptyView.showEmptyContainer()
            confirm_tv.visibility = View.GONE
            return
        }
        if (listType == 1) {
            confirm_tv.visibility = View.VISIBLE
        }
        mAdapter.setNewInstance(data)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        val userCoupon = mAdapter.data[position]
        if (listType == 1) {
            mAdapter.data.forEach {
                it.nativeSelected = (it.id == userCoupon.id)
            }
            mAdapter.notifyDataSetChanged()

            selectedCouponId = userCoupon.id

        }


    }
}

interface BillCouponChildView : BaseMvpView, OnItemClickListener {
    fun bindData(data: MutableList<BillsCoupon>)
}

class BillCouponChildPresenter(private var view: BillCouponChildView) : BaseMvpPresenter(view) {


    fun getData(listType: Int, requestModule: Int, requestPrice: String, account: String, selectedCouponId: Int? = null) {

        requestApi(mRetrofit.getBillOrderCoupon(listType, requestModule, requestPrice, account),
            object : BaseMvpObserver<ArrayList<BillsCoupon>>(view) {

                override fun onSuccess(response: ArrayList<BillsCoupon>) {

                    if (selectedCouponId != null && selectedCouponId != 0) {

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

enum class OrderCouponRequestType {
    /** 平台券请求 */
    TYPE_PLATFORM_COUPON,

    /** 店铺券请求 */
    TYPE_SHOP_COUPON
}
