package com.jcs.where.features.address;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.address.AddressResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/22 10:32.
 */
public class AddressPresenter extends BaseMvpPresenter {

    private AddressView view;

    public AddressPresenter(AddressView view) {
        super(view);
        this.view = view;
    }

    public void getList() {
        requestApi(mRetrofit.addressList(), new BaseMvpObserver<List<AddressResponse>>(view) {
            @Override
            protected void onSuccess(List<AddressResponse> response) {

                    view.bindList(response);
            }
        });

    }
}
