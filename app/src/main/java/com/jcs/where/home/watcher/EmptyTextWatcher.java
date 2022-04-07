package com.jcs.where.home.watcher;

import android.text.Editable;
import android.text.TextUtils;
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
        String input = editable.toString().trim();
        if (TextUtils.isEmpty(input)) {
            onEtEmpty();
        }else {
            onEtNotEmpty();
        }
    }

    protected abstract void onEtEmpty();
    protected void onEtNotEmpty(){

    }

}
