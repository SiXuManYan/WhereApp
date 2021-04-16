package com.jcs.where.features.category.edit

import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.category.CategoryAdapter
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_category_edit.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2021/4/15 18:02.
 * 分类编辑页
 */
class CategoryEditActivity : BaseMvpActivity<CategoryEditPresenter>(), CategoryEditView {


    private lateinit var mSelectedAdapter: CategoryAdapter
    private lateinit var mUnSelectedAdapter: CategoryAdapter

    /**
     * 用户在此页面新关注的分类
     */
    var newFollowCategory = ArrayList<Category>()


    override fun getLayoutId() = R.layout.activity_category_edit


    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.blue_4C9EF2))

        mSelectedAdapter = CategoryAdapter().apply {
            setEmptyView(EmptyView(this@CategoryEditActivity).apply {
                showEmptyDefault()
            })
            showAddCategoryButton(false)
        }
        mUnSelectedAdapter = CategoryAdapter().apply {
            setEmptyView(EmptyView(this@CategoryEditActivity).apply {
                showEmptyDefault()
            })
            showAddCategoryButton(true)
            addChildClickViewIds(R.id.add_tv)
            setOnItemChildClickListener { _, _, position ->
                VibrateUtils.vibrate(20)
                val category = this.data[position]

                // 存储关注的分类
                newFollowCategory.add(category)

                // 已关注列表增加这条数据
                mSelectedAdapter.addData(category)

                // 未关注列表移除这条
                this.removeAt(position)
            }
        }

        selected_rv.apply {
            adapter = mSelectedAdapter
            addItemDecoration(getItemDecoration())
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

        unselected_rv.apply {
            adapter = mUnSelectedAdapter
            addItemDecoration(getItemDecoration())
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }


    override fun isStatusDark(): Boolean = false


    override fun initData() {
        presenter = CategoryEditPresenter(this)
        presenter.getEditableCategory()
    }

    override fun bindListener() {
        close_iv.setOnClickListener {
            VibrateUtils.vibrate(20)
            finish()
        }
        finish_tv.setOnClickListener {
            VibrateUtils.vibrate(20)
            if (newFollowCategory.isNotEmpty()) {
                presenter.followCategory(newFollowCategory)
            } else {
                finish()
            }
        }
    }

    private fun getItemDecoration(): DividerDecoration {
        return DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(5f), 0, 0).apply {
            setDrawHeaderFooter(false)
        }
    }

    override fun bindFollowData(follow: ArrayList<Category>) =
            mSelectedAdapter.setNewInstance(follow)

    override fun bindUnFollowData(unFollow: ArrayList<Category>) =
            mUnSelectedAdapter.setNewInstance(unFollow)

    override fun followSuccess() {
        ToastUtils.showShort("Successful operation")
        EventBus.getDefault().post(BaseEvent<ArrayList<Category>>(EventCode.EVENT_ADD_NEW_CATEGORY, newFollowCategory))
        finish()
    }





}