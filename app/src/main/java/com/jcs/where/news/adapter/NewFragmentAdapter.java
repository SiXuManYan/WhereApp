package com.jcs.where.news.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author : hwd
 * date   : 2021/1/6-23:45
 */
public class NewFragmentAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public NewFragmentAdapter( @Nullable List<String> data) {
        super(R.layout.item_news, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {

    }
}
