package com.jcs.where.features.mall.buy

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.GlideUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/15 14:09.
 * 商城提交订单
 */
class MallOrderCommitAdapter : BaseQuickAdapter<MallCartGroup, BaseViewHolder>(R.layout.item_order_commit_mall) {


    override fun convert(holder: BaseViewHolder, item: MallCartGroup) {
        val shop_name_tv = holder.getView<TextView>(R.id.shop_name_tv)
        val child_container_ll = holder.getView<LinearLayout>(R.id.child_container_ll)

        val delivery_price_tv = holder.getView<TextView>(R.id.delivery_price_tv)
        val remark_aet = holder.getView<AppCompatEditText>(R.id.remark_aet)

        shop_name_tv.text = item.title


        child_container_ll.removeAllViews()

        var totalItem :BigDecimal = BigDecimal.ZERO

        item.gwc.forEach {

            totalItem  = BigDecimalUtil.add(it.delivery_fee,totalItem)

            val child = LayoutInflater.from(context).inflate(R.layout.item_dishes_for_order_submit_mall, null)
            val image_iv = child.findViewById<ImageView>(R.id.order_image_iv)
            val good_name_tv = child.findViewById<TextView>(R.id.good_name_tv)
            val good_count_tv = child.findViewById<TextView>(R.id.count_tv)
            val attr_tv = child.findViewById<TextView>(R.id.attr_tv)
            val price_tv = child.findViewById<TextView>(R.id.now_price_tv)


            it.goods_info?.let { mgi ->
                GlideUtil.load(context, mgi.photo, image_iv, 4)
                good_name_tv.text = mgi.title
            }

            good_count_tv.text = StringUtils.getString(R.string.count_format, it.good_num)


            it.specs_info?.let { msi ->
                price_tv.text = StringUtils.getString(R.string.price_unit_format, msi.price.toPlainString())

                val attr = StringBuffer()
                msi.specs.forEach { spec->
                    attr.append(spec.value+" ")
                }
                attr_tv.text = attr

            }

            child_container_ll.addView(child)
            val layoutParams = child.layoutParams as LinearLayout.LayoutParams
            layoutParams.bottomMargin = SizeUtils.dp2px(10f)
            child.layoutParams = layoutParams

        }
        delivery_price_tv.text = StringUtils.getString(R.string.price_unit_format, totalItem.toPlainString())

        remark_aet.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit


            override fun afterTextChanged(s: Editable?) {
                val text = remark_aet.text.toString()
                item.nativeRemark = text
            }

        })
    }
}