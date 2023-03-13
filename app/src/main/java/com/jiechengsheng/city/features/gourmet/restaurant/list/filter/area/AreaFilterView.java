package com.jiechengsheng.city.features.gourmet.restaurant.list.filter.area;

import com.jiechengsheng.city.api.network.BaseMvpView;
import com.jiechengsheng.city.api.response.area.AreaResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/30 11:33.
 */
public interface AreaFilterView extends BaseMvpView {

    void bindList(List<AreaResponse> response);
}
