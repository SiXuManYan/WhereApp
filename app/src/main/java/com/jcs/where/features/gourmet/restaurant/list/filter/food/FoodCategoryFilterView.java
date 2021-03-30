package com.jcs.where.features.gourmet.restaurant.list.filter.food;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.category.Category;

import java.util.List;

/**
 * Created by Wangsw  2021/3/30 11:35.
 */
public interface FoodCategoryFilterView extends BaseMvpView {
    void bindList(List<Category> response);
}
