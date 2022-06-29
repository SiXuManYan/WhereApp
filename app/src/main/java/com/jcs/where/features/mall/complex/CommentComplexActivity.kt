package com.jcs.where.features.mall.complex

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelComment
import com.jcs.where.api.response.mall.MallCommentCount
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.hotel.comment.child.HotelCommentAdapter
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_refresh_list.*

/**
 * Created by Wangsw  2022/5/19 15:43.
 * 商城评价
 */
class CommentComplexActivity : BaseMvpActivity<CommentComplexPresenter>(), CommentComplexView {


    /**
     * 列表类型  1 2 3 4 5
     * 6最新 7有图 8低分 0全部
     */
    private var listType = 0

    /** 商品id,酒店id */
    private var businessId = 0

    /** 0 eStore 1 酒店 */
    private var useType = 0


    private lateinit var header: MallCommentHeader


    private lateinit var mAdapter: HotelCommentAdapter

    private lateinit var emptyView: EmptyView
    private var page = Constant.DEFAULT_FIRST_PAGE

    companion object {

        var USE_TYPE_STORE = 0
        var USE_TYPE_HOTEL = 1

        var TYPE_ALL = 0
        var TYPE_1 = 1
        var TYPE_2 = 2
        var TYPE_3 = 3
        var TYPE_4 = 4
        var TYPE_5 = 5

        var TYPE_NEWEST = 6
        var TYPE_IMAGE = 7
        var TYPE_LOW = 8


        fun navigation(context: Context, goodId: Int, useType: Int? = 0) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, goodId)
                useType?.let {
                    putInt(Constant.PARAM_TYPE, it)
                }
            }

            val intent = if (User.isLogon()) {
                Intent(context, CommentComplexActivity::class.java).putExtras(bundle)
            } else {
                Intent(context, LoginActivity::class.java)
            }

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

    }

    override fun isStatusDark() = true


    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {

        initExtra()
        mJcsTitle.setMiddleTitle(getString(R.string.evaluation))

        initList()

    }

    private fun initList() {
        swipe_layout.setOnRefreshListener(this)
        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_refund_comment)
            setEmptyHint(R.string.no_cmments_yet)
        }

        mAdapter = HotelCommentAdapter().apply {
            footerWithEmptyEnable = true
            headerWithEmptyEnable = true
            singleLineImage = false
            isDiamond = (useType == USE_TYPE_HOTEL)

            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener(this@CommentComplexActivity)
            setEmptyView(emptyView)
        }
        recycler.adapter = mAdapter


        header = MallCommentHeader(this).apply {
            initData((useType == USE_TYPE_HOTEL))
            containerClickListener = this@CommentComplexActivity
        }
        mAdapter.addHeaderView(header)


    }

    private fun initExtra() {
        businessId = intent.getIntExtra(Constant.PARAM_ID, 0)
        useType = intent.getIntExtra(Constant.PARAM_TYPE, 0)

    }

    override fun initData() {
        presenter = CommentComplexPresenter(this)
        presenter.loadCount(businessId, useType)
        loadData()
    }

    override fun bindListener() = Unit

    override fun onRefresh() {
        swipe_layout.isRefreshing = true
        page = Constant.DEFAULT_FIRST_PAGE
        loadData()
    }

    override fun onLoadMore() {
        page++
        loadData()
    }

    private fun loadData() {
        presenter.getCommentList(businessId, page, listType, useType)
    }

    override fun onClick(it: View) {
        when (it.id) {
            R.id.all_ll -> listType = TYPE_ALL
            R.id.newest_ll -> listType = TYPE_NEWEST
            R.id.has_image_ll -> listType = TYPE_IMAGE
            R.id.star_five_ll -> listType = TYPE_5
            R.id.star_four_ll -> listType = TYPE_4
            R.id.star_three_ll -> listType = TYPE_3
            R.id.star_two_ll -> listType = TYPE_2
            R.id.star_one_ll -> listType = TYPE_1
        }
        mAdapter.setNewInstance(null)
        page = Constant.DEFAULT_FIRST_PAGE
        loadData()

    }


    override fun bindData(data: MutableList<HotelComment>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyDefault()
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

    override fun bindCount(response: MallCommentCount) {
        header.setNumber(response)
    }


}