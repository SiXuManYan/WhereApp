package com.jiechengsheng.city.features.mall.shop.home.good

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallGood
import com.jiechengsheng.city.api.response.mall.request.MallGoodListRequest
import com.jiechengsheng.city.api.response.mall.request.SortEnum
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.mall.home.child.MallRecommendAdapter
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
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

    /** 是否需要隐藏店铺名 */
    private var mHideListShopName = false

    private var goodRequest = MallGoodListRequest()
    private var mPage = Constant.DEFAULT_FIRST_PAGE

    /** 商品列表 */
    private lateinit var mAdapter: MallRecommendAdapter

    private lateinit var emptyView: EmptyView

    companion object {

        /**
         * @param shopId 店铺id
         * @param shopCategoryId 店铺分类Id (店铺详情跳转商品列表页)
         */
        fun newInstance(shopId: Int, shopCategoryId: Int? = null, hideListShopName: Boolean? = false): MallShopGoodFragment {
            val fragment = MallShopGoodFragment()

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shopId)

                shopCategoryId?.let {
                    putInt(Constant.PARAM_CATEGORY_ID, it)
                }
                hideListShopName?.let {
                    putBoolean(Constant.PARAM_TYPE, hideListShopName)
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
            mHideListShopName = it.getBoolean(Constant.PARAM_TYPE)
        }
        initContent()
    }

    private fun initContent() {
        emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)

        mAdapter = MallRecommendAdapter().apply {
            hideShopName = mHideListShopName
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