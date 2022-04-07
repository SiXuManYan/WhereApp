package com.jcs.where.search.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;

import org.jetbrains.annotations.NotNull;

/**
 * create by zyf on 2021/1/31 2:33 下午
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchHistoryAdapter() {
        super(R.layout.item_searchhistory);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
        TextView searchHistoryTv = baseViewHolder.findView(R.id.tv_searchhistory);
        searchHistoryTv.setText(s);
    }
}
