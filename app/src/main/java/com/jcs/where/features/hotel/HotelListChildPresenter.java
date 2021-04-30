package com.jcs.where.features.hotel;

import com.google.gson.Gson;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.hotel.HotelListResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/16 14:52.
 */
public class HotelListChildPresenter extends BaseMvpPresenter {

    private HotelListChildView view;

    private Gson gson;

    public HotelListChildPresenter(HotelListChildView view) {
        super(view);
        this.view = view;
        gson = new Gson();
    }

    /**
     * 酒店列表
     *
     * @param page
     * @param areaId       地区id
     * @param mPriceStart  价格区间（特殊情况，900以上请传递[900,100000]）
     * @param star_level   星级（特殊情况，二星以下请传递[1,2]）
     * @param hotelTypeIds 住宿类型ID（多选）
     * @param search_input 搜索酒店内容
     * @param score        酒店评分
     */
    public void getList(int page,
                        String areaId,
                        int mPriceStart,
                        int mPriceEnd,
                        int star_level,
                        String hotelTypeIds,
                        String search_input,
                        float score) {

        String lat = "0";
        String lng = "0";

        // 价格区间
        String priceRange = "";
        if (mPriceEnd > 0) {
            int[] price = {mPriceStart, mPriceEnd};
            priceRange = gson.toJson(price);
        }

        // 星级（特殊情况，二星以下请传递[1,2]）
        String starStr = "";
        if (star_level != 0) {
            int[] star;
            if (star_level <= 2) {
                star = new int[]{1, 2};
            } else {
                star = new int[]{star_level};
            }
            starStr = gson.toJson(star);
        }

        requestApi(mRetrofit.getHotelList(page, lat, lng, areaId, priceRange, starStr, hotelTypeIds, search_input, String.valueOf(score)), new BaseMvpObserver<PageResponse<HotelListResponse>>(view) {

            @Override
            protected void onSuccess(PageResponse<HotelListResponse> response) {
                boolean isLastPage = response.getLastPage() == page;
                List<HotelListResponse> data = response.getData();
                view.bindList(data, isLastPage);
            }
        });


    }
}
