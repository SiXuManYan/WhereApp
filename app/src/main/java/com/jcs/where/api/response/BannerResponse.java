package com.jcs.where.api.response;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

public class BannerResponse implements BaseBannerInfo {
    public int id;
    public String src;

    public BannerResponse(int id, String src) {
        this.id = id;
        this.src = src;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public Object getXBannerUrl() {
        return src;
    }

    @Override
    public String getXBannerTitle() {
        return null;
    }
}
