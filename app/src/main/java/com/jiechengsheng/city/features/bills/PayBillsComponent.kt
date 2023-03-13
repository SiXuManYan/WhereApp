package com.jiechengsheng.city.features.bills

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.BannerResponse

/**
 * Created by Wangsw  2022/9/8 16:30.
 *
 */
interface PayBillsView : BaseMvpView {
    fun bindTopBannerData(bannerUrls: ArrayList<String>, response: ArrayList<BannerResponse>)

}

class PayBillsPresenter(val view: PayBillsView) : BaseMvpPresenter(view) {
    /**
     * 获取顶部轮播图
     */
    fun getTopBanner() {
        requestApi(mRetrofit.getBanners(7), object : BaseMvpObserver<ArrayList<BannerResponse>>(view, false) {
            override fun onSuccess(response: ArrayList<BannerResponse>?) {
                if (response == null || response.isEmpty()) {
                    return
                }

                val bannerUrls: ArrayList<String> = ArrayList()
                response.forEach {
                    bannerUrls.add(it.src)
                }
                view.bindTopBannerData(bannerUrls, response)
            }
        })
    }
}
