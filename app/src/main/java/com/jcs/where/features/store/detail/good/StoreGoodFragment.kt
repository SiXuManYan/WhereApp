package com.jcs.where.features.store.detail.good

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.request.StoreShopRequest
import com.jcs.where.api.response.store.StoreGoods
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.store.good.StoreGoodDetailActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
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
            showEmptyNothing()
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
        recycler.adapter = mAdapter


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
                emptyView.showEmptyDefault()
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