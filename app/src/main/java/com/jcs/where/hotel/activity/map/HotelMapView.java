package com.jcs.where.hotel.activity.map;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.bean.HotelMapListBean;

import java.util.List;

/**
 * Created by Wangsw  2021/3/9 16:23.
 */
public interface HotelMapView extends BaseMvpView {
    void getMapDataSuccess(List<HotelMapListBean> data);
}
