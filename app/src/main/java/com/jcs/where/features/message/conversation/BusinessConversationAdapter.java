package com.jcs.where.features.message.conversation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;

import org.jetbrains.annotations.NotNull;

import io.rong.imlib.model.Conversation;

/**
 * Created by Wangsw  2021/2/20 16:13.
 * 会话列表
 */
public class BusinessConversationAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> implements LoadMoreModule {

    public BusinessConversationAdapter() {
        super(R.layout.item_message_business_conversation);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Conversation conversation) {

    }


}
