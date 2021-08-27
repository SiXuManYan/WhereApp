package com.jcs.where.features.map.government

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.flyco.tablayout.listener.OnTabSelectListener
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.map.MechanismPagerAdapter
import com.jcs.where.features.store.filter.ThirdCategoryAdapter
import kotlinx.android.synthetic.main.activity_government.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2021/8/24 17:04.
 * 政府机构
 */
class GovernmentActivity : BaseMvpActivity<GovernmentPresenter>(), GovernmentView {


    // 内容和 tab二级分类
    private lateinit var mPagerAdapter: MechanismPagerAdapter

    // 三级分类
    private lateinit var mChildTagAdapter: ThirdCategoryAdapter

    override fun getLayoutId() = R.layout.activity_government

    override fun isStatusDark() = true

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        mPagerAdapter = MechanismPagerAdapter(supportFragmentManager)
        mChildTagAdapter = ThirdCategoryAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val child = mChildTagAdapter.data
                child.forEachIndexed { index, category ->
                    category.nativeIsSelected = index == position
                }
                mChildTagAdapter.notifyDataSetChanged()

                // 刷新筛选列表
                val target = mChildTagAdapter.data[position]
                EventBus.getDefault().post(BaseEvent<String>(EventCode.EVENT_REFRESH_MECHANISM, target.id.toString()))

            }
        }
        child_tag_rv.apply {
            adapter = mChildTagAdapter
            layoutManager = LinearLayoutManager(this@GovernmentActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun initData() {
        presenter = GovernmentPresenter(this)
        presenter.getGovernmentChildCategory()
    }

    override fun bindListener() {

        back_iv.setOnClickListener { finish() }

        tabs_type.setOnTabSelectListener(object : OnTabSelectListener {

            override fun onTabReselect(position: Int) = Unit

            override fun onTabSelect(position: Int) {
                mChildTagAdapter.setNewInstance(null)
                val category = mPagerAdapter.category
                if (category.isEmpty()) {
                    return
                }
                val child = category[position]
                if (child.has_children == 2 && child.child_categories.isNotEmpty()) {
                    mChildTagAdapter.setNewInstance(child.child_categories)
                }
            }
        })
    }

    override fun bindGovernmentChildCategory(response: ArrayList<Category>) {

        // pager
        mPagerAdapter.category.addAll(response)
        mPagerAdapter.notifyDataSetChanged()
        content_vp.offscreenPageLimit = response.size
        content_vp.adapter = mPagerAdapter
        tabs_type.setViewPager(content_vp)

    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        if (baseEvent.code == EventCode.EVENT_SET_LIST_TOTAL_COUNT) {
            val count = baseEvent.message
            total_count_tv.text = getString(R.string.total_list_count_format, count)
        }


    }


}