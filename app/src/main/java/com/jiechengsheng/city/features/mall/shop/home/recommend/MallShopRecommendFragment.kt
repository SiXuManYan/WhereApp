package com.jiechengsheng.city.features.mall.shop.home.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.Coupon
import com.jiechengsheng.city.api.response.mall.MallGood
import com.jiechengsheng.city.api.response.mall.ShopRecommend
import com.jiechengsheng.city.api.response.mall.request.MallGoodListRequest
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.coupon.center.CouponCenterAdapter
import com.jiechengsheng.city.features.mall.home.child.MallRecommendAdapter
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_mall_shop_recommend.*

/**
 * Created by Wangsw  2022/2/24 14:09.
 * 商城店铺推荐
 */
class MallShopRecommendFragment : BaseMvpFragment<ShopRecommendPresenter>(), ShopRecommendView {

    private var mShopId = 0

    private var goodRequest = MallGoodListRequest()
    private var mPage = Constant.DEFAULT_FIRST_PAGE

    /** 轮播和推荐 */
    private lateinit var mRecommendHeaderAdapter: ShopRecommendHeaderAdapter

    /** 店铺优惠券 */
    private lateinit var mCouponHeaderAdapter: CouponCenterAdapter

    /** 商品列表 */
    private lateinit var mAdapter: MallRecommendAdapter

    /** 店铺券header */
    private lateinit var couponHeader: View

    /** 推荐header */
    private lateinit var recommendHeader: View

    private lateinit var emptyView: EmptyView

    companion object {

        /**
         * @param shopId 店铺id
         */
        fun newInstance(shopId: Int): MallShopRecommendFragment {
            val fragment = MallShopRecommendFragment()

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shopId)

            }
            fragment.arguments = bundle
            return fragment
        }


    }

    override fun getLayoutId() = R.layout.fragment_mall_shop_recommend

    override fun initView(view: View?) {
        arguments?.let {
            mShopId = it.getInt(Constant.PARAM_SHOP_ID)
        }
        initContent()
        initRecommendHeader()
        initCouponHeader()
    }


    private fun initRecommendHeader() {

        recommendHeader = LayoutInflater.from(requireContext()).inflate(R.layout.layout_mall_shop_recommend_header, null, false)
        mAdapter.addHeaderView(recommendHeader)

        mRecommendHeaderAdapter = ShopRecommendHeaderAdapter()

        val header_rv = recommendHeader.findViewById<RecyclerView>(R.id.header_rv)
        val manager = object : LinearLayoutManager(context, VERTICAL, false) {
            override fun canScrollVertically() = false
        }
        header_rv.layoutManager = manager
        header_rv.adapter = mRecommendHeaderAdapter
    }


    private fun initCouponHeader() {

        couponHeader = LayoutInflater.from(requireContext()).inflate(R.layout.layout_mall_shop_recommend_header, null, false)
        mAdapter.addHeaderView(couponHeader, 0)

        mCouponHeaderAdapter = CouponCenterAdapter().apply {
            addChildClickViewIds(R.id.get_tv)
            setOnItemChildClickListener { adapter, view, position ->
                val userCoupon = mCouponHeaderAdapter.data[position]

                when (view.id) {
                    R.id.get_tv -> presenter.getShopCoupon(userCoupon.id, userCoupon.couponType)
                    else -> {}
                }
            }
        }
        val header_rv = couponHeader.findViewById<RecyclerView>(R.id.header_rv)
        val manager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun canScrollVertically() = false
        }
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(22f), 0, 0)

        header_rv.apply {
            layoutManager = manager
            adapter = mCouponHeaderAdapter
            addItemDecoration(decoration)
        }
    }


    private fun initContent() {
        emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)

        mAdapter = MallRecommendAdapter().apply {
            hideShopName = true
            headerWithEmptyEnable = true
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener {
                mPage++
                goodRequest.page = mPage
                presenter.getMallList(goodRequest)
            }

        }
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0)
        content_rv.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }

    }

    override fun initData() {
        presenter = ShopRecommendPresenter(this)
        goodRequest.apply {
            page = Constant.DEFAULT_FIRST_PAGE
            shopId = mShopId
            recommend = 1
        }
    }

    override fun loadOnVisible() {
        presenter.requestShopCoupon(mShopId)
        presenter.getRecommend(mShopId)
        presenter.getMallList(goodRequest)
    }

    override fun bindListener() {
        swipe_layout.setOnRefreshListener {
            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                shopId = mShopId
                recommend = 1
            }
            loadOnVisible()
        }
    }

    override fun bindData(data: MutableList<MallGood>, lastPage: Boolean) {
        swipe_layout.isRefreshing = false
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (mPage == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (mPage == Constant.DEFAULT_FIRST_PAGE) {
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

    override fun bindCoupon(response: MutableList<Coupon>) {

        if (response.size > 0) {
            couponHeader.visibility = View.VISIBLE
        } else {
            couponHeader.visibility = View.GONE
        }
        swipe_layout.isRefreshing = false
        mCouponHeaderAdapter.setNewInstance(response)
        content_rv.smoothScrollToPosition(0)
    }

    override fun bindRecommend(response: ArrayList<ShopRecommend>) {
        if (response.size > 0) {
            recommendHeader.visibility = View.VISIBLE
        } else {
            recommendHeader.visibility = View.GONE
        }
        swipe_layout.isRefreshing = false
        mRecommendHeaderAdapter.setNewInstance(response)
    }

    override fun getCouponResult(message: String) {
        ToastUtils.showShort(message)
        presenter.requestShopCoupon(mShopId)
    }


}