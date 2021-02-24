package com.jcs.where.features.message.cloud;

import android.net.Uri;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcs.where.BuildConfig;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Wangsw  2021/2/20 14:46.
 * 融云会话 列表 Activity
 * @see <a href="https://www.rongcloud.cn/docs/api/android/imlib/io/rong/imlib/RongIMClient.html#getConversationList-io.rong.imlib.RongIMClient">融云API 文档</a>
 *
 */
public class ConversationListActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_conversation_list;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        ConversationListFragment conversationListFragment = new ConversationListFragment();
        // 此处设置 Uri. 通过 appendQueryParameter 去设置所要支持的会话类型. 例如
        // .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")
        // 表示支持单聊会话, false 表示不聚合显示, true 则为聚合显示
        Uri uri = Uri.parse("rong://" + BuildConfig.APPLICATION_ID).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .build();
        conversationListFragment.setUri(uri);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, conversationListFragment);
        transaction.commit();
    }

    @Override
    protected void bindListener() {

    }


}
