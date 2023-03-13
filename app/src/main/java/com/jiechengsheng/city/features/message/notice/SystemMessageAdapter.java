package com.jiechengsheng.city.features.message.notice;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.response.message.SystemMessageResponse;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Wangsw  2021/2/20 16:13.
 * 系统消息列表
 */
public class SystemMessageAdapter extends BaseQuickAdapter<SystemMessageResponse, BaseViewHolder> implements LoadMoreModule {

    public SystemMessageAdapter() {
        super(R.layout.item_message_system_notification);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SystemMessageResponse data) {


        ImageView is_read_iv = holder.getView(R.id.is_read_iv);

        holder.setText(R.id.title_tv, data.title);
        holder.setText(R.id.content_tv, data.message);
        holder.setText(R.id.date_tv, data.created_at);

        // 跳转类型
        if (data.detail_type == 1) {
            holder.getView(R.id.link_tv).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.link_tv).setVisibility(View.GONE);
        }

        if (data.is_read == 0) {
            is_read_iv.setVisibility(View.VISIBLE);
        } else {
            is_read_iv.setVisibility(View.GONE);
        }




    }


}
