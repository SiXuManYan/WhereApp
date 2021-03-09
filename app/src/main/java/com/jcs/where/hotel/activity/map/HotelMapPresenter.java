package com.jcs.where.hotel.activity.map;

import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.bean.HotelMapListBean;
import com.jcs.where.utils.Constant;

/**
 * Created by Wangsw  2021/3/9 16:24.
 */
public class HotelMapPresenter extends BaseMvpPresenter {

    private HotelMapView view;

    public HotelMapPresenter(HotelMapView view) {
        super(view);
        this.view = view;
    }


    public void getMapData(String extraPrice, String extraStar, String extraCityId, String useInputText) {

        requestApi(mRetrofit.getHotelMapList(1,String.valueOf(Constant.LAT), String.valueOf(Constant.LNG),
                extraCityId, useInputText, extraStar, extraPrice), new BaseMvpObserver<PageResponse<HotelMapListBean>>(view) {
                    @Override
                    protected void onSuccess(PageResponse<HotelMapListBean> response) {

                        view.getMapDataSuccess(response.getData());
                    }

                    @Override
                    protected void onError(ErrorResponse errorResponse) {
                        super.onError(errorResponse);
                    }
                }
        );


    }



}
