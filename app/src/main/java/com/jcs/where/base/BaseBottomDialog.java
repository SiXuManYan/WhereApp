package com.jcs.where.base;

import android.view.View;

/**
 * 底部dialog
 * create by zyf on 2021/1/2 7:59 PM
 */
public abstract class BaseBottomDialog extends BaseDialog{
    @Override
    protected boolean isBottom() {
        return true;
    }
}
