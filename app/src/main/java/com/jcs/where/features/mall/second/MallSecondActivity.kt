package com.jcs.where.features.mall.second

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_mall_second.*
import kotlinx.android.synthetic.main.layout_mall_filter.*

/**
 * Created by Wangsw  2021/12/3 15:30.
 * 新版商城二级列表
 */
class MallSecondActivity : BaseMvpActivity<MallSecondPresenter>(), MallSecondView {


    /** 当前页面对应的一级分类 */
    lateinit var targetFirstCategory: Category

    lateinit var mThirdCategoryAdapter: ThirdCategoryFilterAdapter

    override fun getLayoutId() = R.layout.activity_mall_second

    override fun initView() {

        initCategoryFilter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCategoryFilter() {

        mThirdCategoryAdapter = ThirdCategoryFilterAdapter().apply {
            setOnItemClickListener { _, _, position ->
                // 筛选
                val datas = mThirdCategoryAdapter.data
                datas.forEach {
                    it.nativeIsSelected = false
                }
                notifyDataSetChanged()

                val category = data[position]
                category.nativeIsSelected
                notifyItemChanged(position)
                // toto 筛选

            }
        }
        category_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mThirdCategoryAdapter
        }
    }

    override fun initData() {
        presenter = MallSecondPresenter(this)
    }

    override fun bindListener() {
        dismiss_view.setOnClickListener {
            filter_container_ll.visibility = View.GONE
        }

        complex_ll.setOnClickListener {
            if (filter_container_ll.visibility == View.VISIBLE) {
                filter_container_ll.visibility = View.GONE
                complex_tv.isChecked = false
                sales_tv.isChecked = false
                other_tv.isChecked = false
                complex_iv.rotation = 0f
            } else {
                filter_container_ll.visibility = View.VISIBLE
                complex_tv.isChecked = true
                complex_iv.rotation = 180f
                sales_tv.isChecked = false
                other_tv.isChecked = false
            }
        }

        sales_tv.setOnClickListener {
            if (filter_container_ll.visibility == View.VISIBLE) {
                filter_container_ll.visibility = View.GONE
                complex_tv.isChecked = false
            }
            sales_tv.isChecked = !sales_tv.isChecked
            // todo 按销量筛选

        }
    }
}