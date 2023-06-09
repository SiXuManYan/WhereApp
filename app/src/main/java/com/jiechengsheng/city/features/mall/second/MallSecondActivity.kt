package com.jiechengsheng.city.features.mall.second

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallCategory
import com.jiechengsheng.city.api.response.mall.MallGood
import com.jiechengsheng.city.api.response.mall.request.MallGoodListRequest
import com.jiechengsheng.city.api.response.mall.request.SortEnum
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.base.mvp.FixedHeightBottomSheetDialog
import com.jiechengsheng.city.features.mall.cart.MallCartActivity
import com.jiechengsheng.city.features.mall.home.child.MallRecommendAdapter
import com.jiechengsheng.city.features.search.SearchAllActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_mall_second.*
import kotlinx.android.synthetic.main.layout_mall_filter.*

/**
 * Created by Wangsw  2021/12/3 15:30.
 * 新版商城二级列表
 */
class MallSecondActivity : BaseMvpActivity<MallSecondPresenter>(), MallSecondView {


    /** 当前页面对应的二级分类 */
    private var categoryId = 0
    private var goodRequest = MallGoodListRequest()
    private var page = Constant.DEFAULT_FIRST_PAGE

    /** 商品列表 */
    private lateinit var mAdapter: MallRecommendAdapter


    lateinit var mThirdCategoryAdapter: ThirdCategoryFilterAdapter

