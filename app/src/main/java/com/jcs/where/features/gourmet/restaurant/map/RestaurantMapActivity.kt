package com.jcs.where.features.gourmet.restaurant.map

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2021/5/14 15:18.
 *
 */
class RestaurantMapActivity : BaseMvpActivity<RestaurantMapPresenter>(), RestaurantMapView {

    private var mRestaurantId: String = ""

    override fun getLayoutId() = R.layout.activity_restaurant_map

    override fun initView() {
        intent.getStringExtra(Constant.PARAM_ID)?.let {
            mRestaurantId = it
        }
    }

    override fun initData() {
        presenter = RestaurantMapPresenter(this)
//        presenter.getData(mRestaurantId)

    }

    override fun bindListener() {

    }

}