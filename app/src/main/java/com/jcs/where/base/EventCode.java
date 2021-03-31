package com.jcs.where.base;

/**
 * create by zyf on 2021/1/11 10:08 下午
 */
public class EventCode {
    public static final int EVENT_LOGIN_SUCCESS = 1001;
    public static final int EVENT_UPDATE_FOLLOW_CHANNEL = 1002;

    /**
     * 更新签到状态
     */
    public static final int EVENT_SIGN_IN_CHANGE_STATUS = 1003;

    /**
     * 请求签到
     */
    public static final int EVENT_SIGN_IN_REQUEST = 1004;

    /**
     * 密码重置成功
     */
    public static final int EVENT_PASSWORD_RESET_SUCCESS = 1005;

    /**
     * 退出登录
     */
    public static final int EVENT_SIGN_OUT = 1006;

    /**
     * 手机号绑定成功
     */
    public static final int EVENT_BIND_PHONE_SUCCESS = 1007;

    /**
     * 注册成功
     */
    public static final int EVENT_REGISTER_SUCCESS = 1008;

    /**
     * 刷新用户信息
     */
    public static final int EVENT_REFRESH_USER_INFO = 1009;

    /**
     * 刷新消息未读数量
     */
    public static final int EVENT_REFRESH_UNREAD_MESSAGE_COUNT = 1010;

    /**
     * 刷新地址列表
     */
    public static final int EVENT_ADDRESS = 1011;

    /**
     * 选择区域
     */
    public static final int EVENT_FILTER_BY_AREA = 1012;
}
