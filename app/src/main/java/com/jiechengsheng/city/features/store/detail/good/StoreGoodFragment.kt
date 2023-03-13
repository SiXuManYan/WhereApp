package com.jiechengsheng.city.features.store.detail.good

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.StoreShopRequest
import com.jiechengsheng.city.api.response.store.StoreGoods
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.store.good.StoreGoodDetailActivity
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list_no_refresh.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/6/16 15:27.
 * 商城 商品列表
 */
class StoreGoodFragment : BaseMvpFragment<StoreGoodPresenter>(), StoreGoodView, OnLoadMoreListener, OnItemClickListener {


    /** 商家id */
    private var shop_id: Int = 0

    /** 配送费 */
    private var delivery_fee: Float = 0f

    private var shop_name: String = ""

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var mAdapter: StoreGoodAdapter
    private lateinit var emptyView: EmptyView


    companion object {

        /**
         * 美食评论
         * @param shop_id 商家ID
         */
        fun newInstance(shop_id: Int): StoreGoodFragment {
            val fragment = StoreGoodFragment()
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shop_id)
//                putFloat(Constant.PARAM_DELIVERY_FEE, delivery_fee)
//                putString(Constant.PARAM_SHOP_NAME, shop_name)
            }
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutId() = R.layout.fragment_refresh_list_no_refresh

    override fun initView(view: View) {

        arguments?.let {
            shop_id = it.getInt(Constant.PARAM_SHOP_ID)
            delivery_fee = it.getFloat(Constant.PARAM_DELIVERY_FEE)
            shop_name = it.getString(Constant.PARAM_SHOP_NAME, "")
        }

        emptyView = EmptyView(context).apply {
            showEmptyDefault()
            addEmptyList(this)
        }
        mAdapter = StoreGoodAdapter().apply {
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@StoreGoodFragment)
            setEmptyView(emptyView)
            setOnItemClickListener(this@StoreGoodFragment)
            addChildClickViewIds(R.id.buy_tv)
            setOnItemChildClickListener { adapter, view, position ->

                if (!User.isLogon()) {
                    startActivity(LoginActivity::class.java)
                    return@setOnItemChildClickListener
                }

                val storeGoods = mAdapter.data[position]
                EventBus.getDefault().post(BaseEvent(storeGoods))
            }
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0))

        }


    }

    override fun initData() {
        presenter = StoreGoodPresenter(this)
    }

    override fun bindListener() {

    }

    override fun onLoadMore() {
        page++
        loadOnVisible()
    }

    override fun loadOnVisible() {
        presenter.getGood(shop_id, page)
    }

    override fun bindData(data: MutableList<StoreGoods>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(data)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(data)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        startActivity(StoreGoodDetailActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ID, data.id)
            putInt(Constant.PARAM_SHOP_ID, shop_id)
            putFloat(Constant.PARAM_DELIVERY_FEE, delivery_fee)
            putString(Constant.PARAM_SHOP_NAME, shop_name)
        })
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        val data = baseEvent.data
        if (data is StoreShopRequest) {
            delivery_fee = data.delivery_fee
            shop_name = data.shop_name
        }

    }

}