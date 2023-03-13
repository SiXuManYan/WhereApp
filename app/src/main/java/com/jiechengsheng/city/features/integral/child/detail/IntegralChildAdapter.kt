package com.jiechengsheng.city.features.integral.child.detail

import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.IntegralDetailResponse

/**
 * Created by Wangsw  2021/1/27 15:06.
 * 积分明细列表
 */
class IntegralChildAdapter : BaseQuickAdapter<IntegralDetailResponse, BaseViewHolder>(R.layout.item_integral_child), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: IntegralDetailResponse) {

        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val time_tv = holder.getView<TextView>(R.id.time_tv)
        val score_tv = holder.getView<TextView>(R.id.score_tv)

        time_tv.text = item.created_at


        val type = item.type
        if (type==6 || type == 7) {
            score_tv.text = StringUtils.getString(R.string.cut_format, item.integral)
        }else {
            score_tv.text = StringUtils.getString(R.string.add_format, item.integral)
        }

        /** 类型（1：注册，2：邀请，3：消费，4：评论，5：签到 6兑换优惠券 7 兑换商品） */
        val title :String = when (type) {
            1 -> StringUtils.getString(R.string.type_register)
            2 -> StringUtils.getString(R.string.type_invite)
            3 -> StringUtils.getString(R.string.type_consumption)
            4 -> StringUtils.getString(R.string.type_comment)
            5 -> StringUtils.getString(R.string.type_sign_in)
            6 -> StringUtils.getString(R.string.type_coupon)
            7 -> StringUtils.getString(R.string.type_goods)
            else -> ""
        }

        title_tv.text = title

    }
}