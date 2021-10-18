package com.jcs.where.features.travel.map

import com.blankj.utilcode.util.StringUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.travel.TravelChild
import com.jcs.where.utils.CacheUtil
import java.util.ArrayList

/**
 * Created by Wangsw  2021/10/18 9:38.
 *
 */
interface TravelMapView : BaseMvpView , OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener{
    /**
     * 分类
     */
    fun bindSecondCategory(response: ArrayList<Category>)
    fun bindMakerList(response: MutableList<TravelChild>)
}


class TravelMapPresenter(private var view: TravelMapView) : BaseMvpPresenter(view) {


    /**
     * 获取二级分类
     */
    fun getGovernmentChildCategory(firstCategoryId:Int) {
        requestApi(mRetrofit.getCategoriesList(3, firstCategoryId.toString(), 2), object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {

                // 当前二级分类全部
                val secondAll = Category().apply {
                    name = StringUtils.getString(R.string.all)
                    id = 0
                    has_children = 1
                    nativeIsSelected = true
                }
                response.add(0, secondAll)

                response.forEach {

                    // 三级分类手动添加全部
                    if (it.has_children == 2 && it.child_categories.isNotEmpty()) {
                        val thirdAll = Category().apply {
                            name = StringUtils.getString(R.string.all)
                            id = it.id
                            has_children = 1
                            nativeIsSelected = true
                        }
                        it.child_categories.add(0, thirdAll)
                    }

                }
                view.bindSecondCategory(response)
            }

        })


    }

    fun getMakerData(categoryId: Int, searchInput: String? =null) {

        val safeSelectLatLng = CacheUtil.getSafeSelectLatLng()

        requestApi(mRetrofit.getTravelMarker(
            categoryId,
            searchInput,
            safeSelectLatLng.latitude,
            safeSelectLatLng.longitude
        ), object : BaseMvpObserver<ArrayList<TravelChild>>(view) {
            override fun onSuccess(response: ArrayList<TravelChild>) {
                view.bindMakerList(response.toMutableList())
            }
        })

    }
}