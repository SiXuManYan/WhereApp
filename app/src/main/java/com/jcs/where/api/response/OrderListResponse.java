package com.jcs.where.api.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * create by zyf on 2020/12/11 7:57 PM
 */
public class OrderListResponse implements MultiItemEntity {

    /**
     * 订单类型：酒店订单
     */
    public static final int ORDER_TYPE_HOTEL_1 = 1;

    /**
     * 订单类型：餐饮-堂食
     */
    public static final int ORDER_TYPE_DINE_2 = 2;

    /**
     * 订单类型：餐饮-外卖
     */
    public static final int ORDER_TYPE_TAKEAWAY_3 = 3;


    /**
     * 订单ID
     */
    public int id = 0;

    /**
     * 订单类型（1：酒店订单，2：餐厅-堂食菜品，3：餐厅-外卖订单）
     */
    public Integer order_type = 0;

    /**
     * 订单号
     */
    public String trade_no;

    /**
     * 模块ID,如酒店模块，则为hotel_id
     */
    public Integer model_id;

    /**
     * 订单标题
     */
    public String title;

    /**
     * 创建时间
     */
    public String created_at;

    /**
     * 订单价格
     */
    public Integer price;

    /**
     * 模块数据
     */
    public ModelDataDTO model_data;

    /**
     * 订单图片
     */
    public List<String> image = new ArrayList<>();

    @Override
    public int getItemType() {
        return order_type;
    }


    /**
     * 模式类型（酒店、餐饮-堂食、餐饮-外卖）
     */
    public static class ModelDataDTO {

        // 所用模块的公共参数
        /**
         * 酒店：
         * 订单状态（1：待付款，2：待使用，3：待评价，4：已完成，5：已取消，6：退款中，7：退款成功，8：退款失败，9：商家已取消，10：待确认）
         * <p>
         * 美食：
         * 订单状态（1：待付款，2：已取消，3：待使用，4：已完成，5：支付失败，6：退款中，7：已退款，8：退款失败，9：待评价）
         * <p>
         * 外卖：
         * 订单状态（1：待支付，2：未接单，3：已接单，4：已取消，5：已完成，6：支付失败，7：退款中，8：已退款，9：退款失败，10：待评价）
         */
        public int order_status;


        // ### 酒店 ####

        /**
         * 房间数量
         */
        public int room_num;
        /**
         * 房间类型
         */
        public String room_type;

        /**
         * 入住日期
         */
        public String start_date;

        /**
         * 离店日期
         */
        public String end_date;

        /**
         * 房间价格
         */
        public BigDecimal room_price = BigDecimal.ZERO;

        // ###　美食　###

        /**
         * 食物图片
         */
        public String food_image = "";

        /**
         * 食物名称
         */
        public String food_name = "";

        /**
         * 商品数量
         */
        public int good_num;

        // ### 外卖  ###
        public String good_names = "";


    }
}
