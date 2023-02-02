package com.jcs.where.features.message.notice

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.message.SystemMessageResponse
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.message.notice.detail.SystemMessageDetailActivity
import com.jcs.where.features.web.WebViewActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration

/**
 * Created by Wangsw  2021/2/20 15:14.
 * 系统通知
 */
class SystemNoticeFragment : BaseMvpFragment<SystemNoticePresenter?>(), SystemNoticeView, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private   var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var swipe_layout: SwipeRefreshLayout
    private  lateinit var recycler: RecyclerView
    private  lateinit var mAdapter: SystemMessageAdapter
    var id = ArrayList<String>()

    private  lateinit var emptyView: EmptyView

    override fun getLayoutId()  = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        swipe_layout = view.findViewById(R.id.swipe_layout)
        recycler = view.findViewById(R.id.recycler)
        recycler.addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5),
            SizeUtils.dp2px(1f),
            SizeUtils.dp2px(15f),
            0))
        emptyView = EmptyView(requireContext())
        emptyView!!.setEmptyImage(R.mipmap.ic_empty_un_notice)
        emptyView!!.setEmptyHint(R.string.no_notice_yet)
    }

    override fun initData() {
        presenter = SystemNoticePresenter(this)
        mAdapter = SystemMessageAdapter()
        recycler!!.adapter = mAdapter
        mAdapter!!.setEmptyView(emptyView!!)
        mAdapter!!.loadMoreModule.setOnLoadMoreListener(this)
        mAdapter!!.loadMoreModule.isAutoLoadMore = false
        mAdapter!!.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        mAdapter!!.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            val data = mAdapter!!.data[position]
            if (data.detail_type == 1) {
                val link = data.link
                if (TextUtils.isEmpty(link)) {
                    return@setOnItemClickListener
                }
                WebViewActivity.navigation(requireContext(), link)
            } else {
                SystemMessageDetailActivity.goTo(activity, data.title, data.message, data.created_at)
            }

            // 设置消息已读
            if (data.is_read != 1) {
                data.is_read = 1
                mAdapter!!.notifyItemChanged(position)
                id.add(data.id)
                presenter!!.setMessageRead(id)
            }
        }
    }

    override fun loadOnVisible() {
        onRefresh()
    }

    override fun bindListener() {
        swipe_layout!!.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        swipe_layout!!.isRefreshing = true
        page = Constant.DEFAULT_FIRST_PAGE
        presenter!!.getMessageList(page)
    }

    override fun onLoadMore() {
        page++
        presenter!!.getMessageList(page)
    }

    override fun bindList(data: List<SystemMessageResponse>, isLastPage: Boolean) {
        if (swipe_layout!!.isRefreshing) {
            swipe_layout!!.isRefreshing = false
        }
        val loadMoreModule = mAdapter!!.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                emptyView.showEmptyContainer()
                loadMoreModule.loadMoreComplete()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter!!.setNewInstance(data.toMutableList())
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter!!.addData(data)
            if (isLastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }
}