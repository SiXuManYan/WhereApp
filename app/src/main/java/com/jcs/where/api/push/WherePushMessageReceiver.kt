package com.jcs.where.api.push

import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.*
import cn.jpush.android.service.JPushMessageReceiver
import com.comm100.livechat.VisitorClientInterface
import com.google.gson.Gson
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant

/**
 * 处理推送消息
 */
class WherePushMessageReceiver : JPushMessageReceiver() {

    companion object {
        private const val TAG = "JIGUANG-WherePushMessageReceiver"
    }


    /**
     * 注册成功回调
     */
    override fun onRegister(context: Context, registrationId: String) {
        Log.e(TAG, "[onRegister] $registrationId")

        val intent = Intent(Constant.PUSH_RECEIVER_ACTION)
        intent.putExtra("rid", registrationId)
        context.sendBroadcast(intent)
    }


    /**
     * 长连接状态回调
     * @param isConnected
     */
    override fun onConnected(context: Context, isConnected: Boolean) {
        Log.e(TAG, "[onConnected] $isConnected")
        if (isConnected) {
            val registrationID = JPushInterface.getRegistrationID(context)
            Log.e(TAG, "[onConnected ,  registrationID === ] $registrationID")

            // 设置common 100 id
            VisitorClientInterface.setRemoteNotificationDeviceId(registrationID)
        }

    }

    /**
     * 自定义消息回调
     */
    override fun onMessage(context: Context, customMessage: CustomMessage) {
        Log.e(TAG, "[onMessage] $customMessage")
        val intent = Intent(Constant.PUSH_RECEIVER_ACTION)
        intent.putExtra("msg", customMessage.message)
        context.sendBroadcast(intent)
        super.onMessage(context, customMessage)

    }


    /**
     * 收到通知回调  1
     *
     * @param message 接收到的通知内容
     */
    override fun onNotifyMessageArrived(context: Context, message: NotificationMessage) {
        super.onNotifyMessageArrived(context, message)
        Log.e(TAG, "[onNotifyMessageArrived] $message")
        // NotificationMessage{notificationId=508822366, msgId='18100303377322203', appkey='c1845048016655585610609e', notificationContent='ghjgj', notificationAlertType=-1, notificationTitle='Where', notificationSmallIcon='', notificationLargeIcon='', notificationExtras='{"target_id":"2865","type":"news_video"}', notificationStyle=0, notificationBuilderId=0, notificationBigText='', notificationBigPicPath='', notificationInbox='', notificationPriority=0, notificationCategory='', developerArg0='developerArg0', platform=0, notificationChannelId='', displayForeground='', notificationType=0', inAppMsgType=1', inAppMsgShowType=2', inAppMsgShowPos=0', inAppMsgTitle=, inAppMsgContentBody=, inAppType=0}

    }


    /**
     * 点击通知回调
     * @param message 点击的通知内容
     */
    override fun onNotifyMessageOpened(context: Context, message: NotificationMessage) {
        super.onNotifyMessageOpened(context, message)
        Log.e(TAG, "[onNotifyMessageOpened] $message")

        // '{"target_id":"2865","type":"news_video"}'


        val extras = message.notificationExtras
        if (extras.isNullOrBlank()) {
            return
        }

        try {
            //打开自定义的Activity
            val gson = Gson()
            val model = gson.fromJson(extras, NotificationExtras::class.java)
            val deepLinksTargetIntent = BusinessUtils.getDeepLinksTargetIntent(model.type, model.target_id, context)
            context.startActivity(deepLinksTargetIntent)

        } catch (throwable: Throwable) {
        }
    }

    /**
     * 通知的 MultiAction 回调
     */
    override fun onMultiActionClicked(context: Context, intent: Intent) {
        super.onMultiActionClicked(context, intent)
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮")
        val nActionExtra = intent.extras!!.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        /*
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
        */
    }


    /**
     * 清除通知回调
     * @param message 清除的通知内容
     */
    override fun onNotifyMessageDismiss(context: Context, message: NotificationMessage) {
        Log.e(TAG, "[onNotifyMessageDismiss] $message")
    }


    /**
     * 交互事件回调
     */
    override fun onCommandResult(context: Context, cmdMessage: CmdMessage) {
        Log.e(TAG, "[onCommandResult] $cmdMessage")
    }

    /**
     * 标签操作回调，tag 增删查改的操作会在此方法中回调结果。
     * @param jPushMessage tag 相关操作返回的消息结果体，具体参考 JPushMessage 类的说明。
     */
    override fun onTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage)
        super.onTagOperatorResult(context, jPushMessage)
    }

    /**
     * 查询标签绑定状态回调
     */
    override fun onCheckTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage)
        super.onCheckTagOperatorResult(context, jPushMessage)
    }

    /**
     * 别名操作回调
     */
    override fun onAliasOperatorResult(context: Context, jPushMessage: JPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage)
        super.onAliasOperatorResult(context, jPushMessage)
    }

    /**
     * 设置手机号码回调
     */
    override fun onMobileNumberOperatorResult(context: Context, jPushMessage: JPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage)
        super.onMobileNumberOperatorResult(context, jPushMessage)
    }

    /**
     * 通知开关状态回调
     * 该方法会在以下情况触发时回调。
     * 1.sdk 每次启动后都会检查通知开关状态并通过该方法回调给开发者
     * 2.当 sdk 检查到通知状态变更时会通过该方法回调给开发者。
     *
     * @param isOn    通知开关状态
     * @param source  触发场景，0 为 sdk 启动，1 为检测到通知开关状态变更
     */
    override fun onNotificationSettingsCheck(context: Context, isOn: Boolean, source: Int) {
        super.onNotificationSettingsCheck(context, isOn, source)
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:$isOn,source:$source")
    }

    /**
     * 通知未展示回调
     * 1.3.5.8 之后支持推送时指定前台不展示功能，当通知未展示时，会回调该接口
     */
    override fun onNotifyMessageUnShow(context: Context, notificationMessage: NotificationMessage) {
        super.onNotifyMessageUnShow(context, notificationMessage)
    }


}