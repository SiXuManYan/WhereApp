package com.jcs.where.features.search;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.search.SearchResultResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Wangsw  2021/2/25 10:26.
 */
public class SearchAllPresenter extends BaseMvpPresenter {

    private SearchAllView view;

    public SearchAllPresenter(SearchAllView baseMvpView) {
        super(baseMvpView);
        this.view = baseMvpView;
    }


    /**
     * 搜索数据
     *
     * @param finalInput
     */
    public void search(String finalInput) {
        requestApi(mRetrofit.getSearchResult(finalInput), new BaseMvpObserver<List<SearchResultResponse>>(view) {
            @Override
            protected void onSuccess(List<SearchResultResponse> response) {
                /*
                if (response != null && !response.isEmpty()) {
                    // 保存搜索记录
                    saveSearchHistory(finalInput);
                }
                */
                view.bindSearchResult(response);
            }
        });
    }

    /**
     * 保存搜索记录
     */
    private void saveSearchHistory(String finalInput) {
        SPUtils shareDefault = CacheUtil.getShareDefault();
        String key = Constant.SP_SEARCH_HISTORY;

        String history = shareDefault.getString(key, "");
        if (history.isEmpty()) {
            shareDefault.put(key, history);
            return;
        }
        shareDefault.put(key, history + "," + finalInput);
    }

    /**
     * 获取搜索历史记录
     *
     * @return
     */
    public List<String> getSearchHistory() {

        List<String> historyList = new ArrayList<>();

        SPUtils shareDefault = CacheUtil.getShareDefault();
        String key = Constant.SP_SEARCH_HISTORY;
        String history = shareDefault.getString(key, "");

        if (history.contains(",")) {
            String[] split = history.split(",");
            historyList.addAll(Arrays.asList(split));
        } else {
            historyList.add(history);
        }

        return historyList;

    }

    /**
     * 清空搜索记录
     */
    public void clearSearchHistory() {
        CacheUtil.getShareDefault().put(Constant.SP_SEARCH_HISTORY, "");
        ToastUtils.showShort(R.string.clear_success);
    }


}
