package com.jcs.where.category;

import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.jcs.where.R;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.utils.StatusBarUtils;
import com.jcs.where.widget.JcsTitle;


public class CategoryFragment extends BaseFragment {

    private JcsTitle mJcsTitle;

    @Override
    protected void initView(View view) {
        mJcsTitle = view.findViewById(R.id.jcsTitle);
        setMargins(mJcsTitle, 0, getStatusBarHeight(), 0, 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }
}
