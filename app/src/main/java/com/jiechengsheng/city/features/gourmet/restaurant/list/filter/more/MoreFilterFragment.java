package com.jiechengsheng.city.features.gourmet.restaurant.list.filter.more;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.base.BaseEvent;
import com.jiechengsheng.city.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Wangsw  2021/3/30 14:11.
 * 餐厅其他筛选
 */
public class MoreFilterFragment extends BaseFragment {

    private AppCompatEditText startPriceEt, endPriceEt;
    private CheckBox takeawaySupportCb;
    private RadioGroup sort_rg;
    private MaterialRadioButton praiseRb, salesRb, distanceRb;
    private TextView resetTv, confirmTv;
    private ArrayList<String> perPriceArray = new ArrayList<>();

    private MoreFilter moreFilter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_restaurant_filter_more;
    }

    @Override
    protected void initView(View view) {
        startPriceEt = view.findViewById(R.id.start_price_et);
        endPriceEt = view.findViewById(R.id.end_price_et);
        takeawaySupportCb = view.findViewById(R.id.takeaway_support_cb);

        sort_rg = view.findViewById(R.id.sort_rg);
        praiseRb = view.findViewById(R.id.praise_rb);
        salesRb = view.findViewById(R.id.sales_rb);
        distanceRb = view.findViewById(R.id.distance_rb);

        resetTv = view.findViewById(R.id.reset_tv);
        confirmTv = view.findViewById(R.id.confirm_tv);

    }

    @Override
    protected void initData() {
        moreFilter = new MoreFilter();
    }

    @Override
    protected void bindListener() {
        resetTv.setOnClickListener(this::onResetClick);
        confirmTv.setOnClickListener(this::onConfirmClick);
        sort_rg.setOnCheckedChangeListener(this::onSortChanged);

    }

    @SuppressLint("NonConstantResourceId")
    private void onSortChanged(RadioGroup radioGroup, int checkedId) {

        switch (checkedId) {
            case R.id.praise_rb:
                moreFilter.sort = 2;
                break;
            case R.id.sales_rb:
                moreFilter.sort = 3;
                break;
            case R.id.distance_rb:
                moreFilter.sort = 4;
                break;
            default:
                break;

        }


    }


    /**
     * 重置
     */
    private void onResetClick(View view) {

        // data
        moreFilter.sort = 1;
        moreFilter.per_price = null;
        moreFilter.service = null;
        perPriceArray.clear();

        // ui
        startPriceEt.setText("");
        endPriceEt.setText("");
        takeawaySupportCb.setChecked(false);
        praiseRb.setChecked(false);
        salesRb.setChecked(false);
        distanceRb.setChecked(false);

        EventBus.getDefault().post(new BaseEvent<>(moreFilter));
    }


    /**
     * 确认
     */
    private void onConfirmClick(View view) {
        perPriceArray.clear();
        String startPrice = startPriceEt.getText().toString().trim();
        String endPrice = endPriceEt.getText().toString().trim();

        if (!TextUtils.isEmpty(startPrice) && !TextUtils.isEmpty(endPrice)) {
            perPriceArray.add(startPrice);
            perPriceArray.add(endPrice);
            moreFilter.per_price = perPriceArray.toString();
        }
        if (takeawaySupportCb.isChecked()) {
            moreFilter.service = "1";
        } else {
            moreFilter.service = null;
        }
        EventBus.getDefault().post(new BaseEvent<>(moreFilter));
    }


    public static class MoreFilter {

        /**
         * 人均价格
         */
        public String per_price;

        /**
         * 商家服务（1：支持外卖）
         */
        public String service;

        /**
         * （必要参数）排序（1：智能排序，2：好评优先，3：销量优先，4：距离优先）
         */
        public int sort = 1;

    }

}
