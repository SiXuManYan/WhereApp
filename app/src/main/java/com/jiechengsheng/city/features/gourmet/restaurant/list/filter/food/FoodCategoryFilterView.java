package com.jiechengsheng.city.features.gourmet.restaurant.list.filter.food;

import com.jiechengsheng.city.api.network.BaseMvpView;
import com.jiechengsheng.city.api.response.category.Category;

import java.util.List;

/**
 * Created by Wangsw  2021/3/30 11:35.
 */
public interface FoodCategoryFilterView extends BaseMvpView {
    void bindList(List<Category> response);
}
