package com.jcs.where.home.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.OrderListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * create by zyf on 2020/12/11 9:11 PM
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderListResponse.DataBean, BaseViewHolder> {
    private HashMap<Integer, String> mOrderStatus;


    public OrderListAdapter(int layoutResId) {
        super(layoutResId);
        mOrderStatus = new HashMap<>();
        mOrderStatus.put(1, "待付款");
        mOrderStatus.put(2, "待使用");
        mOrderStatus.put(3, "待评价");
        mOrderStatus.put(4, "已完成");
        mOrderStatus.put(5, "已取消");
        mOrderStatus.put(6, "退款中");
        mOrderStatus.put(7, "退款成功");
        mOrderStatus.put(8, "退款失败");
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OrderListResponse.DataBean dataBean) {
        List<String> image = dataBean.getImage();
        ImageView hotelIconIv = baseViewHolder.findView(R.id.hotelIcon);
        if (image != null && image.size() > 0) {
            Glide.with(getContext()).load(image.get(0)).into(hotelIconIv);
        }

        baseViewHolder.setText(R.id.orderTitleTv, dataBean.getTitle());
        baseViewHolder.setText(R.id.orderTypeTv, mOrderStatus.get(dataBean.getOrder_status()));
        //model 中存储的是酒店数据
        OrderListResponse.DataBean.ModelDataBean hotelData = dataBean.getModel_data();
        baseViewHolder.setText(R.id.hotelDescTv, hotelData.getRoom_num() + "间，" + hotelData.getRoom_type());
        baseViewHolder.setText(R.id.orderDateTv, hotelData.getStart_date() + "-" + hotelData.getEnd_date());
        baseViewHolder.setText(R.id.priceTv, "房价：$" + hotelData.getRoom_price());

    }
}
