package com.jcs.where.features.mall.cart

import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_mall_cart.*


/**
 * Created by Wangsw  2021/12/14 17:49.
 * 商城购物车
 */
class MallCartActivity : BaseMvpActivity<MallCartPresenter>(), MallCartView {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: MallCartAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_cart

    override fun initView() {
        swipe_layout.apply {
            setOnRefreshListener(this@MallCartActivity)
            setColorSchemeColors(ColorUtils.getColor(R.color.blue_4C9EF2))
        }
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
        }
        mAdapter = MallCartAdapter().apply {
            setEmptyView(emptyView)
            numberChangeListener = this@MallCartActivity
            onChildSelectClick = this@MallCartActivity
            onGroupSelectClick = this@MallCartActivity

        }
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }


    }

    override fun initData() {
        presenter = MallCartPresenter(this)
        onRefresh()
    }

    override fun bindListener() {
        select_all_tv.setOnClickListener {
            var isSelectAll = select_all_tv.isChecked
            isSelectAll = !isSelectAll

            // data
            presenter.handleSelectAll(mAdapter, isSelectAll)

            // ui
            if (isSelectAll) {
                VibrateUtils.vibrate(10)
                val handlePrice = presenter.handlePrice(mAdapter)
                total_price_tv.text =
                    StringUtils.getString(R.string.price_unit_format, handlePrice.stripTrailingZeros().toPlainString())
//                select_count_tv.text = getString(R.string.total_format, presenter.getSelectedCount(mAdapter))
            } else {
                total_price_tv.text = getString(R.string.price_unit_format, "0.00")
//                select_count_tv.text = getString(R.string.total_format, 0)
            }

            select_all_tv.isChecked = isSelectAll
        }

        delete_tv.setOnClickListener {

            if (select_all_tv.isChecked) {
                presenter.clearStoreCart()
                mAdapter.setNewInstance(null)
                emptyView.showEmptyDefault()
            } else {

                presenter.deleteCart(mAdapter)
            }
        }


        settlement_tv.setOnClickListener {
/*
            val selectedCount = presenter.getSelectedCount(mAdapter)
            if (selectedCount <= 0) {
                ToastUtils.showShort(R.string.please_select_a_product)
                return@setOnClickListener
            }

            val selectedData = presenter.getSelectedData(mAdapter)

            val appList: ArrayList<StoreOrderCommitData> = ArrayList()

            selectedData.forEach {

                // shop
                val shop = StoreOrderCommitData().apply {
                    shop_id = it.shop_id
                    shop_title = it.shop_name
                    delivery_type = listType + 1
                    delivery_fee = it.delivery_fee.toFloat()
                }

                // shop $good
                it.goods.forEach { good ->
                    val goodData = good.good_data
                    val goodInfo = StoreGoodsCommit().apply {
                        good_id = goodData.id
                        delivery_type = listType + 1

                        if (goodData.images.isNotEmpty()) {
                            image = goodData.images[0]
                        }
                        goodName = goodData.title
                        good_num = good.good_num
                        price = goodData.price
                    }
                    shop.goods.add(goodInfo)
                }
                appList.add(shop)
            }

            startActivityAfterLogin(StoreOrderCommitActivity::class.java, Bundle().apply {
                putSerializable(Constant.PARAM_ORDER_COMMIT_DATA, appList)
            })
*/
        }


    }

    override fun bindData(data: MutableList<MallCartGroup>, lastPage: Boolean) {

    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page)
    }

    override fun onChildNumberChange(cartId: Int, add: Boolean) {

    }

    override fun onChildSelected(checked: Boolean) {

    }

    override fun onGroupSelected(nativeIsSelect: Boolean) {

    }


}