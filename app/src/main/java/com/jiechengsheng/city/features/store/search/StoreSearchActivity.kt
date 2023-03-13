package com.jiechengsheng.city.features.store.search

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.store.StoreRecommend
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.store.detail.StoreDetailActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_search_store.*

/**
 * Created by Wangsw  2021/6/4 15:58.
 * 商城搜索
 */
class StoreSearchActivity : BaseMvpActivity<StoreSearchPresenter>(), StoreSearchView, OnLoadMoreListener, OnItemChildClickListener {

    lateinit var mAdapter: StoreSearchAdapter

    private var title: String = ""
    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.activity_search_store

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.getStringExtra(Constant.PARAM_NAME)?.let {
            title = it
            search_aet.text = it
        }
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            addEmptyList(this)
        }
        mAdapter = StoreSearchAdapter().apply {
            setEmptyView(emptyView)
            addChildClickViewIds(R.id.parent_rl)
            setOnItemChildClickListener(this@StoreSearchActivity)
        }

        mAdapter.loadMoreModule.apply {
            setOnLoadMoreListener(this@StoreSearchActivity)
            isAutoLoadMore = true
            isEnableLoadMoreIfNotFullPage = true

        }

        recycler_view.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
                SizeUtils.dp2px(15f),
                0))
        }
    }

    override fun initData() {
        presenter = StoreSearchPresenter(this)
        presenter.getData(page, title)
    }

    override fun bindListener() {
        back_iv.setOnClickListener { finish() }
        search_aet.setOnClickListener { finish() }
    }

    override fun bindData(data: MutableList<StoreRecommend>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
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

    override fun onLoadMore() {
        page++
        presenter.getData(page, title)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]

        when (view.id) {
            R.id.parent_rl -> {
                startActivity(StoreDetailActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ID, data.id)
                })
            }
            else -> {
            }
        }
    }


}