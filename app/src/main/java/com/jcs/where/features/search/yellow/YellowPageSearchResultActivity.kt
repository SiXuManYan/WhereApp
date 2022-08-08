package com.jcs.where.features.search.yellow

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.map.MechanismAdapter
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_yellow_page_search_result.*

/**
 * Created by Wangsw  2021/9/3 14:57.
 *
 */
class YellowPageSearchResultActivity : BaseMvpActivity<YellowPageSearchResultPresenter>(), YellowPageSearchResultView,
    OnItemClickListener, OnLoadMoreListener {


    private var search = ""
    private var categoryId = ""

    private lateinit var mAdapter: MechanismAdapter
    private lateinit var emptyView: EmptyView
    private var page = Constant.DEFAULT_FIRST_PAGE

    override fun getLayoutId() = R.layout.activity_yellow_page_search_result

    override fun isStatusDark() = true


    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        val bundle = intent.extras ?: return
        search = bundle.getString(Constant.PARAM_NAME, "")
        categoryId = bundle.getString(Constant.PARAM_CATEGORY_ID, "")

        //
        search_tv.text = search
        emptyView = EmptyView(this).apply {
            initEmpty(R.mipmap.ic_empty_search, R.string.empty_search, R.string.empty_search_hint)
            addEmptyList(this)
        }
        mAdapter = MechanismAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@YellowPageSearchResultActivity)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener(this@YellowPageSearchResultActivity)
        }

        content_rv.adapter = mAdapter
        content_rv.addItemDecoration(
            DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0).apply {
                setDrawHeaderFooter(true)
            })


    }

    override fun initData() {
        presenter = YellowPageSearchResultPresenter(this)
        presenter.getData(page, categoryId, search)
    }

    override fun onLoadMore() {
        page++
        presenter.getData(page, categoryId, search)
    }

    override fun bindListener() {
        back_iv.setOnClickListener { finish() }
        search_ll.setOnClickListener { finish() }

    }

    override fun bindData(response: MutableList<MechanismResponse>, isLastPage: Boolean) {

        val loadMoreModule = mAdapter.loadMoreModule
        if (response.isEmpty()) {
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
            mAdapter.setNewInstance(response)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(response)
            if (isLastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        startActivity(MechanismActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ID, data.id)
        })

    }

}