package com.jcs.where.features.hotel;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.hotel.HotelListResponse;
import com.jcs.where.utils.Constant;

import java.util.List;

/**
 * Created by Wangsw  2021/3/16 14:52.
 */
public class HotelListChildPresenter extends BaseMvpPresenter {

    private HotelListChildView view;

    public HotelListChildPresenter(HotelListChildView view) {
        super(view);
        this.view = view;
    }

    /**
     * 酒店列表
     *
     * @param page
     * @param areaId       地区id
     * @param price_range  价格区间（特殊情况，900以上请传递[900,100000]）
     * @param star_level   星级（特殊情况，二星以下请传递[1,2]）
     * @param hotelTypeIds 住宿类型ID（多选）
     * @param search_input 搜索酒店内容
     * @param grade        酒店评分
     */
    public void getList(int page,
                        String areaId,
                        String price_range,
                        String star_level,
                        String hotelTypeIds,
                        String search_input,
                        String grade) {


        String lat = String.valueOf(Constant.LAT);
        String lng = String.valueOf(Constant.LAT);

        requestApi(mRetrofit.getHotelList(page, lat, lng, areaId, price_range, star_level, hotelTypeIds, search_input, grade), new BaseMvpObserver<PageResponse<HotelListResponse>>(view) {



            @Override
            protected void onSuccess(PageResponse<HotelListResponse> response) {
                boolean isLastPage = response.getLastPage() == page;
                List<HotelListResponse> data = response.getData();
                view.bindList(data, isLastPage);
            }
        });


    }
}
