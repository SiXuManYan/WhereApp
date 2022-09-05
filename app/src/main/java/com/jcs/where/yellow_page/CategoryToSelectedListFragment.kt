package com.jcs.where.yellow_page

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.CategoryResponse
import com.jcs.where.base.BaseFragment

/**
 * 选择分类的列表
 * create by zyf on 2021/1/3 8:06 PM
 */
class CategoryToSelectedListFragment : BaseFragment() {


    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: Adapter
    private var mListener: OnItemClickListener? = null

    /**
     * index = 0：一级分类选中的item
     * index = 1：二级分类选中的item
     * index = 2：三级分类选中的item
     */
    private var mSelectPosition: ArrayList<Int?> = ArrayList<Int?>()
    private var mTotalCategories: List<CategoryResponse>? = null
    private var mFirstLevelTotalIds = ""
    val defaultChildCategoryId: String? = null

    /**
     * 默认分类等级是一级分类
     * 注意，这个分类等级只是对应企业黄页的选项
     * 不是对应分类API的level参数
     */
    private var mLevel = -1
    override fun initView(view: View) {
        mRecyclerView = view.findViewById(R.id.recycler)
        mRecyclerView.setLayoutManager(LinearLayoutManager(context))
    }

    override fun initData() {
        mAdapter = Adapter()
        mRecyclerView!!.adapter = mAdapter
        mSelectPosition = ArrayList()
        mSelectPosition.add(null)
        mSelectPosition.add(null)
        mSelectPosition.add(null)
    }

