package com.jcs.where.features.gourmet.restaurant.list.filter.food;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.category.Category;

import java.util.ArrayList;
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
     * 首页金刚区传89，其余地方根据接口返回传值
     *
     * @param pid
     * @param pidName
     */
    public void getCategoriesList(int pid, String pidName) {

        requestApi(mRetrofit.getCategoriesList(3, pid, 1), new BaseMvpObserver<List<Category>>(view) {
            @Override
            protected void onSuccess(List<Category> response) {
                if (response == null) {
                    return;
                }
                Category category = new Category();
                category.id = 0;
                category.name = pidName ;
                category.has_children = 1;
                category.nativeIsSelected = true;
                ArrayList<Category> categories = new ArrayList<>(response);
                categories.add(0,category);
                view.bindList(categories);
            }
        });
    }
}
