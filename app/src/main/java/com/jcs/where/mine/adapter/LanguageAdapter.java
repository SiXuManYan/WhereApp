package com.jcs.where.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;

import org.jetbrains.annotations.NotNull;

/**
 * create by zyf on 2021/1/10 8:07 下午
 */
public class LanguageAdapter extends BaseQuickAdapter<LanguageAdapter.LanguageBean, BaseViewHolder> {

    public LanguageAdapter() {
        super(R.layout.item_language);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LanguageBean languageBean) {
        holder.setText(R.id.languageTitleTv, languageBean.title);
        if (languageBean.isSelected) {
            holder.setGone(R.id.languageCheckedIcon, false);
        } else {
            holder.setGone(R.id.languageCheckedIcon, true);
        }

        if (holder.getAdapterPosition() == getItemCount() - 1) {
            holder.setGone(R.id.bottomLine, true);
        }
    }

    public static class LanguageBean {
        public String title;
        public String local;
        public boolean isSelected;

        public LanguageBean() {
        }

        public LanguageBean(String title, String local, boolean isSelected) {
            this.title = title;
            this.local = local;
            this.isSelected = isSelected;
        }
    }
}
