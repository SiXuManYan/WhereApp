package com.jcs.where.features.search.result

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.search.SearchResultResponse
import com.jcs.where.base.IntentEntry
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.government.activity.MechanismDetailActivity
import com.jcs.where.home.HomeActivity
import com.jcs.where.hotel.activity.HotelDetailActivity
import com.jcs.where.travel.TouristAttractionDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarDialog
import kotlinx.android.synthetic.main.activity_search_all_result.*

/**
 * Created by Wangsw  2021/8/23 15:15.
 * 搜索结果页
 */
class SearchAllResultActivity : BaseMvpActivity<SearchAllResultPresenter>(), SearchAllResultView, OnItemClickListener {

    var name = ""

    private lateinit var mAdapter: SearchAllAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.activity_search_all_result

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        name = bundle.getString(Constant.PARAM_NAME, "")

        search_aet.text = name

        initList()
    }

    private fun initList() {
        emptyView = EmptyView(this).apply {
            initEmpty(
                R.mipmap.ic_empty_search, R.string.empty_search,
                R.string.empty_search_hint, R.string.back_home
            ) {
                startActivityClearTop(HomeActivity::class.java, null)
            }

        }

        mAdapter = SearchAllAdapter().apply {
            setOnItemClickListener(this@SearchAllResultActivity)
            setEmptyView(emptyView)
            keyWord = name
        }


        recycler.adapter = mAdapter
    }

    override fun initData() {
        presenter = SearchAllResultPresenter(this)
        presenter.search(name)
    }

    override fun bindListener() {


        back_iv.setOnClickListener {
            finish()
        }
        delete_iv.setOnClickListener {
            finish()
        }
    }

    override fun bindSearchResult(response: MutableList<SearchResultResponse>) {
        if (response.isEmpty()) {
            mAdapter.setNewInstance(null)
            emptyView.showEmptyContainer()
            return
        }
        mAdapter.setNewInstance(response)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        when (data.type) {
            SearchResultResponse.TYPE_1_HOTEL -> {
                val dialog = JcsCalendarDialog()
                dialog.initCalendar(this)
                HotelDetailActivity.goTo(this, data.id, dialog.startBean, dialog.endBean, 1, "", "", 1)
            }
            SearchResultResponse.TYPE_2_TRAVEL -> TouristAttractionDetailActivity.goTo(this, data.id)
            SearchResultResponse.TYPE_3_SERVICE -> toActivity(
                MechanismDetailActivity::class.java,
                IntentEntry(MechanismDetailActivity.K_MECHANISM_ID, data.id.toString())
            )
            SearchResultResponse.TYPE_4_RESTAURANT -> {
                val bundle = Bundle()
                bundle.putString(Constant.PARAM_ID, data.id.toString())
                startActivity(RestaurantDetailActivity::class.java, bundle)
            }
            else -> {
            }
        }


    }

}