package com.jcs.where.government.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.utils.Constant;

/**
 * create by zyf on 2021/3/11 11:15 下午
 */
public class ConvenienceServiceSearchModel extends BaseModel {
    /**
     * 获得综合服务、政府机构列表数据
     */
    public void getMechanismList(String categoryId, BaseObserver<PageResponse<MechanismResponse>> observer) {

        dealResponse(mRetrofit.getMechanismListById(categoryId, "", Constant.LAT, Constant.LNG), observer);
    }
}
