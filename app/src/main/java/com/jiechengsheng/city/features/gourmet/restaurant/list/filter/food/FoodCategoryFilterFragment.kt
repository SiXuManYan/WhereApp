package com.jiechengsheng.city.features.gourmet.restaurant.list.filter.food

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.view.empty.EmptyView
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/3/29 15:55.
 * 餐厅美食分类筛选
 */
class FoodCategoryFilterFragment : BaseMvpFragment<FoodCategoryFilterPresenter>(), FoodCategoryFilterView, OnItemClickListener {
    private val dataList = ArrayList<Category>()
    private  lateinit var mEmptyView: EmptyView
    private  lateinit  var mAdapter: FoodCategoryFilterAdapter
    private  lateinit  var contentRv: RecyclerView
    @JvmField
    var pid = 0
    @JvmField
    var pidName = ""
    override fun getLayoutId(): Int {
        return R.layout.single_recycler_view
    }

    override fun initView(view: View) {
        contentRv = view.findViewById(R.id.content_rv)
        mEmptyView = EmptyView(requireContext())
    }

    override fun initData() {
        presenter = FoodCategoryFilterPresenter(this)
        mAdapter = FoodCategoryFilterAdapter()
        mAdapter!!.setEmptyView(mEmptyView!!)
        mAdapter!!.setNewInstance(dataList)
        mAdapter!!.setOnItemClickListener(this)
        contentRv!!.adapter = mAdapter
    }

    override fun bindListener() {}
    override fun loadOnVisible() {
        presenter!!.getCategoriesList(pid, pidName)
    }

    override fun bindList(response: List<Category>) {
        mAdapter!!.setNewInstance(response.toMutableList())
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val list: List<Category> = mAdapter!!.data
        for (i in list.indices) {
            val data = list[i]
            if (position == i) {
                data.nativeIsSelected = true
            } else {
                data.nativeIsSelected = false
            }
        }
        mAdapter!!.notifyDataSetChanged()
        val category = mAdapter!!.data[position]
        EventBus.getDefault().post(BaseEvent(category))
    }
}