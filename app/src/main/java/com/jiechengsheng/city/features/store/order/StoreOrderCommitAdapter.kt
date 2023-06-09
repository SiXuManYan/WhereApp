package com.jiechengsheng.city.features.store.order

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.store.StoreOrderCommitData
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/6/22 11:53.
 * 商城提交订单
 */
class StoreOrderCommitAdapter : BaseQuickAdapter<StoreOrderCommitData, BaseViewHolder>(R.layout.item_order_commit) {


    override fun convert(holder: BaseViewHolder, item: StoreOrderCommitData) {

        val shop_name_tv = holder.getView<TextView>(R.id.shop_name_tv)
        val child_container_ll = holder.getView<LinearLayout>(R.id.child_container_ll)


        val delivery_rl = holder.getView<RelativeLayout>(R.id.delivery_rl)
        val remark_rl = holder.getView<RelativeLayout>(R.id.remark_rl)
        val delivery_price_tv = holder.getView<TextView>(R.id.delivery_price_tv)
        val remark_aet = holder.getView<AppCompatEditText>(R.id.remark_aet)

        shop_name_tv.text = item.shop_title
        when (item.delivery_type) {
            1 -> {
                delivery_rl.visibility = View.GONE
                remark_rl.visibility = View.GONE
            }
            2 -> {
                delivery_rl.visibility = View.VISIBLE
                remark_rl.visibility = View.VISIBLE
                delivery_price_tv.text = StringUtils.getString(R.string.price_unit_format, item.delivery_fee.toString())
            }

        }

        child_container_ll.removeAllViews()
        item.goods.forEach {

            val child = LayoutInflater.from(context).inflate(R.layout.item_dishes_for_order_submit, null)

            val image_iv = child.findViewById<ImageView>(R.id.order_image_iv)
            val good_name_tv = child.findViewById<TextView>(R.id.good_name_tv)
            val good_count_tv = child.findViewById<TextView>(R.id.count_tv)
            val price_tv = child.findViewById<TextView>(R.id.now_price_tv)

            GlideUtil.load(context, it.image, image_iv, 4)
            good_name_tv.text = it.goodName
            good_count_tv.text = StringUtils.getString(R.string.count_format, it.good_num)

            price_tv.text = StringUtils.getString(R.string.price_unit_format, it.price.toPlainString())

            child_container_ll.addView(child)
            val layoutParams = child.layoutParams as LinearLayout.LayoutParams
            layoutParams.bottomMargin = SizeUtils.dp2px(10f)
            child.layoutParams = layoutParams
        }


        remark_aet.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit


            override fun afterTextChanged(s: Editable?) {
                val text = remark_aet.text.toString()
                item.remark = text
            }

        })

    }
}