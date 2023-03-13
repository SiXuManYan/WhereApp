package com.jiechengsheng.city.search.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.search.bean.ISearchResponse;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by zyf on 2021/1/31 2:34 下午
 */
public class RecommendAdapter extends BaseQuickAdapter<ISearchResponse, BaseViewHolder> {

    public RecommendAdapter() {
        super(R.layout.item_recommendsearch);
    }
    private String mInputText = "";

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ISearchResponse data) {
        TextView nameTv = baseViewHolder.findView(R.id.tv_name);
        nameTv.setText(matcherSearchText(data.getName(), mInputText));
    }

    public SpannableString matcherSearchText(String text, String keyword) {
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//new ForegroundColorSpan(color)
        }
        return ss;
    }

    public void setInputText(String inputText) {
        this.mInputText = inputText;
    }
}
