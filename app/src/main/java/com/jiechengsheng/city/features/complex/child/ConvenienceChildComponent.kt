package com.jiechengsheng.city.features.complex.child

import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.CategoryResponse
import com.jiechengsheng.city.api.response.MechanismResponse
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.SPKey

/**
 * Created by Wangsw  2022/9/8 18:31.
 *
 */
interface ConvenienceChildView : BaseMvpView {
    fun bindChildCategory(response: List<CategoryResponse>)
    fun bindList(data: MutableList<MechanismResponse>, lastPage: Boolean)

}

class ConvenienceChildPresenter(private var view: ConvenienceChildView) : BaseMvpPresenter(view) {

    fun getChildCategory(mCurrentCategoryId: String) {
        requestApi(mRetrofit.getAllChildCategories(3, mCurrentCategoryId), object : BaseMvpObserver<List<CategoryResponse>>(view) {
            override fun onSuccess(response: List<CategoryResponse>) {
                view.bindChildCategory(response)
            }

        })
    }


    fun getData(page: Int, categoryId: String,recommend :Int?) {

        val latLng = CacheUtil.getSafeSelectLatLng()
        var areaId = SPUtils.getInstance().getString(SPKey.SELECT_AREA_ID, "")

        val lat = latLng.latitude
        val lng = latLng.longitude

        if (TextUtils.isEmpty(areaId)) {
            areaId = null
        } else {
//            lat = null;
//            lng = null;
        }


        requestApi(mRetrofit.getMechanismListById3(page,categoryId,"",lat,lng,areaId ,recommend),object :BaseMvpObserver<PageResponse<MechanismResponse>>(view){
            override fun onSuccess(response: PageResponse<MechanismResponse>) {
                val data = response.data
                val isLastPage = response.lastPage == page
                view.bindList(data.toMutableList(),isLastPage)


            }

        })
    }


}