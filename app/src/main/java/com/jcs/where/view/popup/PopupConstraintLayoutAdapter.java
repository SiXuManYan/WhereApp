package com.jcs.where.view.popup;

/**
 * create by zyf on 2020/12/25 8:38 PM
 */
public abstract class PopupConstraintLayoutAdapter {
    public abstract int getMaxHeight();

    public int getMinHeight() {
        return 1;
    }

    public boolean showShadow() {
        return true;
    }

    public boolean clickOutSideClose() {
        return true;
    }

    public boolean isGoneAfterBottom() {
        return true;
    }

    public long getDuration() {
        return 500;
    }

    public void onHideCompleted() {
    }

    public void onShowCompleted() {
    }

    public boolean enableAnim() {
        return true;
    }

}
