package com.jcs.where.features.complex.child

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.CategoryResponse
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.map.MechanismAdapter
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_mechanism_list.*

/**
 * Created by Wangsw  2022/9/8 18:31.
 * 综合服务子分类
 */
class ConvenienceChildFragment : BaseMvpFragment<ConvenienceChildPresenter>(), ConvenienceChildView,
    SwipeRefreshLayout.OnRefreshListener,
    OnLoadMoreListener,
    OnItemClickListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: MechanismAdapter

    private var mCategoryResponse: CategoryResponse? = null
    private val mChildCategories: ArrayList<CategoryResponse> = ArrayList();

    /**
     * 第一次展示时获取网络数据
     * 此 tag 表示是否已经获取过网络数据
     */
    private val mIsDataLoaded = false



    /** 当前列表分类id */
    private var mCurrentCategoryId = "0"


    /**
     * 1 推荐
     * 0 距离最近
     */
    private var recommend = 1

    private var needRefreshForFilter = false

    override fun getLayoutId() = R.layout.fragment_mechanism_list


    companion object {
        fun newInstance(category: CategoryResponse): ConvenienceChildFragment {
            val child = ConvenienceChildFragment()
            val apply = Bundle().apply {
                putSerializable(Constant.KEY_CATEGORY_RESPONSE, category);
            }
            child.arguments = apply
            return child
        }
    }

    override fun initView(view: View?) {

        initExtra()
        initContent()
    }

    private fun initExtra() {
        arguments?.let {
            mCategoryResponse = it.getSerializable(Constant.KEY_CATEGORY_RESPONSE) as CategoryResponse?
            mCurrentCategoryId = mCategoryResponse?.id.toString()
        }
    }

    private fun initContent() {
        mechanismRefresh.setOnRefreshListener(this@ConvenienceChildFragment)

        emptyView = EmptyView(requireContext()).apply {
            showEmptyDefault()
            addEmptyList(this)
        }
        mAdapter = MechanismAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@ConvenienceChildFragment)
            setOnItemClickListener(this@ConvenienceChildFragment)
        }

        mechanismRecycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0))
        }
    }

    override fun initData() {
        presenter = ConvenienceChildPresenter(this)
    }

    override fun bindListener() {
        mechanismRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (mechanismRefresh.isRefreshing) {
                return@setOnCheckedChangeListener
            }
            when (checkedId) {
                R.id.allRadio -> {
                    // 选择了全部
                    mCurrentCategoryId = mCategoryResponse!!.id.toString()
                    onRefresh()
                }
                else -> {
                    // 选择了其他的子分类
                    mChildCategories.forEach {

                        val categoryId = it.id.toInt()
                        if (categoryId == checkedId) {
                            mCurrentCategoryId = categoryId.toString()
                            onRefresh()
                        }
                    }
                }
            }
        }

    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE;
        presenter.getData(page, mCurrentCategoryId, recommend)
        needRefreshForFilter = false
    }


    override fun loadOnVisible() {
        onRefresh()

        // 获取子三级分类
        mCategoryResponse?.let {

            presenter.getChildCategory(mCurrentCategoryId)
        }
    }


    override fun onLoadMore() {
        page++
        presenter.getData(page, mCurrentCategoryId, recommend)

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val mechanismResponse = mAdapter.data[position]
        MechanismActivity.navigation(requireContext(), mechanismResponse.id)
    }

    override fun bindChildCategory(response: List<CategoryResponse>) {

        if (response.isNullOrEmpty()) {
            radioScroll.visibility = View.GONE
            mechanismRadioGroup.visibility = View.GONE
            return
        }
        radioScroll.visibility = View.VISIBLE
        mechanismRadioGroup.visibility = View.VISIBLE
        val allRadio = mechanismRadioGroup.getChildAt(0)
        mChildCategories.clear()
        mChildCategories.addAll(response)

        mChildCategories.forEachIndexed { index, categoryResponse ->

            val temp = RadioButton(context).apply {
                buttonDrawable = null
                setBackgroundResource(R.drawable.selector_store_third_category)
                text = categoryResponse.name;
                setTextColor(resources.getColorStateList(R.color.selector_grey_blue, null))
                layoutParams = allRadio.layoutParams
                id = Integer.parseInt(categoryResponse.id)
                setPadding(allRadio.paddingLeft, allRadio.paddingTop, allRadio.paddingRight, allRadio.paddingBottom);
                textSize = 12f
            }
            mechanismRadioGroup.addView(temp, mechanismRadioGroup.childCount)

        }

    }

    override fun bindList(data: MutableList<MechanismResponse>, lastPage: Boolean) {

        if (mechanismRefresh != null) {
            mechanismRefresh.isRefreshing = false
        }


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

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_CONVENIENCE_CHILD -> {
                val newRecommend = baseEvent.data as Int

                if (recommend != newRecommend) {
                    recommend = newRecommend
                    needRefreshForFilter = true
                    if (isViewVisible) {
                        onRefresh()
                    }
                }

            }
            else -> {}
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && needRefreshForFilter) {
            onRefresh()
        }

    }


}