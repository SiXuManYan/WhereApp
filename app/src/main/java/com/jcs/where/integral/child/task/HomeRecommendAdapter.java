package com.jcs.where.integral.child.task;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Wangsw  2021/3/2 11:39.
 * 推荐列表
 */
public class HomeRecommendAdapter extends BaseMultiItemQuickAdapter<HomeRecommendResponse, BaseViewHolder> {


    public HomeRecommendAdapter() {
        addItemType(HomeRecommendResponse.MODULE_TYPE_1_HOTEL, R.layout.item_home_recommend_hotel);
        addItemType(HomeRecommendResponse.MODULE_TYPE_2_SERVICE, R.layout.item_home_recommend_service);
        addItemType(HomeRecommendResponse.MODULE_TYPE_3_FOOD, R.layout.item_home_recommend_hotel);
        addItemType(HomeRecommendResponse.MODULE_TYPE_4_TRAVEL, R.layout.item_home_recommend_travel);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, HomeRecommendResponse homeRecommendResponse) {

    }
}
