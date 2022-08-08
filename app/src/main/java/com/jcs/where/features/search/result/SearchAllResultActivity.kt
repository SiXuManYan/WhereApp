package com.jcs.where.features.search.result

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.search.SearchResultResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.main.MainActivity
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
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
            setEmptyImage(R.mipmap.ic_empty_search)
            setEmptyMessage(R.string.empty_search_message)
            setEmptyHint(R.string.empty_search_hint)
            setEmptyActionText(R.string.back_home)
            setEmptyActionOnClickListener {
                startActivityClearTop(MainActivity::class.java, null)
            }
            addEmptyList(this)

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
        findViewById<RelativeLayout>(R.id.title_rl).setOnClickListener {
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
//                HotelDetailActivity.goTo(this, data.id, dialog.startBean, dialog.endBean, 1, "", "", 1)
                HotelDetailActivity2.navigation(this, data.id, dialog.startBean, dialog.endBean)
            }
            SearchResultResponse.TYPE_2_TRAVEL -> {
                TravelDetailActivity.navigation(this@SearchAllResultActivity, data.id)
            }
            SearchResultResponse.TYPE_3_SERVICE -> {
                startActivity(MechanismActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ID, data.id)
                })
            }
            SearchResultResponse.TYPE_4_RESTAURANT -> {
                val bundle = Bundle()
                bundle.putInt(Constant.PARAM_ID, data.id)
                startActivity(RestaurantDetailActivity::class.java, bundle)
            }
            else -> {
            }
        }


    }

}