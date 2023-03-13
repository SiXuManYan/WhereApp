package com.jiechengsheng.city.features.mall.home

import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.mall.MallCategory

/**
 * Created by Wangsw  2021/11/30 16:22.
 *
 */
interface MallHomeView : BaseMvpView {
    fun bindCategory(response: ArrayList<MallCategory>, titles: ArrayList<String>)

}


class MallHomePresenter(private var view: MallHomeView) : BaseMvpPresenter(view) {


    fun getFirstCategory(needAll: Boolean? = false) {

        requestApi(mRetrofit.mallFirstSecondCategory, object : BaseMvpObserver<ArrayList<MallCategory>>(view) {
            override fun onSuccess(response: ArrayList<MallCategory>) {
                needAll?.let {
                    if (it) {
                        response.add(0,
                            MallCategory().apply {
                                id = 0
                                name = StringUtils.getString(R.string.all)
                                icon = ""
                            }
                        )
                    }
                }

                val titles: ArrayList<String> = ArrayList()
                response.forEach {
                    titles.add(it.name)
                }
                view.bindCategory(response, titles)
            }
        })


    }


}