package com.jcs.where.features.store.filter.classification

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * Created by Wangsw  2021/6/9 15:04.
 * 二级分类
 */
class SecondCategoryFragment : BaseMvpFragment<SecondCategoryPresenter>(), SecondCategoryView, OnItemClickListener {

    companion object {
        /**
         * @param pid 一级分类
         */
        fun newInstance(pid: Int): SecondCategoryFragment {
            val fragment = SecondCategoryFragment()
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_PID, pid)
            }
            fragment.arguments = bundle
            return fragment
        }

    }

    lateinit var mAdapter: SecondCategoryAdapter

    override fun getLayoutId() = R.layout.single_recycler_view

    override fun initView(view: View) {
        mAdapter = SecondCategoryAdapter()
        mAdapter.setOnItemClickListener(this@SecondCategoryFragment)
        content_rv.adapter = mAdapter
    }

    override fun initData() {

    }

    override fun bindListener() {

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mAdapter.data .forEachIndexed { index, category ->
            category.nativeIsSelected = index == position
        }
        mAdapter.notifyDataSetChanged()
        val category = mAdapter.data[position]




    }

}