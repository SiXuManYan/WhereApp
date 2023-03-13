package com.jiechengsheng.city.features.job.form.city

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.CityPickerResponse

/**
 * Created by Wangsw  2022/9/29 16:42.
 *
 */
class CvCityAdapter:BaseQuickAdapter<CityPickerResponse.CityChild, BaseViewHolder>(R.layout.item_city_picker)  {
    override fun convert(holder: BaseViewHolder, item: CityPickerResponse.CityChild) {
        val city_tv = holder.getView<CheckedTextView>(R.id.city_tv)
        city_tv .text = item.name

        city_tv.isChecked = item.nativeIsSelected


    }
}