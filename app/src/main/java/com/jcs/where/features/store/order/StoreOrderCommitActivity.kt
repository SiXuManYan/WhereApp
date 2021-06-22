package com.jcs.where.features.store.order

import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreOrderCommitData
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_order_commit.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/21 10:26.
 *  商城提交订单
 */
class StoreOrderCommitActivity : BaseMvpActivity<StoreOrderCommitPresenter>(), StoreOrderCommitView {

    private lateinit var mAdapter: StoreOrderCommitAdapter

    private var totalPrice: BigDecimal = BigDecimal.ZERO

    var data: StoreOrderCommitData? = null

    override fun getLayoutId() = R.layout.activity_store_order_commit

    override fun initView() {

        val bundle = intent.extras
        bundle?.let {
            data = it.getSerializable(Constant.PARAM_ORDER_COMMIT_DATA) as StoreOrderCommitData
        }

        mAdapter = StoreOrderCommitAdapter()
        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(
                    DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                            SizeUtils.dp2px(10f), 0, 0).apply { setDrawHeaderFooter(false) }
            )
        }

        data?.let {
            mAdapter.addData(it)

        }
    }

    override fun isStatusDark() = true

    override fun initData() {
        presenter = StoreOrderCommitPresenter(this)
        totalPrice = presenter.handlePrice(mAdapter)
        total_price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())
    }


    override fun bindListener() {

    }


}