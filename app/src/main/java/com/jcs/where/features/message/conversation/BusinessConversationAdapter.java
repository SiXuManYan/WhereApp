package com.jcs.where.features.message.conversation;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.utils.time.TimeUtil;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * Created by Wangsw  2021/2/20 16:13.
 * 会话列表
 */
public class BusinessConversationAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> implements LoadMoreModule {

    /**
     * 文本消息
     */
    private final String TEXT_MESSAGE = "RC:TxtMsg";

    /**
     * 图文消息
     */
    private final String IMAGE_MESSAGE = "RC:ImgMsg";

    /**
     * 语音消息
     */
    private final String VOICE_MESSAGE = "RC:VcMsg";


    public BusinessConversationAdapter() {
        super(R.layout.item_message_business_conversation);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Conversation data) {

        CircleImageView avatar_civ = holder.getView(R.id.avatar_civ);
        TextView name_tv = holder.getView(R.id.name_tv);
        TextView content_tv = holder.getView(R.id.content_tv);
        TextView time_tv = holder.getView(R.id.time_tv);
        TextView unread_count_tv = holder.getView(R.id.unread_count_tv);


        // 判断消息类型
        String objectName = data.getObjectName();
        if (objectName.contains(TEXT_MESSAGE)) {
            TextMessage latestMessage = (TextMessage) data.getLatestMessage();
            content_tv.setText(latestMessage.getContent());
        } else if (objectName.contains(IMAGE_MESSAGE)) {
            content_tv.setText(R.string.image_message);
        } else if (objectName.contains(VOICE_MESSAGE)) {
            content_tv.setText(R.string.voice_message);
        }

        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(data.getTargetId());
        if (userInfo != null) {
            Glide.with(getContext()).load(userInfo.getPortraitUri()).into(avatar_civ);
            name_tv.setText(userInfo.getName());
        }

        time_tv.setText(TimeUtil.getDateTimeFromMillisecond(data.getReceivedTime()));

        Message.ReceivedStatus receivedStatus = data.getReceivedStatus();
        if (receivedStatus.isRead()) {
            unread_count_tv.setVisibility(View.GONE);
        } else {
            unread_count_tv.setVisibility(View.VISIBLE);
            unread_count_tv.setText(String.valueOf(data.getUnreadMessageCount()));
        }

    }


}
