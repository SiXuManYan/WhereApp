package com.jcs.where.government.activity;

import android.view.View;
import android.widget.ImageView;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.view.popup.PopupConstraintLayout;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;

/**
 * 政府机构地图页
 * 政府机构列表是本页的fragment
 * create by zyf on 2020/12/28 7:44 PM
 */
public class GovernmentMapActivity extends BaseActivity {
    private PopupConstraintLayout mBottomPopupLayout;
    private ImageView mTopArrowIv;


    @Override
    protected void initView() {
        mBottomPopupLayout = findViewById(R.id.bottomPopupLayout);
        mBottomPopupLayout.setAdapter(new PopupConstraintLayoutAdapter() {
            @Override
            public boolean isGoneAfterBottom() {
                return false;
            }

            @Override
            public int getMinHeight() {
                return getPxFromDp(55);
            }

            @Override
            public int getMaxHeight() {
                return getPxFromDp(583);
            }
        });

        mTopArrowIv = findViewById(R.id.topArrowIv);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {
        mTopArrowIv.setOnClickListener(this::onTopArrowClick);
    }

    private void onTopArrowClick(View view) {
        mBottomPopupLayout.showOrHide();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_government_map;
    }
}