    override fun bindListener() {
        mAdapter!!.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<*, *>, view: View, position: Int ->
            onItemClicked(baseQuickAdapter,
                view,
                position)
        }
    }

    private fun onItemClicked(baseQuickAdapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        // 当前展示的数据是什么level
        when (mLevel) {
            LEVEL_FIRST -> {
                // 存储一级分类选中状态，选择了哪个item，并重置下级分类的选择状态
                mSelectPosition!![0] = position
                mSelectPosition!![1] = null
                mSelectPosition!![2] = null
            }
            LEVEL_SECOND -> {
                // 存储二级分类选中状态，选择了哪个item，并重置下级分类的选择状态
                mSelectPosition!![1] = position
                mSelectPosition!![2] = null
            }
            LEVEL_THIRD ->                 // 存储三级分类选中状态，选择了哪个item
                mSelectPosition!![2] = position
        }
        val categoryResponse = mAdapter!!.data[position]
        mListener!!.onItemClicked(mLevel, categoryResponse)
    }

    fun setData(categoryList: List<CategoryResponse>, level: Int) {
        mAdapter!!.setNewInstance(categoryList.toMutableList())
        mLevel = level
    }

    fun setTotalCategories(totalCategories: List<CategoryResponse>?) {
        mTotalCategories = totalCategories
    }

    fun setFirstLevelTotalIds(firstLevelTotalIds: String) {
        mFirstLevelTotalIds = firstLevelTotalIds
    }

    fun setDefaultChildCategoryId(defaultChildCategoryId: String) {
        val size = mTotalCategories!!.size
        for (i in 0 until size) {
            val first = mTotalCategories!![i]
            if (first.id == defaultChildCategoryId) {
                mSelectPosition!![0] = i
                mLevel = LEVEL_FIRST
                return
            }
            val secondCategories = first.child_categories
            for (j in secondCategories.indices) {
                val second = secondCategories[j]
                if (second.id == defaultChildCategoryId) {
                    mSelectPosition!![0] = i
                    mSelectPosition!![1] = j
                    mLevel = LEVEL_SECOND
                    return
                }
                val thirdCategories = second.child_categories
                for (k in thirdCategories.indices) {
                    val third = thirdCategories[k]
                    if (third.id == defaultChildCategoryId) {
                        mSelectPosition!![0] = i
                        mSelectPosition!![1] = j
                        mSelectPosition!![2] = k
                        mLevel = LEVEL_THIRD
                        return
                    }
                }
            }
        }
        Log.e("CategoryFragment", "setDefaultChildCategoryId: " + "")
    }

    fun setListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    val isFirstLevel: Boolean
        get() = mLevel == LEVEL_FIRST
    val isSecondLevel: Boolean
        get() = mLevel == LEVEL_SECOND
    val isThirdLevel: Boolean
        get() = mLevel == LEVEL_THIRD

    /**
     * level 为 -1，说明还没有点击选择子分类，应该展示全部数据
     *
     * @return
     */
    val isNoLevel: Boolean
        get() = mLevel == -1
    val selectFirstPosition: Int?
        get() = mSelectPosition!![0]
    val selectSecondPosition: Int?
        get() = mSelectPosition!![1]
    val selectThirdPosition: Int?
        get() = mSelectPosition!![2]
    val selectFirstCate: CategoryResponse?
        get() {
            val selectFirstPosition = selectFirstPosition ?: return null
            return mTotalCategories!![selectFirstPosition]
        }
    val selectSecondCate: CategoryResponse?
        get() {
            val selectFirstCate = selectFirstCate
            val selectSecondPosition = selectSecondPosition
            if (selectFirstCate == null || selectSecondPosition == null) {
                return null
            }
            val child_categories = selectFirstCate.child_categories
            return if (child_categories.isEmpty() || selectSecondPosition >= child_categories.size) {
                null
            } else child_categories[selectSecondPosition]
        }
    val selectThirdCate: CategoryResponse?
        get() {
            val selectSecondCate = selectSecondCate
            val selectThirdPosition = selectThirdPosition
            if (selectSecondCate == null || selectThirdPosition == null) {
                return null
            }
            val child_categories = selectSecondCate.child_categories
            return if (child_categories.isEmpty() || selectThirdPosition >= child_categories.size) {
                null
            } else child_categories[selectThirdPosition]
        }

    /**
     * 获得当前分类id
     *
     * @return 分类id字符串，可能是 "1" "[1,2,3]"
     */
    val currentCategoryId: String
        get() {
            when (mLevel) {
                LEVEL_FIRST -> {
                    return if (selectFirstCate == null) {
                        "[10,17,21,27,94,114,209,226]"
                    } else selectFirstCate!!.id.toString()
                }
                LEVEL_SECOND -> {
                    val selectSecondCate = selectSecondCate
                    return selectSecondCate?.id?.toString() ?: ""
                }
                LEVEL_THIRD -> {
                    val selectThirdCate = selectThirdCate
                    return selectThirdCate?.id?.toString() ?: ""
                }
            }
            return mFirstLevelTotalIds
        }

    override fun getLayoutId(): Int {
        return R.layout.fragmeng_category_list
    }

    fun notifyAdapter() {
        mAdapter!!.notifyDataSetChanged()
    }

    fun unSelectSecond() {
        if (mSelectPosition != null) {
            mSelectPosition!![1] = null
        }
    }

    fun unSelectThird() {
        if (mSelectPosition != null) {
            mSelectPosition!![2] = null
        }
    }

    internal inner class Adapter : BaseQuickAdapter<CategoryResponse, BaseViewHolder>(R.layout.item_category_to_selected_list) {
        override fun convert(baseViewHolder: BaseViewHolder, categoryResponse: CategoryResponse) {
            baseViewHolder.setText(R.id.categoryTitleTv, categoryResponse.name)
            var selectedPosition: Int? = null
            when (mLevel) {
                LEVEL_FIRST ->                     // 获取一级分类选中的item索引
                    selectedPosition = mSelectPosition!![0]
                LEVEL_SECOND ->                     // 获取二级分类选中的item索引
                    selectedPosition = mSelectPosition!![1]
                LEVEL_THIRD ->                     // 获取三级分类选中的item索引
                    selectedPosition = mSelectPosition!![2]
            }

            // 选中状态的item
            if (selectedPosition != null && baseViewHolder.adapterPosition == selectedPosition) {
                baseViewHolder.setTextColor(R.id.categoryTitleTv, context.getColor(R.color.blue_4D9FF2))
                baseViewHolder.setGone(R.id.categoryCheckedIcon, false)
            } else {
                baseViewHolder.setGone(R.id.categoryCheckedIcon, true)
                baseViewHolder.setTextColor(R.id.categoryTitleTv, context.getColor(R.color.black_333333))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(level: Int, categoryResponse: CategoryResponse?)
    }

    companion object {
        const val LEVEL_FIRST = 1
        const val LEVEL_SECOND = 2
        const val LEVEL_THIRD = 3
    }
}