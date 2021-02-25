package com.jcs.where.features.search;

import android.text.SpannableString;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.search.SearchResultResponse;
import com.jcs.where.utils.FeaturesUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Wangsw  2021/2/25 14:42.
 * 搜索结果 adapter
 */
public class SearchAllAdapter extends BaseQuickAdapter<SearchResultResponse, BaseViewHolder> {

    public String keyWord = "";


    public SearchAllAdapter() {
        super(R.layout.item_search_all);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SearchResultResponse response) {
        TextView name_tv = holder.getView(R.id.name_tv);
        SpannableString spannableString = FeaturesUtil.getHighLightKeyWord(ColorUtils.getColor(R.color.blue_4C9EF2), response.name, keyWord);
        name_tv.setText(spannableString);
    }
}
