package com.jiechengsheng.city.features.address.edit

import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.address.AddressRequest


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
    fun handleSave(
        address: String?,
        recipient: String?,
        phone: String?,
        sex: Int,
        isChange: Boolean,
        mAddressId: String?,
        mAreaId: Int?,
    ) {
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
        if (mAreaId == 0) {
            ToastUtils.showShort(R.string.address_city_edit)
            return
        }

        val body = AddressRequest()
        body.address = address
        body.contact_name = recipient
        body.contact_number = phone
        body.sex = sex
        body.city_id = mAreaId
        if (isChange) {
            requestApi(mRetrofit.editAddress(mAddressId, body), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.editSuccess()
                }
            })
        } else {
            requestApi(mRetrofit.addAddress(body), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
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