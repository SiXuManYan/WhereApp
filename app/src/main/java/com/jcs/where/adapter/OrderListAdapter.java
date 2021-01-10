package com.jcs.where.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.OrderListResponse;
import com.jcs.where.hotel.activity.HotelCommentActivity;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.hotel.activity.HotelOrderDetailActivity;
import com.jcs.where.hotel.activity.HotelPayActivity;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 首页订单列表的Adapter
 * create by zyf on 2020/12/11 9:11 PM
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderListResponse, BaseViewHolder> implements UpFetchModule, LoadMoreModule {
    /**
     * 酒店订单状态
     */
    private HashMap<Integer, OrderStatusHolder> mHotelOrderHolder;
    /**
     * 堂食订单状态
     */
    private HashMap<Integer, OrderStatusHolder> mDineOrderHolder;
    /**
     * 外卖订单状态
     */
    private HashMap<Integer, OrderStatusHolder> mTakeawayOrderHolder;
    /**
     * 订单类型：酒店订单
     */
    private final int ORDER_TYPE_HOTEL = 1;

    /**
     * 订单类型：餐饮-堂食
     */
    private final int ORDER_TYPE_DINE = 2;

    /**
     * 订单类型：餐饮-外卖
     */
    private final int ORDER_TYPE_TAKEAWAY = 3;


    public OrderListAdapter(int layoutResId) {
        super(layoutResId);
        initHotelOrderHolder();
        initDineOrderHolder();
        initTakeawayOrderHolder();
   }

    private void initHotelOrderHolder() {
        mHotelOrderHolder = new HashMap<>();
        mHotelOrderHolder.put(1, new OrderStatusHolder(1, "代付款", "取消订单", null, "支付", HotelPayActivity.class));
        mHotelOrderHolder.put(2, new OrderStatusHolder(2, "待使用", "", null, "去使用", HotelOrderDetailActivity.class));
        mHotelOrderHolder.put(3, new OrderStatusHolder(3, "待评价", "去评价", HotelCommentActivity.class, "再次预定", HotelOrderDetailActivity.class));
        //TODO 评价详情页面还没有，查看评价无法跳转
        mHotelOrderHolder.put(4, new OrderStatusHolder(4, "已完成", "查看评价", null, "再次预定", HotelOrderDetailActivity.class));
        mHotelOrderHolder.put(5, new OrderStatusHolder(5, "已取消", "", null, "再次预定", HotelOrderDetailActivity.class));
        mHotelOrderHolder.put(6, new OrderStatusHolder(6, "退款中", "", null, "再次预定", HotelOrderDetailActivity.class));
        mHotelOrderHolder.put(7, new OrderStatusHolder(7, "退款成功", "", null, "再次预定", HotelOrderDetailActivity.class));
        //TODO 退款失败未在UI图中找到对应的
        mHotelOrderHolder.put(8, new OrderStatusHolder(8, "退款失败", "退款失败", HotelDetailActivity.class, "退款失败", null));
    }

    private void initDineOrderHolder(){
        mDineOrderHolder = new HashMap<>();
        mDineOrderHolder.put(1, new OrderStatusHolder(1, "代付款", "取消订单", null, "支付", HotelPayActivity.class));
        mDineOrderHolder.put(2, new OrderStatusHolder(2, "已取消", "", null, "去使用", HotelOrderDetailActivity.class));
        mDineOrderHolder.put(3, new OrderStatusHolder(3, "待使用", "去评价", HotelCommentActivity.class, "再次预定", HotelOrderDetailActivity.class));
        mDineOrderHolder.put(4, new OrderStatusHolder(4, "已完成", "查看评价", null, "再次预定", HotelOrderDetailActivity.class));
        mDineOrderHolder.put(5, new OrderStatusHolder(5, "支付失败", "", null, "再次预定", HotelOrderDetailActivity.class));
        mDineOrderHolder.put(6, new OrderStatusHolder(6, "退款中", "", null, "再次预定", HotelOrderDetailActivity.class));
        mDineOrderHolder.put(7, new OrderStatusHolder(7, "已退款", "", null, "再次预定", HotelOrderDetailActivity.class));
        mDineOrderHolder.put(8, new OrderStatusHolder(8, "退款失败", "退款失败", HotelDetailActivity.class, "退款失败", null));
        mDineOrderHolder.put(9, new OrderStatusHolder(9, "待评价", "去评价", HotelDetailActivity.class, "再次预定", null));
    }

    private void initTakeawayOrderHolder(){
        mTakeawayOrderHolder = new HashMap<>();
        mTakeawayOrderHolder.put(1, new OrderStatusHolder(1, "待支付", "", null, "", HotelPayActivity.class));
        mTakeawayOrderHolder.put(2, new OrderStatusHolder(2, "未接单", "", null, "", HotelOrderDetailActivity.class));
        mTakeawayOrderHolder.put(3, new OrderStatusHolder(3, "已接单", "", HotelCommentActivity.class, "", HotelOrderDetailActivity.class));
        mTakeawayOrderHolder.put(4, new OrderStatusHolder(4, "已取消", "", null, "", HotelOrderDetailActivity.class));
        mTakeawayOrderHolder.put(5, new OrderStatusHolder(5, "已完成", "", null, "", HotelOrderDetailActivity.class));
        mTakeawayOrderHolder.put(6, new OrderStatusHolder(6, "支付失败", "", null, "", HotelOrderDetailActivity.class));
        mTakeawayOrderHolder.put(7, new OrderStatusHolder(7, "退款中", "", null, "", HotelOrderDetailActivity.class));
        mTakeawayOrderHolder.put(8, new OrderStatusHolder(8, "已退款", "", HotelDetailActivity.class, "", null));
        mTakeawayOrderHolder.put(9, new OrderStatusHolder(9, "退款失败", "", HotelDetailActivity.class, "", null));
        mTakeawayOrderHolder.put(10, new OrderStatusHolder(10, "待评价", "", HotelDetailActivity.class, "", null));
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OrderListResponse orderListResponse) {
        List<String> image = orderListResponse.getImage();
        ImageView hotelIconIv = baseViewHolder.findView(R.id.hotelIcon);
        if (hotelIconIv != null && image != null && image.size() > 0) {
            Glide.with(getContext()).load(image.get(0)).into(hotelIconIv);
        }

        baseViewHolder.setText(R.id.orderTitleTv, orderListResponse.getTitle());
        Integer orderStatus = orderListResponse.getModelData().getOrderStatus();
        OrderStatusHolder orderStatusHolder = mHotelOrderHolder.get(orderStatus);
        if (orderStatusHolder != null) {
            baseViewHolder.setText(R.id.orderTypeTv, orderStatusHolder.statusText);
            if (orderStatusHolder.leftText.equals("")) {
                baseViewHolder.setGone(R.id.leftToTv, true);
            } else {
                baseViewHolder.setGone(R.id.leftToTv, false);
                baseViewHolder.setText(R.id.leftToTv, orderStatusHolder.leftText);
                baseViewHolder.setText(R.id.rightToTv, orderStatusHolder.rightText);
            }
        }

        //model 中存储的是酒店、餐饮-堂食、餐饮-外卖的数据
        OrderListResponse.ModelDataDTO modelData = orderListResponse.getModelData();
        switch (orderListResponse.getOrderType()) {
            case ORDER_TYPE_HOTEL:
                baseViewHolder.setText(R.id.hotelDescTv, modelData.getRoomNum() + "间，" + modelData.getRoomType());
                baseViewHolder.setText(R.id.orderDateTv, modelData.getStartDate() + "-" + modelData.getEndDate());
                baseViewHolder.setText(R.id.priceTv, "房价：$" + modelData.getRoomPrice());
                break;
            case ORDER_TYPE_DINE:
                break;
            case ORDER_TYPE_TAKEAWAY:
                break;
        }

    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).getId();
    }

    public Class<? extends AppCompatActivity> getToLeftClass(int position) {

        Integer orderStatus = getData().get(position).getModelData().getOrderStatus();
        OrderStatusHolder orderStatusHolder = mHotelOrderHolder.get(orderStatus);
        Log.e("OrderListAdapter", "getToLeftClass: " + orderStatus);
        if (orderStatusHolder != null) {
            return orderStatusHolder.toLeftClazz;
        } else {
            return null;
        }
    }

    public Class<? extends AppCompatActivity> getToRightClass(int position) {
        Integer orderStatus = getData().get(position).getModelData().getOrderStatus();
        OrderStatusHolder orderStatusHolder = mHotelOrderHolder.get(orderStatus);
        Log.e("OrderListAdapter", "getToRightClass: " + orderStatus);
        if (orderStatusHolder != null) {
            return orderStatusHolder.toRightClazz;
        } else {
            return null;
        }
    }

    public static class OrderStatusHolder {
        public int orderStatus;
        public String statusText;
        public String leftText;
        public Class<? extends AppCompatActivity> toLeftClazz;
        public String rightText;
        public Class<? extends AppCompatActivity> toRightClazz;

        public OrderStatusHolder(int orderStatus, String statusText, String leftText, Class<? extends AppCompatActivity> toLeftClazz, String rightText, Class<? extends AppCompatActivity> toRightClazz) {
            this.orderStatus = orderStatus;
            this.statusText = statusText;
            this.leftText = leftText;
            this.toLeftClazz = toLeftClazz;
            this.rightText = rightText;
            this.toRightClazz = toRightClazz;
        }
    }
}
