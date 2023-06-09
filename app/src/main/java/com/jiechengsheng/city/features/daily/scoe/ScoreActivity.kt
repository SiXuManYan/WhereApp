package com.jiechengsheng.city.features.daily.scoe

import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.IntegralDetailResponse
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.daily.sign.SignInRuleActivity
import com.jiechengsheng.city.features.integral.child.detail.IntegralChildAdapter
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_score.*

/**
 * Created by Wangsw  2021/11/25 18:25.
 * 我的积分
 */
class ScoreActivity : BaseMvpActivity<ScorePresenter>(), ScoreView {

    private var isToolbarDark = false

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var emptyView: EmptyView

    private lateinit var mAdapter: IntegralChildAdapter

    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_score

    override fun initView() {

        initScroll()
    }


    override fun initData() {
        presenter = ScorePresenter(this)
        presenter.getUserInfo()
        presenter.getIntegralDetailList(page)
    }

    override fun bindListener() {
        rule_tv.setOnClickListener {
            startActivity(SignInRuleActivity::class.java)
        }
    }

    private fun initScroll() {
        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_points)
            setEmptyHint(R.string.empty_points)
            addEmptyList(this)
        }

        mAdapter = IntegralChildAdapter().apply {
            setEmptyView(R.layout.view_empty_data_brvah_default)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getIntegralDetailList(page)
            }
        }
        content_rv.apply {
            isNestedScrollingEnabled = true
            adapter = mAdapter
            addItemDecoration(DividerDecoration(getColor(R.color.colorPrimary), 1, SizeUtils.dp2px(15f), 0))
            layoutManager = object : LinearLayoutManager(this@ScoreActivity, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
        }

        useView.setBackgroundColor(getColor(R.color.white))
        toolbar.setBackgroundColor(getColor(R.color.white))
        useView.background.alpha = 0
        toolbar.background.alpha = 0
        scrollView.setOnScrollChangeListener { _, _, y, _, _ ->
            val headHeight = media_fl.measuredHeight - toolbar.measuredHeight
            var alpha = (y.toFloat() / headHeight * 255).toInt()
            if (alpha >= 255) {
                alpha = 255
            }
            if (alpha <= 5) {
                alpha = 0
            }
            isToolbarDark = alpha > 130

            if (isToolbarDark) {
                back_iv.setImageResource(R.mipmap.ic_back_black)
            } else {
                back_iv.setImageResource(R.mipmap.ic_back_white)
            }
            score_title_tv.isChecked = isToolbarDark
            rule_tv.isChecked = isToolbarDark
            useView.background.alpha = alpha
            toolbar.background.alpha = alpha

            changeStatusTextColor()
        }

    }

    override fun bindUserIntegral(integral: String) {
        my_score_tv.text = integral
    }

    override fun bindDetailData(data: List<IntegralDetailResponse>, lastPage: Boolean) {

        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(data.toMutableList())
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

}