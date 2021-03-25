package com.jcs.where.features.gourmet.restaurant.list;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse;
import com.jcs.where.bean.RestaurantListRequest;

/**
 * Created by Wangsw  2021/3/24 13:57.
 */
public class RestaurantListPresenter extends BaseMvpPresenter {

    private RestaurantListView view;

    public RestaurantListPresenter(RestaurantListView view) {
        super(view);
        this.view = view;
    }


    public void getList(int page, RestaurantListRequest request) {


        requestApi(mRetrofit.getRestaurantList(
                request.trading_area_id,
                request.per_price,
                request.service,
                request.sort,
                request.search_input,
                request.lat,
                request.lng,
                request.category_id), new BaseMvpObserver<PageResponse<RestaurantResponse>>(view) {
            @Override
            protected void onSuccess(PageResponse<RestaurantResponse> response) {
                boolean isLastPage = response.getLastPage() == page;
                view.bindList(response.getData(), isLastPage);
            }
        });
    }
}
