package com.jiechengsheng.city.government.bean;

import com.google.android.gms.maps.model.BitmapDescriptor;

/**
 * 存储marker不同状态展示的BitmapDescriptor
 * create by zyf on 2020/12/30 11:21 PM
 */
public class MarkerBitmapDescriptors {
    private BitmapDescriptor selectedBitmapDescriptor;
    private BitmapDescriptor unselectedBitmapDescriptor;
    private boolean isSelected = false;

    public BitmapDescriptor getSelectedBitmapDescriptor() {
        return selectedBitmapDescriptor;
    }

    public void setSelectedBitmapDescriptor(BitmapDescriptor selectedBitmapDescriptor) {
        this.selectedBitmapDescriptor = selectedBitmapDescriptor;
    }

    public BitmapDescriptor getUnselectedBitmapDescriptor() {
        return unselectedBitmapDescriptor;
    }

    public void setUnselectedBitmapDescriptor(BitmapDescriptor unselectedBitmapDescriptor) {
        this.unselectedBitmapDescriptor = unselectedBitmapDescriptor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
