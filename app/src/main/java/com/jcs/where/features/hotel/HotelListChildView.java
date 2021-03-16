package com.jcs.where.features.hotel;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.hotel.HotelListResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/16 14:51.
 */
public interface HotelListChildView extends BaseMvpView {

    void bindList(List<HotelListResponse> data, boolean isLastPage);

}
