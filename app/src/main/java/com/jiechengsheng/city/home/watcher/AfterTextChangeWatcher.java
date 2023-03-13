package com.jiechengsheng.city.home.watcher;

import android.text.Editable;
import android.text.TextWatcher;


public abstract class AfterTextChangeWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        onAfterTextChanged(editable);
    }

    protected abstract void onAfterTextChanged(Editable editable);

}
