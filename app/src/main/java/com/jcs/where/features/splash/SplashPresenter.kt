package com.jcs.where.features.splash;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.SPKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/3/18 16:14.
 */
public class SplashPresenter extends BaseMvpPresenter {

    private SplashView view;

    public SplashPresenter(SplashView view) {
        super(view);
        this.view = view;
    }

    public void getYellowPageAllCategories() {

        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(10);
        categoryIds.add(17);
        categoryIds.add(21);
        categoryIds.add(27);
        categoryIds.add(94);
        categoryIds.add(114);
        categoryIds.add(209);
        categoryIds.add(226);

        requestApi(mRetrofit.getAllChildCategories(1, categoryIds.toString()), new BaseMvpObserver<List<CategoryResponse>>(view) {
            @Override
            protected void onSuccess(List<CategoryResponse> response) {
                if (CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_YELLOW_PAGE_CATEGORIES).equals("") && response.size() > 0) {
                    CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_YELLOW_PAGE_CATEGORIES, response);
                }
            }
        });


    }
}
