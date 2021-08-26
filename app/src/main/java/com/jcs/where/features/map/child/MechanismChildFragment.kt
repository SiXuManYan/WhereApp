package com.jcs.where.features.map.child

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.map.MechanismAdapter
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * Created by Wangsw  2021/8/26 14:27.
 * 机构列表
 */
class MechanismChildFragment : BaseMvpFragment<MechanismChildPresenter>(), MechanismChildView, OnItemClickListener {


    private lateinit var mAdapter: MechanismAdapter
    private lateinit var emptyView: EmptyView
    public var categoryId = ""


    override fun getLayoutId() = R.layout.single_recycler_view

    override fun initView(view: View?) {


        emptyView = EmptyView(context).apply {
            initEmpty(
                R.mipmap.ic_empty_search, R.string.empty_search,
                R.string.empty_search_hint, R.string.back
            ) {

            }
            action_tv.visibility = View.GONE
        }
        mAdapter = MechanismAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@MechanismChildFragment)
        }

        content_rv.adapter = mAdapter
    }

    override fun initData() {
        presenter = MechanismChildPresenter(this)
        presenter.getData(categoryId, "")
    }

    override fun bindListener() {

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    override fun bindData(response: MutableList<MechanismResponse>) {
        if (response.isEmpty()) {
            emptyView.showEmptyContainer()
            return
        }
        emptyView.hideEmptyContainer()
        mAdapter.setNewInstance(response)
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        if (baseEvent.code == EventCode.EVENT_REFRESH_MECHANISM) {
            presenter.getData(categoryId, baseEvent.message)
        }


    }


}