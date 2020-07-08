package com.jcs.where.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.jcs.where.R;
import com.jcs.where.login.LoginActivity;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.utils.V;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView settingIv;

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        return view;
    }

    private void initView() {
        settingIv = V.f(view, R.id.iv_setting);
        settingIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                LoginActivity.goTo(getContext());
                break;
            default:
        }
    }
}