    companion object {

        fun navigation(context: Context, categoryId: Int) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_CATEGORY_ID, categoryId)
            }
            val intent = Intent(context, MallSecondActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var emptyView: EmptyView

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_second

    override fun initView() {
        categoryId = intent.getIntExtra(Constant.PARAM_CATEGORY_ID, 0)
        initCategoryFilter()
        initContent()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCategoryFilter() {

        mThirdCategoryAdapter = ThirdCategoryFilterAdapter().apply {
            setOnItemClickListener { _, _, position ->
                // 筛选
                val datas = mThirdCategoryAdapter.data
                datas.forEach {
                    it.nativeIsSelected = false
                }
                notifyDataSetChanged()

                val category = data[position]
                category.nativeIsSelected = true
                notifyItemChanged(position)
                goodRequest.apply {
                    page = Constant.DEFAULT_FIRST_PAGE
                    categoryId = category.id
                    presenter.getMallList(goodRequest)
                }
            }
        }
        category_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mThirdCategoryAdapter
        }
    }

    private fun initContent() {

        emptyView = EmptyView(this)
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)

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
        content_rv.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }
        swipe_layout.setOnRefreshListener {
            goodRequest.page = Constant.DEFAULT_FIRST_PAGE
            presenter.getMallList(goodRequest)
        }
    }

    override fun initData() {
        presenter = MallSecondPresenter(this)
        presenter.getThirdCategory(categoryId)
    }

    override fun bindListener() {
        cart_iv.setOnClickListener {
            startActivityAfterLogin(MallCartActivity::class.java)
        }

        dismiss_view.setOnClickListener {
            filter_container_ll.visibility = View.GONE
            if (sales_tv.isChecked) {
                complex_tv.isChecked = false
            }
        }

        complex_ll.setOnClickListener {
            if (filter_container_ll.visibility == View.VISIBLE) {
                filter_container_ll.visibility = View.GONE
                complex_tv.isChecked = false
                complex_iv.rotation = 0f
            } else {
                filter_container_ll.visibility = View.VISIBLE
                complex_tv.isChecked = true
                complex_iv.rotation = 180f
            }
            if (filter_complex_ctv.isChecked || filter_price_down_ctv.isChecked || filter_price_up_ctv.isChecked) {
                complex_tv.isChecked = true
            }

        }

        // 综合
        filter_complex_ctv.setOnClickListener {
            sales_tv.isChecked = false

            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                order = null
                sold = null
            }
            presenter.getMallList(goodRequest)

            complex_tv.text = filter_complex_ctv.text
            filter_container_ll.visibility = View.GONE

            filter_complex_ctv.isChecked = true
            filter_price_down_ctv.isChecked = false
            filter_price_up_ctv.isChecked = false
            complex_tv.isChecked = true

        }

        // 价格降序
        filter_price_down_ctv.setOnClickListener {
            sales_tv.isChecked = false
            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                order = SortEnum.desc
                sold = null
            }
            presenter.getMallList(goodRequest)

            complex_tv.text = filter_price_down_ctv.text
            filter_container_ll.visibility = View.GONE
            filter_complex_ctv.isChecked = false
            filter_price_down_ctv.isChecked = true
            filter_price_up_ctv.isChecked = false
            complex_tv.isChecked = true
        }

        // 价格升序
        filter_price_up_ctv.setOnClickListener {
            sales_tv.isChecked = false
            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                order = SortEnum.asc
                sold = null
            }
            presenter.getMallList(goodRequest)

            complex_tv.text = filter_price_up_ctv.text
            filter_container_ll.visibility = View.GONE
            filter_complex_ctv.isChecked = false
            filter_price_down_ctv.isChecked = false
            filter_price_up_ctv.isChecked = true
            complex_tv.isChecked = true
        }

        // 销量
        sales_tv.setOnClickListener {
            complex_tv.isChecked = false
            filter_container_ll.visibility = View.GONE
            sales_tv.isChecked = !sales_tv.isChecked
            goodRequest.apply {
                page = Constant.DEFAULT_FIRST_PAGE
                order = null
                sold = if (sales_tv.isChecked) {
                    SortEnum.desc
                } else {
                    null
                }
            }
            presenter.getMallList(goodRequest)
        }

        other_filter_ll.setOnClickListener {
            filter_container_ll.visibility = View.GONE
            showOtherFilterDialog()
        }

        search_ll.setOnClickListener {
            startActivity(SearchAllActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 6)
                // 通过2三级分类跳转搜索，要同时穿2级分类
                putString(Constant.PARAM_CATEGORY_ID, categoryId.toString())
            })
        }
    }

    override fun bindThirdCategory(response: ArrayList<MallCategory>) {
        mThirdCategoryAdapter.setNewInstance(response)
        if (response.isNotEmpty()) {
            goodRequest.apply {
                categoryId = response[0].id
            }
            presenter.getMallList(goodRequest)
        }

    }

    override fun bindData(data: MutableList<MallGood>, lastPage: Boolean) {
        swipe_layout.isRefreshing = false
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


    private lateinit var filterDialog: FixedHeightBottomSheetDialog

    private fun showOtherFilterDialog() {

        if (!::filterDialog.isInitialized) {
            filterDialog = FixedHeightBottomSheetDialog(this, R.style.bottom_sheet_edit, ScreenUtils.getScreenHeight() * 70 / 100)
            val view = LayoutInflater.from(this).inflate(R.layout.dialog_mall_other_filter, null)
            filterDialog.setContentView(view)

            try {
                val parent = view.parent as ViewGroup
                parent.setBackgroundResource(android.R.color.transparent)
            } catch (e: Exception) {
            }

            val min_et = view.findViewById<AppCompatEditText>(R.id.min_et)
            val max_et = view.findViewById<AppCompatEditText>(R.id.max_et)

            view.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
                filterDialog.dismiss()
            }

            view.findViewById<TextView>(R.id.confirm_tv).setOnClickListener {
                if (!min_et.text.isNullOrBlank() && !max_et.text.isNullOrBlank()) {
                    goodRequest.apply {
                        page = Constant.DEFAULT_FIRST_PAGE
                        startPrice = min_et.text.toString()
                        endPrice = max_et.text.toString()
                    }
                    presenter.getMallList(goodRequest)
                }
                filterDialog.dismiss()
            }

            view.findViewById<TextView>(R.id.reset_tv).setOnClickListener {
                goodRequest.apply {
                    page = Constant.DEFAULT_FIRST_PAGE
                    startPrice = null
                    endPrice = null
                }
                presenter.getMallList(goodRequest)
                filterDialog.dismiss()
            }
        }
        filterDialog.show()
    }


}