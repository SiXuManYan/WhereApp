package com.jcs.where.features.payment.counter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.pay.PayCounter

/**
 * Created by Wangsw  2023/3/7 16:43.
 *
 */
class PayCounterAdapter :BaseQuickAdapter<PayCounter, BaseViewHolder>(R.layout.item_pay_counter) {


    override fun convert(holder: BaseViewHolder, item: PayCounter) {

    }
}