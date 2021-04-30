package com.jcs.where.features.gourmet.restaurant.detail;

import com.google.gson.JsonElement;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.request.CollectionRestaurantRequest;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.gourmet.comment.CommentResponse;
import com.jcs.where.api.response.gourmet.dish.DishResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse;
import com.jcs.where.utils.Constant;

import java.util.List;

import retrofit2.http.PUT;

/**
 * Created by Wangsw  2021/4/1 10:28.
 */
public class RestaurantDetailPresenter extends BaseMvpPresenter {

    private RestaurantDetailView view;

    public RestaurantDetailPresenter(RestaurantDetailView view) {
        super(view);
        this.view = view;
    }

    public void getDetail(String restaurantId) {
        requestApi(mRetrofit.getRestaurantDetail(restaurantId, Constant.LAT + "", Constant.LNG + ""), new BaseMvpObserver<RestaurantDetailResponse>(view) {
            @Override
            protected void onSuccess(RestaurantDetailResponse response) {
                if (response == null) {
                    return;
                }
                view.bindData(response);
            }
        });


    }

    /**
     * 堂食菜品列表
     */
    public void getDishList(String restaurantId) {


        requestApi(mRetrofit.getDishList(1, restaurantId), new BaseMvpObserver<PageResponse<DishResponse>>(view) {
            @Override
            protected void onSuccess(PageResponse<DishResponse> response) {
                List<DishResponse> data = response.getData();
                if (data!=null && !data.isEmpty()) {
                    view.bindDishData(data);
                }

            }
        });

    }

    /**
     * 菜品列表
     */
    public void getCommentList(String restaurantId) {
        requestApi(mRetrofit.getCommentList(1,0,restaurantId), new BaseMvpObserver<PageResponse<CommentResponse>>(view) {
            @Override
            protected void onSuccess(PageResponse<CommentResponse> response) {
                List<CommentResponse> data = response.getData();
                if (data!=null && !data.isEmpty()) {
                    view.bindCommentData(data);
                }
            }
        });
    }

    public void collection(String restaurantId){

        CollectionRestaurantRequest request = new CollectionRestaurantRequest();
        request.restaurant_id = restaurantId;

        requestApi(mRetrofit.collectsRestaurant(request), new BaseMvpObserver<JsonElement>(view) {
            @Override
            protected void onSuccess(JsonElement response) {
                view.collectionSuccess();
            }
        });

    }


    public void unCllection(String restaurantId){

        CollectionRestaurantRequest request = new CollectionRestaurantRequest();
        request.restaurant_id = restaurantId;

        requestApi(mRetrofit.unCollectsRestaurant(request), new BaseMvpObserver<JsonElement>(view) {
            @Override
            protected void onSuccess(JsonElement response) {
                view.unCollectionSuccess();
            }
        });

    }

}
