package com.jcs.where.features.category.edit

import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.category.CategoryAdapter2
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


    private lateinit var mSelectedAdapter: CategoryAdapter2
    private lateinit var mUnSelectedAdapter: CategoryAdapter2

    /**
     * 用户在此页面新增的关注的分类
     */
    var newAddCategory = ArrayList<Category>()

    /**
     * 用户在此页面删除的分类
     */
    var newCutCategory = ArrayList<Category>()


    override fun getLayoutId() = R.layout.activity_category_edit


    override fun initView() {

        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.blue_377BFF))

        swipe_layout.setOnRefreshListener {
            presenter.getEditableCategory()
        }

        mSelectedAdapter = CategoryAdapter2().apply {
            setEmptyView(EmptyView(this@CategoryEditActivity).apply {
                showEmptyNothing()
                addEmptyList(this)
            })
            showEditButton(true)
            addChildClickViewIds(R.id.cut_iv)

            setOnItemChildClickListener { _, _, position ->
                VibrateUtils.vibrate(20)
                val category = this.data[position]

                category.follow_status = false

                // 本地存储
                newAddCategory.remove(category)
                newCutCategory.add(category)

                // 未关注列表增加这条数据
                mUnSelectedAdapter.addData(category)

                this.removeAt(position)
            }

        }
        mUnSelectedAdapter = CategoryAdapter2().apply {
            setEmptyView(EmptyView(this@CategoryEditActivity).apply {
                showEmptyNothing()
                addEmptyList(this)
            })
            showEditButton(true)
            addChildClickViewIds(R.id.add_iv)

            setOnItemChildClickListener { _, _, position ->
                VibrateUtils.vibrate(20)

                val category = this.data[position]
                category.follow_status = true

                // 本地存储
                newAddCategory.add(category)
                newCutCategory.remove(category)

                // 已关注列表增加这条数据
                mSelectedAdapter.addData(category)

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
        close_tv.setOnClickListener {
            VibrateUtils.vibrate(20)
            finish()
        }
        finish_tv.setOnClickListener {
            VibrateUtils.vibrate(20)
            presenter.followCategory(mSelectedAdapter.data)
        }
    }

    private fun getItemDecoration(): DividerDecoration {
        return DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(5f), 0, 0).apply {
            setDrawHeaderFooter(false)
        }
    }

    override fun bindFollowData(follow: ArrayList<Category>){
        mSelectedAdapter.setNewInstance(follow)
        swipe_layout.isRefreshing = false
    }


    override fun bindUnFollowData(unFollow: ArrayList<Category>) {
        mUnSelectedAdapter.setNewInstance(unFollow)
        swipe_layout.isRefreshing = false
    }

    override fun followSuccess() {
        ToastUtils.showShort("Successful operation")

        if (newAddCategory.isNotEmpty()) {
            EventBus.getDefault().post(BaseEvent<ArrayList<Category>>(EventCode.EVENT_ADD_NEW_CATEGORY, newAddCategory))
        }
        if (newCutCategory.isNotEmpty()) {
            EventBus.getDefault().post(BaseEvent<ArrayList<Category>>(EventCode.EVENT_CUT_NEW_CATEGORY, newCutCategory))
        }
        finish()
    }

    override fun onError(errorResponse: ErrorResponse?) {
        swipe_layout.isRefreshing = false
        super.onError(errorResponse)
    }

}