package com.jcs.where.base;

import android.os.Bundle;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.jcs.where.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 需要占全屏的Fragment
 * create by zyf on 2020/12/20 11:24 AM
 */
public abstract class BaseFullFragment extends BaseFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setColor(getActivity(), ContextCompat.getColor(getContext(), R.color.colorPrimary), 0);
        fullScreen(getActivity());
        super.onViewCreated(view, savedInstanceState);
    }
}
