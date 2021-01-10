package com.jcs.where.api.response;

import java.util.List;

/**
 * create by zyf on 2021/1/10 10:52 上午
 */
public class PageResponse<T> {
    private List<T> data;
    private LinksResponse links;
    private MetaResponse meta;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
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
