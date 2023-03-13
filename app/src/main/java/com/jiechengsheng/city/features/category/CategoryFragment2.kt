package com.jiechengsheng.city.features.category

import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.category.edit.CategoryEditActivity
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_category_2.*

/**
 * Created by Wangsw  2021/4/15 15:12.
 *
 */
class CategoryFragment2 : BaseMvpFragment<CategoryPresenter>(), CategoryView {


    private lateinit var mAdapter: CategoryAdapter2

    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.fragment_category_2

    override fun initView(view: View?) {
        view?.let {
            BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.title_rl))
        }

        swipe_layout.setOnRefreshListener {
            presenter.getCategoryList()
        }

        emptyView = EmptyView(requireContext()).apply {
            showEmptyDefault()
        }

        mAdapter = CategoryAdapter2().apply {
            setEmptyView(emptyView)
        }

        recycler_view.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(5f), 0, 0))
        }


    }

    override fun initData() {
        presenter = CategoryPresenter(this)

    }

    override fun loadOnVisible() {
        presenter.getCategoryList()
    }

    override fun bindListener() {
        more_category_tv.setOnClickListener {
            startActivity(CategoryEditActivity::class.java)
        }
    }

    override fun bindData(response: ArrayList<Category>) {
        swipe_layout.isRefreshing = false
        if (response.isEmpty()) {
            emptyView.showEmptyContainer()
        }
        mAdapter.setNewInstance(response)
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        when (baseEvent.code) {
            EventCode.EVENT_ADD_NEW_CATEGORY -> {
                val arrayList = baseEvent.data as ArrayList<Category>
                val result = ArrayList<Category>()
                arrayList.forEach {
                    if (!mAdapter.data.contains(it)) {
                        result.add(it)
                    }
                }
                if (result.isNotEmpty()) {
                    mAdapter.addData(result)
                }
            }

            EventCode.EVENT_CUT_NEW_CATEGORY -> {
                val arrayList = baseEvent.data as ArrayList<Category>
                arrayList.forEach {
                    if (mAdapter.data.contains(it)) {
                        mAdapter.remove(it)
                    }
                }
            }
            else -> {
            }
        }


    }

    override fun onError(errorResponse: ErrorResponse?) {
        swipe_layout.isRefreshing = false
        super.onError(errorResponse)
        emptyView.showNetworkError {
            swipe_layout.isRefreshing = true
            presenter.getCategoryList()
        }
    }


}