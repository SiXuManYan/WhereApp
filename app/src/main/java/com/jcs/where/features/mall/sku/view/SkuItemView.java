package com.jcs.where.features.mall.sku.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jcs.where.R;


/**
 * Created by wuhenzhizao on 2017/7/31.
 */

public class SkuItemView extends AppCompatTextView {
    private String attributeValue;

    public SkuItemView(Context context) {
        super(context);
        init();
    }

    public SkuItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SkuItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.sku_item_selector);
        setTextColor(getResources().getColorStateList(R.color.sku_item_text_selector,null));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        setSingleLine();
        setGravity(Gravity.CENTER);
        setPadding(SizeUtils.dp2px(18f), 0, SizeUtils.dp2px(18f), 0);
        setMinWidth(SizeUtils.dp2px(45f));
        setMaxWidth(SizeUtils.dp2px(200f));
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
        setText(attributeValue);
    }
}
