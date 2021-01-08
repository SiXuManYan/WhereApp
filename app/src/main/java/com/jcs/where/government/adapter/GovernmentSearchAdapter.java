package com.jcs.where.government.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.SearchResponse;

import org.jetbrains.annotations.NotNull;

/**
 * create by zyf on 2021/1/7 3:39 下午
 */
public class GovernmentSearchAdapter extends BaseQuickAdapter<MechanismResponse, BaseViewHolder> {
    public GovernmentSearchAdapter() {
        super(R.layout.item_search);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MechanismResponse mechanismResponse) {
        baseViewHolder.setText(R.id.searchTitleTv, mechanismResponse.getTitle());
        baseViewHolder.setGone(R.id.line, baseViewHolder.getAdapterPosition() == getItemCount() - 1);
    }

}
