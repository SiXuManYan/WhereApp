package com.jcs.where.features.message.cloud

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.features.message.custom.CustomMessage
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import io.rong.imkit.IMCenter
import io.rong.imkit.RongIM
import io.rong.imkit.config.ConversationClickListener
import io.rong.imkit.conversation.ConversationFragment
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.UserInfo
import kotlinx.android.synthetic.main.activity_conversation.*


/**
 * Created by Wangsw  2021/2/24 10:49.
 * 融云会话聊天页面
 */
class ConversationActivity : BaseActivity() {

    /**
     * 0 内置会话
     * 1 estore 商城会话，携带商品
     */
    private var conversationType = 0


    private var targetId = ""

    private lateinit var mallGoodMessageData: CustomMessage
    private lateinit var conversationFragment: ConversationFragment


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_conversation

    override fun initView() {
        initExtra()
    }

    private fun initExtra() {
        intent.extras?.let {

            conversationType = it.getInt(Constant.PARAM_TYPE, 0)
            targetId = it.getString("targetId", "")

            chat_title_tv.text = it.getString(Constant.PARAM_TITLE, "")

            val phone = it.getString(Constant.PARAM_PHONE, "")
            if (TextUtils.isEmpty(phone)) {
                chat_tel_iv.visibility = View.GONE
            } else {
                chat_tel_iv.visibility = View.VISIBLE
                chat_tel_iv.setOnClickListener { v: View? ->
                    val i = Intent(Intent.ACTION_DIAL)
                    i.data = Uri.parse("tel:$phone")
                    startActivity(i)
                }
            }

            if (it.containsKey(Constant.PARAM_GOOD_DATA)) {
                mallGoodMessageData = it.getParcelable<CustomMessage>(Constant.PARAM_GOOD_DATA)!!
            }

        }


    }


    override fun initData() {
        // 添加会话界面
        conversationFragment = ConversationFragment()

        if (conversationType == 1) {

            val footer = LayoutInflater.from(this).inflate(R.layout.layout_rong_cloud_mall_footer, null)
            footer.apply {
                val image_iv = findViewById<ImageView>(R.id.image_iv)
                val title_tv = findViewById<TextView>(R.id.title_tv)
                val price_tv = findViewById<TextView>(R.id.price_tv)

                val send_tv = findViewById<TextView>(R.id.send_tv)
                val cancel_send_iv = findViewById<ImageView>(R.id.cancel_send_iv)

                GlideUtil.load(this@ConversationActivity, mallGoodMessageData.goodsImage, image_iv)
                title_tv.text = mallGoodMessageData.goodsName
                price_tv.text = StringUtils.getString(R.string.price_unit_format, mallGoodMessageData.goodsPrice)

                cancel_send_iv.setOnClickListener {
                    this.visibility = View.GONE
                }

                send_tv.setOnClickListener {
                    this.visibility = View.GONE
                    // 发送自定义消息
                    sendMallGoodMessage(mallGoodMessageData)
                }
                setOnClickListener {
                    finish()
                }

            }

            conversationFragment.addFooterView(footer)

            KeyboardUtils.showSoftInput()

        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, conversationFragment)
        transaction.commit()


    }


    override fun bindListener() {

        RongIM.setConversationClickListener(object : ConversationClickListener {


            override fun onUserPortraitClick(
                context: Context?,
                conversationType: Conversation.ConversationType?,
                user: UserInfo?,
                targetId: String?,
            ): Boolean = false

            override fun onUserPortraitLongClick(
                context: Context?,
                conversationType: Conversation.ConversationType?,
                user: UserInfo?,
                targetId: String?,
            ): Boolean = false


            override fun onMessageLongClick(context: Context?, view: View?, message: Message?): Boolean = false

            override fun onMessageLinkClick(context: Context?, link: String?, message: Message?): Boolean = false

            override fun onReadReceiptStateClick(context: Context?, message: Message?): Boolean = false

            override fun onMessageClick(context: Context, view: View?, message: Message): Boolean {

                if (message.content is CustomMessage) {

                    val customMessage = message.content as CustomMessage

                    MallDetailActivity.navigation(context, customMessage.goodsID)

                    return true
                }



                return false
            }

        })

    }


    private fun sendMallGoodMessage(messageContent: CustomMessage) {

        var conversationType = Conversation.ConversationType.PRIVATE

        /**
         * 当下发远程推送消息时，在通知栏里会显示这个字段。
         * 如果发送的是自定义消息，该字段必须填写，否则无法收到远程推送消息。
         * 如果发送 sdk 中默认的消息类型，例如文本消息、图片消息等，则不需要填写，默认已经指定。
         */
        val pushContent = "estore 商城"
        val pushData = "estore 商品"

        val message = Message.obtain(targetId, conversationType, messageContent)
        IMCenter.getInstance().sendMessage(message, pushContent, pushData, object : IRongCallback.ISendMessageCallback {

            override fun onAttached(message: Message?) = Unit

            override fun onSuccess(message: Message?) {

                ToastUtils.showShort("send success ")
            }

            override fun onError(message: Message?, errorCode: RongIMClient.ErrorCode?) {
                ToastUtils.showShort("send fail ")
            }

        })


    }
}