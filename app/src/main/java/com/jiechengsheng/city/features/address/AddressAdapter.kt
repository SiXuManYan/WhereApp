package com.jiechengsheng.city.features.address

import com.chad.library.adapter.base.BaseQuickAdapter
import com.jiechengsheng.city.api.response.address.AddressResponse
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import android.widget.TextView

/**
 * Created by Wangsw  2021/11/4 17:22.
 *
 *
 */
class AddressAdapter : BaseQuickAdapter<AddressResponse, BaseViewHolder>(R.layout.item_address) {

    override fun convert(holder: BaseViewHolder, data: AddressResponse) {
        val addressTv = holder.getView<TextView>(R.id.address_name_tv)
        val nameTv = holder.getView<TextView>(R.id.name_tv)
        val  phoneTv = holder.getView<TextView>(R.id.phone_tv)
        var  editIv = holder.getView<TextView>(R.id.edit_iv)
        addressTv.text = data.address
        nameTv.text = data.contact_name
        phoneTv.text = data.contact_number
    }
}