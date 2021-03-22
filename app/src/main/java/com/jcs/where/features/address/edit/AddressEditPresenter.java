package com.jcs.where.features.address.edit;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/3/22 14:16.
 */
public class AddressEditPresenter extends BaseMvpPresenter {

    private AddressEditView view;

    public AddressEditPresenter(AddressEditView view) {
        super(view);
        this.view = view;
    }
}
