package com.jcs.where.yellow_page.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismPageResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * create by zyf on 2021/1/3 11:03 AM
 */
public class YellowPageModel extends BaseModel {

    /**
     * @param categoryId 分类id集合
     * @param search     查询字段
     */
    public void getMechanismList(String categoryId,
                                 String search, BaseObserver<MechanismPageResponse> observer) {

        dealResponse(mRetrofit.getMechanismListById(categoryId, search), observer);

    }

    /**
     * 根据一个分类id集合，获得对应的分类数据
     *
     * @param categories 分类id集合
     */
    public void getCategories(int level, String categories, BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(level, categories), observer);
    }

    /**
     * 根据一个一级分类分类id，获得该分类所有的二级分类
     *
     * @param categoryId 分类id
     */
    public void getCategories(int categoryId, BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(2, String.valueOf(categoryId)), observer);
    }

    public void getInitData(List<Integer> categoryIds, BaseObserver<YellowPageZipResponse> observer) {

        Observable<MechanismPageResponse> mechanismListByIdObservable = mRetrofit.getMechanismListById(categoryIds.toString(), "");
        // 获取一级分类
        Observable<List<CategoryResponse>> categoriesObservable = mRetrofit.getCategories(1, categoryIds.toString());
        Observable<YellowPageZipResponse> zip = Observable.zip(mechanismListByIdObservable, categoriesObservable, new BiFunction<MechanismPageResponse, List<CategoryResponse>, YellowPageZipResponse>() {
            @NonNull
            @Override
            public YellowPageZipResponse apply(@NonNull MechanismPageResponse mechanismPageResponse, @NonNull List<CategoryResponse> categoryResponses) throws Exception {

                return new YellowPageZipResponse(mechanismPageResponse, categoryResponses);
            }
        });
        dealResponse(zip, observer);
    }

    public static class YellowPageZipResponse {
        MechanismPageResponse mechanismPageResponse;
        List<CategoryResponse> categories;

        public YellowPageZipResponse(MechanismPageResponse mechanismPageResponse, List<CategoryResponse> categories) {
            this.mechanismPageResponse = mechanismPageResponse;
            this.categories = categories;
        }

        public MechanismPageResponse getMechanismPageResponse() {
            return mechanismPageResponse;
        }

        public List<CategoryResponse> getCategories() {
            return categories;
        }
    }

}
