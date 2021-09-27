package com.jcs.where.features.hotel.map

import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.mvp.BaseMvpActivity
import java.util.*

/**
 * Created by Wangsw  2021/9/27 14:06.
 *  酒店地图
 */
class HotelMapActivity : BaseMvpActivity<HotelMapPresenter>(), HotelMapView {

    private var categoryId = 0

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_map_hotel

    override fun initView() {

    }

    override fun initData() {
        presenter = HotelMapPresenter(this)
        presenter.getHotelChildCategory(categoryId)
    }

    override fun bindListener() {

    }

    override fun bindCategory(response: ArrayList<Category>) {

    }
}