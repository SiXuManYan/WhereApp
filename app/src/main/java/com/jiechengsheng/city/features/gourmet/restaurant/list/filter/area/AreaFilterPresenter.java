package com.jiechengsheng.city.features.gourmet.restaurant.list.filter.area;

import com.blankj.utilcode.util.StringUtils;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.network.BaseMvpObserver;
import com.jiechengsheng.city.api.network.BaseMvpPresenter;
import com.jiechengsheng.city.api.response.area.AreaResponse;

import java.util.ArrayList;
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
                // 添加默认的区域
                AreaResponse area = new AreaResponse();
                area.id = null;
                area.name = StringUtils.getString(R.string.filter_city);
                area.nativeIsSelected = true;
                ArrayList<AreaResponse> list = new ArrayList<>(response);
                list.add(0, area);
                view.bindList(list);
            }
        });

    }
}
