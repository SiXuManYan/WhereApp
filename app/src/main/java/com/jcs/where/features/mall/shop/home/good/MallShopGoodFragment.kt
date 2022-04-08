package com.jcs.where.features.mall.shop.home.good

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.mall.request.MallGoodListRequest
import com.jcs.where.api.response.mall.request.SortEnum
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.mall.home.child.MallRecommendAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_mall_shop_good.*

/**
 * Created by Wangsw  2022/1/24 10:22.
 * 店铺商品
 * 销量、最新、价格 三种排序互斥
 */
class MallShopGoodFragment : BaseMvpFragment<MallShopGoodPresenter>(), MallShopGoodView {


    /** 店铺分类ID */
    private var mShopCategoryId = 0
    private var mShopId = 0

    private var goodRequest = MallGoodListRequest()
    private var mPage = Constant.DEFAULT_FIRST_PAGE

    /** 商品列表 */
    private lateinit var mAdapter: MallRecommendAdapter


    companion object {

        /**
         * @param shopId 店铺id
         * @param shopCategoryId 店铺分类Id (店铺详情跳转商品列表页)
         */
        fun newInstance(shopId: Int, shopCategoryId: Int? = null): MallShopGoodFragment {
            val fragment = MallShopGoodFragment()

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shopId)
                shopCategoryId?.let {
                    putInt(Constant.PARAM_CATEGORY_ID, it)
                }
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_mall_shop_good

    override fun initView(view: View?) {
        arguments?.let {
            mShopId = it.getInt(Constant.PARAM_SHOP_ID)
            mShopCategoryId = it.getInt(Constant.PARAM_CATEGORY_ID)
        }
        initContent()
    }

    private fun initContent() {
        val emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()

        mAdapter = MallRecommendAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener {
                mPage++
                goodRequest.page = mPage
                presenter.getMallList(goodRequest)
            }
        }
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0)
        content_rv.apply {
            adapter = mAdapter
            layoutManager = manager
            addItemDecoration(decoration)
        }

    }

    override fun initData() {
        presenter = MallShopGoodPresenter(this)
        goodRequest.apply {
            page = Constant.DEFAULT_FIRST_PAGE
            shopId = mShopId
            if (mShopCategoryId != 0) {
                shop_categoryId = mShopCategoryId
            }
        }
        presenter.getMallList(goodRequest)

    }


    override fun bindData(data: MutableList<MallGood>, lastPage: Boolean) {
        swipe_layout.isRefreshing = false
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

    override fun bindListener() {
        swipe_layout.setOnRefreshListener {
            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                shopId = mShopId
            }
            presenter.getMallList(goodRequest)
        }

        sales_tv.setOnClickListener {
            price_tv.setTextColor(ColorUtils.getColor(R.color.selector_gray666_blue))
            sales_tv.toggle()
            newest_tv.isChecked = false
            price_tv.isChecked = false
            price_up_iv.setImageResource(R.mipmap.ic_up_store)
            price_down_iv.setImageResource(R.mipmap.ic_down_store)

            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                //
                sold = if (sales_tv.isChecked) {
                    SortEnum.desc
                } else {
                    null
                }
                order = null
            }
            presenter.getMallList(goodRequest)
        }

        newest_tv.setOnClickListener {
            price_tv.setTextColor(ColorUtils.getColor(R.color.selector_gray666_blue))
            sales_tv.isChecked = false
            newest_tv.toggle()
            price_tv.isChecked = false
            price_up_iv.setImageResource(R.mipmap.ic_up_store)
            price_down_iv.setImageResource(R.mipmap.ic_down_store)
            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                sold = null
                order = null
            }
            presenter.getMallList(goodRequest)
        }

        price_rl.setOnClickListener {
            sales_tv.isChecked = false
            newest_tv.isChecked = false
            price_tv.toggle()
            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                sold = null
                if (price_tv.isChecked) {
                    order = SortEnum.desc
                    price_up_iv.setImageResource(R.mipmap.ic_up_store)
                    price_down_iv.setImageResource(R.mipmap.ic_down_store_pre)
                    price_tv.setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
                } else {
                    order = SortEnum.asc
                    price_up_iv.setImageResource(R.mipmap.ic_up_store_pre)
                    price_down_iv.setImageResource(R.mipmap.ic_down_store)
                    price_tv.setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
                }
            }
            presenter.getMallList(goodRequest)
        }
    }
}