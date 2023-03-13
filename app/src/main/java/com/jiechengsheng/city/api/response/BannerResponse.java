package com.jiechengsheng.city.api.response;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

public class BannerResponse implements BaseBannerInfo {
    public int id;

    public String src;

    /**
     * 	重定向类型（0:不处理，1:h5，2:详情页）
     */
    public int redirect_type;

    /**
     * 模块类型（1:酒店，2:旅游，3:新闻，4:综合服务，5:餐厅 6.新版 mall estore
     */
    public int target_type;

    /**
     * 模块ID
     */
    public int target_id;

    public String h5_link;






    @Override
    public Object getXBannerUrl() {
        return src;
    }

    @Override
    public String getXBannerTitle() {
        return "";
    }
}
