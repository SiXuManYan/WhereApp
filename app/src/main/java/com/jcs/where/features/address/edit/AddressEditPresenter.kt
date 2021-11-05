package com.jcs.where.features.address.edit;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonElement;
import com.google.gson.internal.$Gson$Preconditions;
import com.jcs.where.R;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.address.AddressRequest;

/**
 * Created by Wangsw  2021/3/22 14:16.
 */
public class AddressEditPresenter extends BaseMvpPresenter {

    private AddressEditView view;

    public AddressEditPresenter(AddressEditView view) {
        super(view);
        this.view = view;
    }

    /**
     * 修改和添加地址
     */
    public void handleSave(String address, String recipient, String phone, int sex, boolean isChange, @Nullable String mAddressId) {


        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShort(R.string.address_edit_hint);
            return;
        }
        if (TextUtils.isEmpty(recipient)) {
            ToastUtils.showShort(R.string.recipient_edit_hint);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(R.string.address_phone_edit);
            return;
        }


        AddressRequest body = new AddressRequest();
        body.address = address;
        body.contact_name = recipient;
        body.contact_number = phone;
        body.sex = sex;

        if (isChange) {

            requestApi(mRetrofit.editAddress(mAddressId, body), new BaseMvpObserver<JsonElement>(view) {
                @Override
                protected void onSuccess(JsonElement response) {
                    view.editSuccess();
                }
            });


        } else {
            requestApi(mRetrofit.addAddress(body), new BaseMvpObserver<JsonElement>(view) {
                @Override
                protected void onSuccess(JsonElement response) {
                    view.addAddressSuccess();
                }
            });

        }


    }

    /**
     * 删除地址
     */
    public void deleteAddress(String mAddressId) {
        requestApi(mRetrofit.deleteAddress(mAddressId), new BaseMvpObserver<JsonElement>(view) {
            @Override
            protected void onSuccess(JsonElement response) {
                view.deleteAddressSuccess();
            }
        });
    }
}
