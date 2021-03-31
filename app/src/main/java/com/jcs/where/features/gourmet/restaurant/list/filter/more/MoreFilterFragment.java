package com.jcs.where.features.gourmet.restaurant.list.filter.more;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.jcs.where.R;
import com.jcs.where.base.BaseFragment;

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

    /**
     * 排序规则
     * （1：智能排序(默认)，2：好评优先，3：销量优先，4：距离优先）
     */
    private int sort = 1 ;

    /**
     * 人均价格 开始
     */
    private String perPriceStart = "";

    /**
     * 人均价格 技术
     */
    private String perPriceEnd = "";


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

                break;
            case R.id.sales_rb:

                break;
            case R.id.distance_rb:

                break;

            default:
                break;

        }


    }


    /**
     * 重置
     */
    private void onResetClick(View view) {

    }


    /**
     * 重置
     */
    private void onConfirmClick(View view) {

    }

}
