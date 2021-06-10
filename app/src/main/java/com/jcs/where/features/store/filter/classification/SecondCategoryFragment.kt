package com.jcs.where.features.store.filter.classification

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.area.AreaResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.single_recycler_view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2021/6/9 15:04.
 * 二级分类
 */
class SecondCategoryFragment : BaseMvpFragment<SecondCategoryPresenter>(), SecondCategoryView, OnItemClickListener {

    private var pid = 0
    lateinit var mAdapter: SecondCategoryAdapter

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



    override fun getLayoutId() = R.layout.single_recycler_view

    override fun initView(view: View) {
        arguments?.let {
            pid = it.getInt(Constant.PARAM_PID, 0)
        }

        mAdapter = SecondCategoryAdapter()
        mAdapter.setOnItemClickListener(this@SecondCategoryFragment)
        content_rv.adapter = mAdapter
    }

    override fun initData() {
        presenter = SecondCategoryPresenter(this)
        presenter.getSecondCategory(pid)
    }

    override fun bindListener() = Unit

    override fun bindData(response: ArrayList<Category>) = mAdapter.setNewInstance(response)

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mAdapter.data .forEachIndexed { index, category ->
            category.nativeIsSelected = index == position
        }
        mAdapter.notifyDataSetChanged()
        val category = mAdapter.data[position]
        EventBus.getDefault().post(BaseEvent(category))
    }

}