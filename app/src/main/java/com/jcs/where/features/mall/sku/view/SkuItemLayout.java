package com.jcs.where.features.mall.sku.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jcs.where.R;
import com.jcs.where.features.mall.sku.bean.SkuAttribute;
import com.jcs.where.features.mall.sku.utils.ScreenUtils;
import com.jcs.where.features.mall.sku.utils.ViewUtils;
import com.jcs.where.features.mall.sku.widget.FlowLayout;

import java.util.List;


public class SkuItemLayout extends LinearLayout {

    /**
     * 组标题
     */
    private TextView attrNameTv;
    private FlowLayout attrValueLayout;
    private OnSkuItemSelectListener listener;

    public SkuItemLayout(Context context) {
        super(context);
        init(context);
    }

    public SkuItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SkuItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        attrNameTv = new TextView(context);
        attrNameTv.setId(ViewUtils.generateViewId());
        attrNameTv.setTextColor(ColorUtils.getColor(R.color.black_333333));
        attrNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        attrNameTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        attrNameTv.setIncludeFontPadding(false);


        LayoutParams attributeNameParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        attributeNameParams.leftMargin = ScreenUtils.dp2PxInt(context, 15);
        attributeNameParams.topMargin = ScreenUtils.dp2PxInt(context, 15);
        attrNameTv.setLayoutParams(attributeNameParams);
        addView(attrNameTv);

        attrValueLayout = new FlowLayout(context);
        attrValueLayout.setId(ViewUtils.generateViewId());
        attrValueLayout.setMinimumHeight(ScreenUtils.dp2PxInt(context, 25));

        attrValueLayout.setChildSpacing(ScreenUtils.dp2PxInt(context, 8));
        attrValueLayout.setRowSpacing(ScreenUtils.dp2PxInt(context, 12));

        LayoutParams attributeValueParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        attributeValueParams.leftMargin = ScreenUtils.dp2PxInt(context, 15);
        attributeValueParams.rightMargin = ScreenUtils.dp2PxInt(context, 15);
        attributeValueParams.topMargin = ScreenUtils.dp2PxInt(context, 15);
        attributeValueParams.bottomMargin = ScreenUtils.dp2PxInt(context, 10);
        attrValueLayout.setLayoutParams(attributeValueParams);
        addView(attrValueLayout);

        // 分割线
//        View line = new View(context);
//        line.setBackgroundResource(R.color.comm_line);
//        LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
//        lineParams.leftMargin = ScreenUtils.dp2PxInt(context, 15);
//        lineParams.rightMargin = ScreenUtils.dp2PxInt(context, 15);
//        lineParams.topMargin = ScreenUtils.dp2PxInt(context, 5);
//        line.setLayoutParams(lineParams);
//        addView(line);
    }

    public void setListener(OnSkuItemSelectListener listener) {
        this.listener = listener;
    }

    public void buildItemLayout(int position, String attributeName, List<String> attributeValueList) {
        attrNameTv.setText(attributeName);
        attrValueLayout.removeAllViewsInLayout();
        for (int i = 0; i < attributeValueList.size(); i++) {
        SkuItemView itemView = new SkuItemView(getContext());
            itemView.setId(ViewUtils.generateViewId());
            itemView.setAttributeValue(attributeValueList.get(i));
            itemView.setOnClickListener(new ItemClickListener(position, itemView));
            itemView.setLayoutParams(new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT,
                    SizeUtils.dp2px(28f)));
            attrValueLayout.addView(itemView);
        }
    }

    /**
     * 清空是否可点击，选中状态
     */
    public void clearItemViewStatus() {
        for (int i = 0; i < attrValueLayout.getChildCount(); i++) {
     SkuItemView itemView = (SkuItemView) attrValueLayout.getChildAt(i);
            itemView.setSelected(false);
            itemView.setEnabled(false);
        }
    }

    /**
     * 设置指定属性为可点击状态
     *
     * @param attributeValue
     */
    public void optionItemViewEnableStatus(String attributeValue) {
        for (int i = 0; i < attrValueLayout.getChildCount(); i++) {
          SkuItemView itemView = (SkuItemView) attrValueLayout.getChildAt(i);
            if (attributeValue.equals(itemView.getAttributeValue())) {
                itemView.setEnabled(true);
            }
        }
    }

    /**
     * 设置指定属性为选中状态
     *
     * @param selectValue
     */
    public void optionItemViewSelectStatus(SkuAttribute selectValue) {
        for (int i = 0; i < attrValueLayout.getChildCount(); i++) {
         SkuItemView itemView = (SkuItemView) attrValueLayout.getChildAt(i);
            if (selectValue.getValue().equals(itemView.getAttributeValue())) {
                itemView.setEnabled(true);
                itemView.setSelected(true);
            }
        }
    }

    /**
     * 当前属性集合是否有选中项
     * @return
     */
    public boolean isSelected() {
        for (int i = 0; i < attrValueLayout.getChildCount(); i++) {
        SkuItemView itemView = (SkuItemView) attrValueLayout.getChildAt(i);
            if (itemView.isSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取属性名称
     * @return
     */
    public String getAttributeName() {
        return attrNameTv.getText().toString();
    }

    private void onSkuItemClicked(int position,SkuItemView view) {
        boolean selected = !view.isSelected();
        SkuAttribute attribute = new SkuAttribute();
        attribute.setKey(attrNameTv.getText().toString());
        attribute.setValue(view.getAttributeValue());
        listener.onSelect(position, selected, attribute);
    }

    private class ItemClickListener implements OnClickListener {
        private int position;
        private SkuItemView view;

        ItemClickListener(int position, SkuItemView view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            onSkuItemClicked(position, view);
        }
    }

    interface OnSkuItemSelectListener {
        void onSelect(int position, boolean select, SkuAttribute attribute);
    }
}
