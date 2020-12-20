package com.jcs.where.home.watcher;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * create by zyf on 2020/12/12 7:58 PM
 */
public abstract class EmptyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String input = editable.toString();
        if (input != null && input.equals("")) {
            onEtEmpty();
        }
    }

    protected abstract void onEtEmpty();
}
