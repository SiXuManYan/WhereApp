package com.jcs.where.features.message.notice;

import android.view.View;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpFragment;

/**
 * Created by Wangsw  2021/2/20 15:14.
 * 系统通知
 */
public class SystemNoticeFragment extends BaseMvpFragment<SystemNoticePresenter> implements SystemNoticeView {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_list;
    }


    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {
        presenter = new SystemNoticePresenter(this);
    }

    @Override
    protected void bindListener() {

    }

}
