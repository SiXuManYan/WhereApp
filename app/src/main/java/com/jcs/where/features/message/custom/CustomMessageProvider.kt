package com.jcs.where.features.message.custom

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.utils.GlideUtil
import io.rong.imkit.conversation.messgelist.provider.BaseMessageItemProvider
import io.rong.imkit.model.UiMessage
import io.rong.imkit.widget.adapter.IViewProviderListener
import io.rong.imkit.widget.adapter.ViewHolder
import io.rong.imlib.model.MessageContent


/**
 * Created by Wangsw  2022/2/22 11:17.
 * 融云自定义消息展示
 * https://doc.rongcloud.cn/im/Android/5.X/ui/conversation/customMsgProvider
 */
class CustomMessageProvider : BaseMessageItemProvider<CustomMessage>() {

    init {
        // 修改模版属性
        mConfig.apply {
            showPortrait = false // 隐藏头像
            showSummaryWithName = false // 是否在会话的内容体里显示发送者名字
            showReadState = true // 显示已读回执状态。
            showContentBubble = false // 隐藏气泡
        }

    }


    /**
     * 创建 ViewHolder
     */
    override fun onCreateMessageContentViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rong_cloud_store_message, null, false)
        return ClassViewHolder(parent.context, view)
    }

    /**
     * 设置消息视图里各 view 的值
     * @param holder ViewHolder
     * @param parentHolder 父布局的 ViewHolder
     * @param customMessage 此展示模板对应的消息
     * @param uiMessage {@link UiMessage}
     * @param position 消息位置
     * @param list 列表
     * @param listener ViewModel 的点击事件监听器。如果某个子 view 的点击事件需要 ViewModel 处理，可通过此监听器回调。
     */
    override fun bindMessageContentViewHolder(
        holder: ViewHolder,
        parentHolder: ViewHolder,
        customMessage: CustomMessage,
        uiMessage: UiMessage,
        position: Int,
        list: List<UiMessage>,
        listener: IViewProviderListener<UiMessage>,
    ) {
        val classViewHolder = holder as ClassViewHolder

        GlideUtil.load(classViewHolder.context, customMessage.goodsImage, classViewHolder.image_iv)
        classViewHolder.title_tv.text = customMessage.goodsName
        classViewHolder.price_tv.text = StringUtils.getString(R.string.price_unit_format,customMessage.goodsPrice)

    }

    override fun onItemClick(
        holder: ViewHolder,
        customMessage: CustomMessage?,
        uiMessage: UiMessage,
        position: Int,
        list: List<UiMessage>,
        listener: IViewProviderListener<UiMessage>,
    ): Boolean {
        return false
    }

    /**
     * 根据消息内容，判断是否为本模板需要展示的消息类型
     */
    override fun isMessageViewType(messageContent: MessageContent): Boolean {
        return messageContent is CustomMessage
    }


    /**
     * 在会话列表页某条会话最后一条消息为该类型消息时，会话里需要展示的内容。
     * 比如: 图片消息在会话里需要展示为"图片"，那返回对应的字符串资源即可。
     */
    override fun getSummarySpannable(context: Context?, t: CustomMessage?): Spannable {
        return SpannableString("商品消息")
    }
}

class ClassViewHolder(context: Context?, itemView: View) : ViewHolder(context, itemView) {

    var image_iv: ImageView = itemView.findViewById(R.id.image_iv)
    var title_tv: TextView = itemView.findViewById(R.id.title_tv)
    var price_tv: TextView = itemView.findViewById(R.id.price_tv)

}