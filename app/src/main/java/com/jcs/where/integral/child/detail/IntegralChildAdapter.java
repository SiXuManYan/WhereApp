package com.jcs.where.integral.child.detail;

import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.IntegralDetailResponse;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Wangsw  2021/1/27 15:06.
 * 积分明细列表
 */
public class IntegralChildAdapter extends BaseQuickAdapter<IntegralDetailResponse, BaseViewHolder> {

    public IntegralChildAdapter() {
        super(R.layout.item_integral_child);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, IntegralDetailResponse data) {
        TextView title_tv = holder.getView(R.id.title_tv);
        TextView time_tv = holder.getView(R.id.time_tv);
        TextView score_tv = holder.getView(R.id.score_tv);

        int type = Math.max(data.type - 1, 0);
        String[] stringArray = StringUtils.getStringArray(R.array.sign_in_type);
        title_tv.setText(stringArray[type]);

        time_tv.setText(data.created_at);
        score_tv.setText(StringUtils.getString(R.string.add_format,data.integral));


    }
}
