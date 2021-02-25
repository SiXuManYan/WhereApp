package com.jcs.where.features.search;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.search.SearchResultResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/2/25 10:25.
 */
public interface SearchAllView extends BaseMvpView {

    /**
     * 设置搜索结果
     * @param response
     */
    void bindSearchResult(List<SearchResultResponse> response);
}
