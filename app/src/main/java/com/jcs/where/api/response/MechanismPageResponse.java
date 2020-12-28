package com.jcs.where.api.response;

import java.util.List;

/**
 * create by zyf on 2020/12/28 10:53 PM
 */
public class MechanismPageResponse {
    private List<MechanismResponse> data;
    private LinksResponse links;
    private MetaResponse meta;

    public List<MechanismResponse> getData() {
        return data;
    }

    public void setData(List<MechanismResponse> data) {
        this.data = data;
    }

    public LinksResponse getLinks() {
        return links;
    }

    public void setLinks(LinksResponse links) {
        this.links = links;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }
}
