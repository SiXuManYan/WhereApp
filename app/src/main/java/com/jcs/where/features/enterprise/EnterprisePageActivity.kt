package com.jcs.where.features.enterprise

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jcs.where.R
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.enterprise.adapter.EnterpriseCategoryAdapter
import com.jcs.where.features.enterprise.adapter.EnterpriseThirdCategoryAdapter
import com.jcs.where.features.map.MechanismAdapter
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_enterprise_page.*


/**
 * Created by Wangsw  2022/9/13 16:20.
 * 企业黄页
 */
class EnterprisePageActivity : BaseMvpActivity<EnterprisePagePresenter>(), EnterprisePageView {


    /** 企业所有黄页一级分类id */
    val firstCategoryIds = ArrayList<Int>()

    /** 用户当前选中的 分类id */
    var currentCategoryId = 0

    /** 内容列表筛选 1 推荐 0 距离最近 */
    private var recommend = TYPE_RECOMMEND

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var emptyView: EmptyView

    private lateinit var mFirstAdapter: EnterpriseCategoryAdapter
    private lateinit var mSecondAdapter: EnterpriseCategoryAdapter
    private lateinit var mThirdAdapter: EnterpriseThirdCategoryAdapter
    private lateinit var mAdapter: MechanismAdapter


    companion object {


        /** 推荐 */
        val TYPE_RECOMMEND = 1

        /** 距离最近 */
        val TYPE_DISTANCE = 0

        /**
         * 打开企业黄页
         * @param allYellowPageFirstCategoryId 企业所有黄页一级分类id
         *                                     首页金刚区点击时可直接传，其他位置可不传，会从sp中获取
         *
         * @param currentCategoryId 当前选中的分类id（用于高亮）
         */
        fun navigation(context: Context, allYellowPageFirstCategoryId: ArrayList<Int>? = null, currentCategoryId: Int? = null) {

            val first = ArrayList<Int>()
            if (!allYellowPageFirstCategoryId.isNullOrEmpty()) {
                // 首页直接传递
                first.addAll(allYellowPageFirstCategoryId)
            } else {
                // 从sp中获取 黄页所有一级分类id
                val firstIds = CacheUtil.getShareDefault().getString(Constant.SP_YELLOW_PAGE_ALL_FIRST_CATEGORY_ID, "")
                if (!firstIds.isNullOrBlank()) {
                    val fromJson = Gson().fromJson<ArrayList<Int>>(firstIds, object : TypeToken<ArrayList<Int>>() {}.type)
                    first.addAll(fromJson)
                }
            }

            val bundle = Bundle().apply {
                putIntegerArrayList(Constant.PARAM_DATA, first)
                currentCategoryId?.let {
                    putInt(Constant.PARAM_CATEGORY_ID, currentCategoryId)
                }
            }

            val intent = Intent(context, EnterprisePageActivity::class.java)
                .putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_enterprise_page

    override fun initView() {

        initExtra()
        initFilter()
        initContent()
    }


    private fun initExtra() {

        intent.extras?.let {
            val elements = it.getIntegerArrayList(Constant.PARAM_DATA)
            if (!elements.isNullOrEmpty()) {
                firstCategoryIds.addAll(elements)
            }
            // 从分类页传递过来的二级分类，高亮
            currentCategoryId = it.getInt(Constant.PARAM_CATEGORY_ID, 0)

        }
    }

    /** 筛选项相关 */
    @SuppressLint("NotifyDataSetChanged")
    private fun initFilter() {

        // 一、二、三级分类列表
        mFirstAdapter = EnterpriseCategoryAdapter().apply {
            isFirstCategoryList = true
        }
        mSecondAdapter = EnterpriseCategoryAdapter()
        mThirdAdapter = EnterpriseThirdCategoryAdapter()
        first_category_rv.apply {
            adapter = mFirstAdapter
            layoutManager = LinearLayoutManager(this@EnterprisePageActivity, LinearLayoutManager.VERTICAL, false)
        }

        second_category_rv.apply {
            adapter = mSecondAdapter
            layoutManager = LinearLayoutManager(this@EnterprisePageActivity, LinearLayoutManager.VERTICAL, false)
        }

        third_category_rv.apply {
            adapter = mThirdAdapter
            layoutManager = LinearLayoutManager(this@EnterprisePageActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(8f), 0, 0))
        }

        // 处理点击交互
        // 一级
        mFirstAdapter.setOnItemClickListener { _, _, position ->
            val data = mFirstAdapter.data
            val category = data[position]

            data.forEach {
                it.nativeIsSelected = it.id == category.id
            }
            mFirstAdapter.notifyDataSetChanged()

            // 填充二级
            mSecondAdapter.setNewInstance(category.child_categories)
            second_category_rv.visibility = View.VISIBLE

            // 隐藏三级
            mThirdAdapter.setNewInstance(null)

            // 刷新列表
            selectedCategory(category, position, is2ndOr3rdCategory = false, ignoreVisibility = false)
        }

        // 二级
        mSecondAdapter.setOnItemClickListener { _, _, position ->
            val data = mSecondAdapter.data
            val category = data[position]

            data.forEach {
                it.nativeIsSelected = it.id == category.id
            }
            mSecondAdapter.notifyDataSetChanged()

            // 填充三级
            val list = category.child_categories
            mThirdAdapter.setNewInstance(list)
            if (list.isNullOrEmpty()) {
                third_category_rv.visibility = View.GONE
            } else {
                third_category_rv.visibility = View.VISIBLE
            }

            // 刷新列表
            selectedCategory(category, position, is2ndOr3rdCategory = true)
        }

        // 三级
        mThirdAdapter.setOnItemClickListener { _, _, position ->
            val data = mThirdAdapter.data
            val category = data[position]

            data.forEach {
                it.nativeIsSelected = it.id == category.id
            }
            mThirdAdapter.notifyDataSetChanged()
            // 刷新列表
            selectedCategory(category, position, is2ndOr3rdCategory = true, ignoreVisibility = true)
        }


    }

    /**
     * 根据分类刷新内容列表
     * @param category                选中的分类
     * @param position                坐标
     * @param is2ndOr3rdCategory 是否为二级三级分类
     *                                当点击二级三级分类时，文案取父级分类name
     * @param ignoreVisibility        忽略处理筛选框是否可见
     */
    private fun selectedCategory(
        category: Category,
        position: Int,
        is2ndOr3rdCategory: Boolean? = false,
        ignoreVisibility: Boolean? = false,
    ) {


        if (is2ndOr3rdCategory != null && is2ndOr3rdCategory && position == 0) {
            category_tv.text = category.parentName
        } else {
            category_tv.text = category.name
        }

        category_tv.isChecked = true
        currentCategoryId = category.id
        onRefresh()
        if (ignoreVisibility == null || !ignoreVisibility) {
            handleFilterVisibility(0)
        }


    }


    /** 内容列表 */
    private fun initContent() {
        mechanismRefresh.setOnRefreshListener(this)

        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            addEmptyList(this)
        }

        mAdapter = MechanismAdapter().apply {
            ignoreTopMargin = true
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@EnterprisePageActivity)
            setOnItemClickListener { _, _, position ->
                val mechanismResponse = mAdapter.data[position]
                MechanismActivity.navigation(this@EnterprisePageActivity, mechanismResponse.id)
            }
        }

        mechanismRecycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@EnterprisePageActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0))
        }


    }


    override fun initData() {
        presenter = EnterprisePagePresenter(this)
        presenter.getAllCategory(firstCategoryIds, currentCategoryId)
        presenter.getData(page, currentCategoryId, recommend)
    }

    override fun bindListener() {
        mJcsTitle.setFirstRightIvClickListener {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_TYPE, 1)
                putString(Constant.PARAM_CATEGORY_ID, currentCategoryId.toString())
            }
            startActivity(SearchAllActivity::class.java, bundle)
        }

        category_filter_ll.setOnClickListener {
            handleFilterVisibility(0)

        }
        other_filter_ll.setOnClickListener {
            handleFilterVisibility(1)
        }
        dismiss_view.setOnClickListener {
            filter_ll.visibility = View.GONE
            firstArrowIv.rotation = 0f
            thirdArrowIv.rotation = 0f
        }

        sort_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.recommend_rb -> {
                    recommend = TYPE_RECOMMEND
                    dismiss_view.performClick()
                    other_filter_content_tv.setText(R.string.recommend_home)
                    onRefresh()
                }
                R.id.distance_rb -> {
                    recommend = TYPE_DISTANCE
                    dismiss_view.performClick()
                    other_filter_content_tv.setText(R.string.nearest)
                    onRefresh()
                }
                else -> {}
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindCategory(response: ArrayList<Category>) {
        mFirstAdapter.setNewInstance(response)

        if (currentCategoryId != 0) {
            var firstPosition = 0
            var secondPosition = 0

            // 查找选中分类，在的一级、二级分类中的坐标
            response.forEachIndexed first@{ firstIndex, first ->
                first.child_categories.forEachIndexed { secondIndex, second ->

                    if (second.id == currentCategoryId) {
                        firstPosition = firstIndex
                        secondPosition = secondIndex
                        return@first
                    }
                }
            }

            // 高亮
            if (firstPosition != 0) {

                val data = mFirstAdapter.data

                // 高亮所属一级分类
                data.forEachIndexed { index, category ->
                    category.nativeIsSelected = index == firstPosition
                }
                mFirstAdapter.notifyDataSetChanged()

                // 设置选中id所属二级分类列表
                mSecondAdapter.setNewInstance(data[firstPosition].child_categories)
                second_category_rv.visibility = View.VISIBLE

                // 高亮二级分类
                val second = mSecondAdapter.data[secondPosition]
                second.nativeIsSelected = true
                mSecondAdapter.notifyItemChanged(secondPosition)
                // 设置标题
                category_tv.text = second.name

                // 显示三级分类
                mThirdAdapter.setNewInstance(second.child_categories)
                if (mThirdAdapter.data.isNullOrEmpty()) {
                    third_category_rv.visibility = View.GONE
                } else {
                    third_category_rv.visibility = View.VISIBLE
                }

            }


        }

    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE;
        presenter.getData(page, currentCategoryId, recommend)
    }

    override fun onLoadMore() {
        page++
        presenter.getData(page, currentCategoryId, recommend)
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


    private fun handleFilterVisibility(currentPosition: Int) {
        if (filter_ll.visibility == View.GONE) {
            filter_ll.visibility = View.VISIBLE

            if (currentPosition == 0) {
                category_parent_ll.visibility = View.VISIBLE
                sort_rg.visibility = View.GONE
            } else {
                category_parent_ll.visibility = View.GONE
                sort_rg.visibility = View.VISIBLE
            }

        } else {

            if (filter_content_sw.getChildAt(currentPosition).visibility == View.VISIBLE) {
                filter_ll.visibility = View.GONE
            } else {

                if (currentPosition == 0) {
                    category_parent_ll.visibility = View.VISIBLE
                    sort_rg.visibility = View.GONE
                } else {
                    category_parent_ll.visibility = View.GONE
                    sort_rg.visibility = View.VISIBLE
                }
            }
        }

        if (filter_ll.visibility == View.GONE) {
            firstArrowIv.rotation = 0f
            thirdArrowIv.rotation = 0f
        } else {
            if (currentPosition == 0) {
                firstArrowIv.rotation = 180f
            } else {
                thirdArrowIv.rotation = 180f
            }
        }

    }

}