package com.jcs.where.features.coupon.good.child

import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.mall.request.MallGoodListRequest
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.mall.home.child.MallRecommendAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2022/3/4 14:40.
 *
 */
class CouponGoodFragment : BaseMvpFragment<CouponGoodPresenter>(), CouponGoodView {


    /** 优惠券 id */
    var mCouponId = 0

    /** 店铺优惠券类型对应的店铺id */
    var mShopId = 0

    /** 当前页面对应的分类 */
    var mCategoryId = 0

    /** 列表请求 */
    private lateinit var goodRequest: MallGoodListRequest

    /** 商品列表 */
    private lateinit var mAdapter: MallRecommendAdapter

    private var page = Constant.DEFAULT_FIRST_PAGE


    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View?) {

        goodRequest = MallGoodListRequest().apply {
            categoryId = mCategoryId
            coupon_id = mCouponId

            if (mShopId!=0) {
                shopId = mShopId
            }
        }

        initContent()
    }

    private fun initContent() {
        swipe_layout.apply {
            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                loadOnVisible()
            }
        }

        val emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()

        mAdapter = MallRecommendAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener {
                page++
                goodRequest.page = page
                presenter.getMallList(goodRequest)
            }
        }
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0)
        recycler.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }

    }

    override fun initData() {
        presenter = CouponGoodPresenter(this)
    }

    override fun bindListener() = Unit

    override fun loadOnVisible() {
        presenter.getMallList(goodRequest)
    }

    override fun bindData(data: MutableList<MallGood>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
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


}