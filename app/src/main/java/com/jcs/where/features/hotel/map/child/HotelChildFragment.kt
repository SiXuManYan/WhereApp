package com.jcs.where.features.hotel.map.child

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.hotel.home.HotelHomeRecommendAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * Created by Wangsw  2021/9/27 15:48.
 * 酒店列表
 */
class HotelChildFragment : BaseMvpFragment<HotelChildPresenter>(), HotelChildView {

    private lateinit var mAdapter: HotelHomeRecommendAdapter

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.single_recycler_view


    override fun initView(view: View) {

        initList()
    }

    private fun initList() {
        emptyView = EmptyView(context).apply {
            initEmpty(
                R.mipmap.ic_empty_search, R.string.empty_search,
                R.string.empty_search_hint, R.string.back
            ) {

            }
            action_tv.visibility = View.GONE
        }


        mAdapter = HotelHomeRecommendAdapter().apply {
            setEmptyView(emptyView)
        }

        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f), SizeUtils.dp2px(15f), SizeUtils.dp2px(15f)))
            layoutManager = object : LinearLayoutManager(activity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }


    }

    override fun initData() {
        presenter = HotelChildPresenter(this)
    }

    override fun bindListener() {

    }


}