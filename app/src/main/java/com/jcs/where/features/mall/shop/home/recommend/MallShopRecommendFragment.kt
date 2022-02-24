package com.jcs.where.features.mall.shop.home.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.mall.ShopRecommend
import com.jcs.where.api.response.mall.request.MallGoodListRequest
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.mall.home.child.MallRecommendAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
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
    private lateinit var mHeaderAdapter: ShopRecommendHeaderAdapter

    /** 商品列表 */
    private lateinit var mAdapter: MallRecommendAdapter

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
        initHeader()
    }

    private fun initHeader() {

        val header = LayoutInflater.from(requireContext()).inflate(R.layout.layout_mall_shop_recommend_header, null, false)
        mAdapter.addHeaderView(header)


        mHeaderAdapter = ShopRecommendHeaderAdapter()

        val header_rv = header.findViewById<RecyclerView>(R.id.header_rv)
        val manager  = object :LinearLayoutManager(context, VERTICAL,false) {
            override fun canScrollVertically() = false
        }
        header_rv.layoutManager = manager
        header_rv.adapter = mHeaderAdapter
    }


    private fun initContent() {
        val emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()

        mAdapter = MallRecommendAdapter().apply {
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
        presenter.getRecommend(mShopId)
        presenter.getMallList(goodRequest)
    }

    override fun bindListener() {

    }

    override fun bindData(data: MutableList<MallGood>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (mPage == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)

                loadMoreModule.loadMoreComplete()
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

    override fun bindRecommend(response: ArrayList<ShopRecommend>) {
        mHeaderAdapter.setNewInstance(response)
        content_rv.smoothScrollToPosition(0)
    }

}