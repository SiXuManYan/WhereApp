package com.jcs.where.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * create by zyf on 2021/1/7 5:44 下午
 */
public abstract class EmptyWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
