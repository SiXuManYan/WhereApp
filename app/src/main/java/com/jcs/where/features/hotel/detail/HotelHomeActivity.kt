package com.jcs.where.features.hotel.detail

import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelListResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import java.util.*

/**
 * Created by Wangsw  2021/9/13 14:47.
 * 酒店详情
 */
class HotelHomeActivity : BaseMvpActivity<HotelDetailPresenter>(), HotelHomeView {

    private lateinit var mAdapter: HotelHomeRecommendAdapter

    override fun getLayoutId() = R.layout.activity_hotel_detail_new

    override fun initView() {

    }

    override fun initData() {
        presenter = HotelDetailPresenter(this)
    }

    override fun bindListener() {

    }

    override fun bindData(response: ArrayList<HotelListResponse>) {

    }


}