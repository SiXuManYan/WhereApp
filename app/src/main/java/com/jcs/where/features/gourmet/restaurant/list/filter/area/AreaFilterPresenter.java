package com.jcs.where.features.gourmet.restaurant.list.filter.area;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.area.AreaResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/30 11:34.
 */
public class AreaFilterPresenter extends BaseMvpPresenter {

    private AreaFilterView view;

    public AreaFilterPresenter(AreaFilterView view) {
        super(view);
        this.view = view;
    }

    public void getAreasList() {
        requestApi(mRetrofit.getAreasList(), new BaseMvpObserver<List<AreaResponse>>(view) {
            @Override
            protected void onSuccess(List<AreaResponse> response) {
                if (response != null && !response.isEmpty()) {
                    view.bindList(response);
                }
            }
        });

    }
}
