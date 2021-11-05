package com.jcs.where.features.address.edit

import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressRequest
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpView


/**
 * Created by Wangsw  2021/3/22 14:16.
 */
interface AddressEditView : BaseMvpView {
    /**
     * 修改成功
     */
    fun editSuccess()

    /**
     * 地址添加成功
     */
    fun addAddressSuccess()

    /**
     * 地址删除成功
     */
    fun deleteAddressSuccess()
}

/**
 * Created by Wangsw  2021/3/22 14:16.
 */
class AddressEditPresenter(private val view: AddressEditView) : BaseMvpPresenter(view) {
    /**
     * 修改和添加地址
     */
    fun handleSave(address: String?, recipient: String?, phone: String?, sex: Int, isChange: Boolean, mAddressId: String?) {
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShort(R.string.address_edit_hint)
            return
        }
        if (TextUtils.isEmpty(recipient)) {
            ToastUtils.showShort(R.string.recipient_edit_hint)
            return
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(R.string.address_phone_edit)
            return
        }
        val body = AddressRequest()
        body.address = address
        body.contact_name = recipient
        body.contact_number = phone
        body.sex = sex
        if (isChange) {
            requestApi(mRetrofit.editAddress(mAddressId, body), object : BaseMvpObserver<JsonElement>(view) {
                protected override fun onSuccess(response: JsonElement) {
                    view.editSuccess()
                }
            })
        } else {
            requestApi(mRetrofit.addAddress(body), object : BaseMvpObserver<JsonElement>(view) {
                protected override fun onSuccess(response: JsonElement) {
                    view.addAddressSuccess()
                }
            })
        }
    }

    /**
     * 删除地址
     */
    fun deleteAddress(mAddressId: String?) {
        requestApi(mRetrofit.deleteAddress(mAddressId), object : BaseMvpObserver<JsonElement>(view) {
            protected override fun onSuccess(response: JsonElement) {
                view.deleteAddressSuccess()
            }
        })
    }
}