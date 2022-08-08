package com.jcs.where.features.mall.result

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.cart.MallCartActivity
import com.jcs.where.features.mall.home.child.MallRecommendAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_mall_search_result.*

/**
 * Created by Wangsw  2021/12/10 10:16.
 * 商城搜索结果
 * 商城首页、商城二级分类页、商城店铺页
 */
class MallSearchResultActivity : BaseMvpActivity<MallSearchPresenter>(), MallSearchView {

    private var searchInput: String? = null

    /** 商城店铺id(通过店铺详情进行搜索时) */
    private var shopId: Int? = null

    /** 商城二级页跳转搜索传递的分类id  */
    private var categoryId: String? = null

    /** 商品推荐 */
    private lateinit var mAdapter: MallRecommendAdapter

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.activity_mall_search_result

    override fun isStatusDark() = true

    override fun initView() {
        intent.extras?.let {
            searchInput = it.getString(Constant.PARAM_NAME)
            categoryId = it.getString(Constant.PARAM_CATEGORY_ID)
            shopId = it.getInt(Constant.PARAM_SHOP_ID)
        }
        search_tv.text = searchInput
        initContent()
    }

    private fun initContent() {

        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_search)
            setEmptyMessage(R.string.empty_search_message)
            setEmptyHint(R.string.empty_search_hint)
            addEmptyList(this)
        }

        mAdapter = MallRecommendAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getMallList(page, searchInput, categoryId, shopId)
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
        presenter = MallSearchPresenter(this)
        presenter.getMallList(page, searchInput, categoryId, shopId)
    }

    override fun bindListener() {
        cart_iv.setOnClickListener {
            startActivityAfterLogin(MallCartActivity::class.java)
        }
        search_rl.setOnClickListener {
            finish()
        }
    }

    override fun bindData(data: MutableList<MallGood>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
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


interface MallSearchView : BaseMvpView {
    fun bindData(data: MutableList<MallGood>, lastPage: Boolean)

}

class MallSearchPresenter(private var view: MallSearchView) : BaseMvpPresenter(view) {

    fun getMallList(page: Int, title: String? = null, categoryId: String? = null, shopId: Int? = null) {

        val secondCategoryId = if (categoryId.isNullOrBlank()) {
            null
        } else {
            categoryId.toInt()
        }

        requestApi(mRetrofit.getMallGoodList(
            page,
            null,
            title,
            secondCategoryId,
            null,
            null,
            null,
            shopId,
            null,
            null,
            null
        ), object : BaseMvpObserver<PageResponse<MallGood>>(view) {
            override fun onSuccess(response: PageResponse<MallGood>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)

            }
        })
    }
}
