package com.jcs.where.features.merchant.record

import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.merchant.MerchantApplyRecord

/**
 * Created by Wangsw  2022/1/11 15:27.
 *
 */
class MerchantRecordAdapter : BaseQuickAdapter<MerchantApplyRecord, BaseViewHolder>(R.layout.item_merchant_record), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: MerchantApplyRecord) {
        holder.setText(R.id.name_tv, item.mer_name)
        holder.setText(R.id.time_tv, StringUtils.getString(R.string.application_time_format, item.created_at))
        // 审核状态 1：待审核，2：审核通过，3：审核未通过
        val status_tv = holder.getView<TextView>(R.id.status_tv)

        status_tv.text = when (item.is_verify) {
            1 -> {
                StringUtils.getString(R.string.wait_apply)
            }
            2 -> {
                StringUtils.getString(R.string.examination_passed)
            }
            3 -> {
                StringUtils.getString(R.string.examination_passed_not)
            }

            else -> {
                ""
            }
        }
    }
}