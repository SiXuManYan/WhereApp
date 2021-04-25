package com.jcs.where.features.gourmet.takeaway

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressResponse

/**
 * Created by Wangsw  2021/4/25 14:33.
 *
 */
class TakeawayAdapter: BaseQuickAdapter<AddressResponse, BaseViewHolder>(R.layout.item_dishes) {
    override fun convert(holder: BaseViewHolder, item: AddressResponse) {

    }
}