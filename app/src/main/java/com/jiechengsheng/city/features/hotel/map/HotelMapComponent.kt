package com.jiechengsheng.city.features.hotel.map

import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.api.response.hotel.HotelHomeRecommend
import com.jiechengsheng.city.utils.SPKey
import java.util.*

/**
 * Created by Wangsw  2021/9/27 14:07.
 *
 */

interface HotelMapView : BaseMvpView, OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {

    /**
     * tab 分类
     */
    fun bindCategory(response: ArrayList<Category>)

    /**
     * maker 数据
     */
    fun bindMakerList(response: MutableList<HotelHomeRecommend>)
}

class HotelMapPresenter(private var view: HotelMapView) : BaseMvpPresenter(view) {


    /**
     * tab 分类
     */
    fun getHotelChildCategory(categoryId: Int) {

        requestApi(mRetrofit.getCategoriesList(3, categoryId.toString(), 2), object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {

                // 当前二级分类全部
                val all = Category().apply {
                    name = StringUtils.getString(R.string.all)
                    id = 0
                    has_children = 1
                    nativeIsSelected = true
                }
                response.add(0, all)

                view.bindCategory(response)
            }

        })
    }


    /**
     * 获取酒店地图所有 Maker 信息
     */
    fun getMakerData(
        search_input: String? = null,
        star_level: String? = null,
        price_range: String? = null,
        hotel_type_ids: Int? = null
    ) {

        val instance = SPUtils.getInstance()
        val areaId = instance.getString(SPKey.SELECT_AREA_ID, "0")

        requestApi(mRetrofit.getHotelMapMaker(areaId, null, null, search_input, star_level, price_range,hotel_type_ids),
            object : BaseMvpObserver<ArrayList<HotelHomeRecommend>>(view) {
                override fun onSuccess(response: ArrayList<HotelHomeRecommend>) {
                    response.forEach {
                        it.contentType = HotelHomeRecommend.CONTENT_TYPE_CARD
                    }

                    view.bindMakerList(response.toMutableList())
                }
            })
    }


}