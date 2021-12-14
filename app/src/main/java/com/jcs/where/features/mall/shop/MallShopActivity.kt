package com.jcs.where.features.mall.shop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.home.child.MallRecommendAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_mall_shop.*


/**
 * Created by Wangsw  2021/12/14 10:36.
 * 商城店铺
 */
class MallShopActivity : BaseMvpActivity<MallShopPresenter>(), MallShopView {

    private var shopId = 0

    private var isToolbarDark = false

    private var page = Constant.DEFAULT_FIRST_PAGE

    /** 商品推荐 */
    private lateinit var mAdapter: MallRecommendAdapter

    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_mall_shop

    companion object {

        fun navigation(context: Context, shopId: Int, shopName: String? = "") {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, shopId)
                putString(Constant.PARAM_NAME, shopName)
            }
            val intent = Intent(context, MallShopActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        shopId = intent.getIntExtra(Constant.PARAM_SHOP_ID, 0)
        shop_name_tv.text = intent.getStringExtra(Constant.PARAM_SHOP_NAME)

        initContent()
        initScroll()
    }

    override fun initData() {
        presenter = MallShopPresenter(this)
        presenter.getMallList(page, shopId)
    }

    private fun initContent() {
        val emptyView = EmptyView(this)
        emptyView.showEmptyDefault()

        mAdapter = MallRecommendAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getMallList(page, shopId)
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

    private fun initScroll() {
        content_rv.isNestedScrollingEnabled = true
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

            back_iv.setImageResource(
                if (isToolbarDark) {
                    R.mipmap.ic_back_black
                } else {
                    R.mipmap.ic_back_light
                }
            )

            useView.background.alpha = alpha
            toolbar.background.alpha = alpha

            changeStatusTextColor()
        }

    }


    override fun bindListener() = Unit

    override fun bindData(data: MutableList<MallGood>, lastPage: Boolean) {
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