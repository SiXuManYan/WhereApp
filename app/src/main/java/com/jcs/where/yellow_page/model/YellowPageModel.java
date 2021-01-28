package com.jcs.where.yellow_page.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.JcsResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.utils.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
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
                                 String search, BaseObserver<PageResponse<MechanismResponse>> observer) {

        dealResponse(mRetrofit.getMechanismListById(categoryId, search, Constant.LAT, Constant.LNG), observer);

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

    public void getInitData(String categoryIds, BaseObserver<YellowPageZipResponse> observer) {

        // 获取机构列表
        Observable<JcsResponse<PageResponse<MechanismResponse>>> mechanismListByIdObservable = mRetrofit.getMechanismListById(categoryIds, "", Constant.LAT, Constant.LNG);
        // 获取一级分类
        Observable<JcsResponse<List<CategoryResponse>>> categoriesObservable = mRetrofit.getAllChildCategories(1, categoryIds);
        Observable<JcsResponse<YellowPageZipResponse>> zip = Observable.zip(mechanismListByIdObservable, categoriesObservable, new BiFunction<JcsResponse<PageResponse<MechanismResponse>>, JcsResponse<List<CategoryResponse>>, JcsResponse<YellowPageZipResponse>>() {
            @NotNull
            @Override
            public JcsResponse<YellowPageZipResponse> apply(@NotNull JcsResponse<PageResponse<MechanismResponse>> mechanismPageResponseJcsResponse, @NotNull JcsResponse<List<CategoryResponse>> listJcsResponse) throws Exception {
                JcsResponse<YellowPageZipResponse> jcsResponse = new JcsResponse<>();
                int code = mechanismPageResponseJcsResponse.getCode();
                int code2 = listJcsResponse.getCode();
                if (code != 200) {
                    jcsResponse.setCode(code);
                    jcsResponse.setMessage(mechanismPageResponseJcsResponse.getMessage());
                    return jcsResponse;
                }

                if (code2 != 200) {
                    jcsResponse.setCode(code2);
                    jcsResponse.setMessage(listJcsResponse.getMessage());
                    return jcsResponse;
                }

                jcsResponse.setCode(200);
                jcsResponse.setMessage("success");
                YellowPageZipResponse zipResponse = new YellowPageZipResponse(mechanismPageResponseJcsResponse.getData(), listJcsResponse.getData());
                jcsResponse.setData(zipResponse);
                return jcsResponse;
            }
        });
        dealResponse(zip, observer);
    }

    public static class YellowPageZipResponse {
        PageResponse<MechanismResponse> mechanismPageResponse;
        List<CategoryResponse> categories;

        public YellowPageZipResponse(PageResponse<MechanismResponse> mechanismPageResponse, List<CategoryResponse> categories) {
            this.mechanismPageResponse = mechanismPageResponse;
            this.categories = categories;
        }

        public PageResponse<MechanismResponse> getMechanismPageResponse() {
            return mechanismPageResponse;
        }

        public List<CategoryResponse> getCategories() {
            return categories;
        }
    }

}
