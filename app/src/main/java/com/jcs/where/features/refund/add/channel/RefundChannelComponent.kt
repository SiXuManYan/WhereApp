package com.jcs.where.features.refund.add.channel

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.RefundChannel

/**
 * Created by Wangsw  2022/4/25 19:03.
 *
 */
interface RefundChannelView : BaseMvpView {
    fun bindChanel(data: ArrayList<RefundChannel>)

}

class RefundChannelPresenter(private var view: RefundChannelView) : BaseMvpPresenter(view) {
    fun getChannel() {

        requestApi(mRetrofit.refundChannel, object : BaseMvpObserver<ArrayList<RefundChannel>>(view) {
            override fun onSuccess(response: ArrayList<RefundChannel>) {
                val addTitle = addTitle(response)
                view.bindChanel(addTitle)
            }

        })
    }

    private fun addTitle(response: ArrayList<RefundChannel>) :ArrayList<RefundChannel>{

        val finalData  = ArrayList<RefundChannel>()

        val groupBy = response.groupBy { it.channel_category  }

        groupBy.forEach { group->
            finalData.addAll(group.value)
        }

        // add title
        groupBy.forEach { group->

            val titleEntity = RefundChannel().apply {
                channel_category = group.key
                this.type = RefundChannel.TYPE_TITLE
            }

            val indexOfFirst = finalData.indexOfFirst {
                it.channel_category == group.key
            }
            finalData.add(indexOfFirst, titleEntity)
        }
        return finalData
    }

}