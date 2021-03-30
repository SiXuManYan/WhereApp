package com.jcs.where.features.gourmet.restaurant.list.filter.food;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.category.Category;

import java.util.List;

/**
 * Created by Wangsw  2021/3/30 11:35.
 */
public class FoodCategoryFilterPresenter extends BaseMvpPresenter {

    private FoodCategoryFilterView view;

    public FoodCategoryFilterPresenter(FoodCategoryFilterView view) {
        super(view);
        this.view = view;
    }

    /**
     * 获取分类列表 !--level 3 pid 89 type =1 --
     */
    public void getCategoriesList() {

        requestApi(mRetrofit.getCategoriesList(3, 89, 1), new BaseMvpObserver<List<Category>>(view) {
            @Override
            protected void onSuccess(List<Category> response) {
                if (response != null && !response.isEmpty()) {
                    view.bindList(response);
                }
            }
        });
    }
}
