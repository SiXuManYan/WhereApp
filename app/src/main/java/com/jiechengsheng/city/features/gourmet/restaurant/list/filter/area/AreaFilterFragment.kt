package com.jiechengsheng.city.features.gourmet.restaurant.list.filter.area

import android.view.View

import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.view.empty.EmptyView
import androidx.recyclerview.widget.RecyclerView
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.area.AreaResponse
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import org.greenrobot.eventbus.EventBus
import com.jiechengsheng.city.base.BaseEvent

/**
 * Created by Wangsw  2021/3/29 15:55.
 * 餐厅商业区筛选
 */
class AreaFilterFragment : BaseMvpFragment<AreaFilterPresenter?>(), AreaFilterView, OnItemClickListener {


    private lateinit var mAdapter: AreaFilterAdapter
    private lateinit var mEmptyView: EmptyView
    private lateinit var contentRv: RecyclerView

    override fun getLayoutId() = R.layout.single_recycler_view

    override fun initView(view: View) {
        contentRv = view.findViewById(R.id.content_rv)
        contentRv.setVerticalScrollBarEnabled(true)
        mEmptyView = EmptyView(requireContext())
    }

    override fun initData() {
        presenter = AreaFilterPresenter(this)
        mAdapter = AreaFilterAdapter()
        mAdapter!!.setEmptyView(mEmptyView!!)
        contentRv!!.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(this)
    }

    override fun bindListener() {}
    override fun loadOnVisible() {
        presenter!!.getAreasList()
    }

    override fun bindList(response: List<AreaResponse>) {
        mAdapter!!.setNewInstance(response.toMutableList())
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val list: List<AreaResponse> = mAdapter!!.data
        for (i in list.indices) {
            val data = list[i]
            data.nativeIsSelected = position == i
        }
        mAdapter!!.notifyDataSetChanged()
        val areaData = mAdapter!!.data[position]
        EventBus.getDefault().post(BaseEvent(areaData))
    }
}