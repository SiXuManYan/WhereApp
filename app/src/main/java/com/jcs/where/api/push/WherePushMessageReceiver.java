package com.jcs.where.api.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jcs.where.utils.Constant;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class WherePushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "WherePushMessageReceiver";

    /**
     * 自定义消息回调，可通知
     */
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        Intent intent = new Intent(Constant.PUSH_RECEIVER_ACTION);
        intent.putExtra("msg", customMessage.message);
        context.sendBroadcast(intent);
    }

    /**
     * 点击通知回调
     * @param message 点击的通知内容
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        try{
            //打开自定义的Activity

//            Intent i = new Intent(context, TestActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE,message.notificationTitle);
//            bundle.putString(JPushInterface.EXTRA_ALERT,message.notificationContent);
//            i.putExtras(bundle);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);

        }catch (Throwable throwable){

        }
    }


    /**
     * 通知的 MultiAction 回调
     * @param context
     * @param intent
     */
    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");

        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

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
     * 收到通知回调
     * @param context
     * @param message 接收到的通知内容
     */
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
    }


    /**
     * 清除通知回调
     * @param message 清除的通知内容
     */
    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }


    /**
     * 注册成功回调
     * @param context
     * @param registrationId
     */
    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
        Intent intent = new Intent(Constant.PUSH_RECEIVER_ACTION);
        intent.putExtra("rid", registrationId);
        context.sendBroadcast(intent);
    }


    /**
     * 长连接状态回调
     * @param context
     * @param isConnected
     */
    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }


    /**
     * 交互事件回调
     * @param context
     * @param cmdMessage
     */
    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);



    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }


    /**
     * 查询标签绑定状态回调
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }


    /**
     * 别名操作回调
     * @param jPushMessage
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    /**
     * 设置手机号码回调
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }


    /**
     * 通知开关状态回调
     * 该方法会在以下情况触发时回调。
     * 1.sdk 每次启动后都会检查通知开关状态并通过该方法回调给开发者
     * 2.当 sdk 检查到通知状态变更时会通过该方法回调给开发者。
     *
     * @param context
     * @param isOn  通知开关状态
     * @param source 触发场景，0 为 sdk 启动，1 为检测到通知开关状态变更
     */
    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
    }


    /**
     * 通知未展示回调
     * 1.3.5.8 之后支持推送时指定前台不展示功能，当通知未展示时，会回调该接口
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageUnShow(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageUnShow(context, notificationMessage);
    }
}
