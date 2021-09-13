package com.jcs.where.features.hotel.detail

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2021/9/13 14:47.
 * 酒店详情
 */
class HotelDetailActivity :BaseMvpActivity<HotelDetailPresenter>(),HotelDetailView{


    override fun getLayoutId() = R.layout.activity_hotel_detail_new

    override fun initView() {

    }

    override fun initData() {
        presenter = HotelDetailPresenter(this)
    }

    override fun bindListener() {

    }


}