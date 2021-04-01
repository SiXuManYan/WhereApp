package com.jcs.where.features.gourmet.restaurant.detail;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;


/**
 * Created by Wangsw  2021/4/1 10:28.
 * 餐厅详情
 */
public class RestaurantDetailActivity extends BaseMvpActivity<RestaurantDetailPresenter> implements RestaurantDetailView {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_restaurant_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        presenter = new RestaurantDetailPresenter(this);
    }

    @Override
    protected void bindListener() {

    }


}
