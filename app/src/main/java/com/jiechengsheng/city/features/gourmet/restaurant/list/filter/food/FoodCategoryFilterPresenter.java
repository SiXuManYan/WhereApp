package com.jiechengsheng.city.features.gourmet.restaurant.list.filter.food;

import com.jiechengsheng.city.api.network.BaseMvpObserver;
import com.jiechengsheng.city.api.network.BaseMvpPresenter;
import com.jiechengsheng.city.api.response.category.Category;

import java.util.ArrayList;

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

        requestApi(mRetrofit.getCategoriesList(3, String.valueOf(pid), 1), new BaseMvpObserver<ArrayList<Category>>(view) {
            @Override
            protected void onSuccess(ArrayList<Category> response) {
                // 添加默认的美食类型
                Category category = new Category();
                category.id = pid;
                category.name = pidName;
                category.has_children = 1;
                category.nativeIsSelected = true;
                ArrayList<Category> categories = new ArrayList<>(response);
                categories.add(0, category);
                view.bindList(categories);
            }
        });
    }
}
