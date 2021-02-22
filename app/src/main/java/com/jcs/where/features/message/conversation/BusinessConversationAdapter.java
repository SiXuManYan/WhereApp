package com.jcs.where.features.message.conversation;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.time.TimeUtil;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by Wangsw  2021/2/20 16:13.
 * 会话列表
 */
public class BusinessConversationAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> implements LoadMoreModule {

    public BusinessConversationAdapter() {
        super(R.layout.item_message_business_conversation);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Conversation data) {

        CircleImageView avatar_civ = holder.getView(R.id.avatar_civ);

        GlideUtil.load(getContext(), data.getPortraitUrl(), avatar_civ);
        holder.setText(R.id.name_tv, data.getConversationTitle());
        holder.setText(R.id.content_tv, data.getConversationTitle());
        holder.setText(R.id.time_tv, TimeUtil.getCountTimeByLong(data.getReceivedTime()));
        TextView unread_count_tv = holder.getView(R.id.unread_count_tv);

        Message.ReceivedStatus receivedStatus = data.getReceivedStatus();
        if (receivedStatus.isRead()) {
            unread_count_tv.setVisibility(View.GONE);
        } else {
            unread_count_tv.setVisibility(View.VISIBLE);
            unread_count_tv.setText(String.valueOf(data.getUnreadMessageCount()));
        }


    }


}
