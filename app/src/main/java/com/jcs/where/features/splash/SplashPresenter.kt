package com.jcs.where.features.splash

import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.JsonSyntaxException
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.CategoryResponse
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.JsonUtil
import com.jcs.where.utils.SPKey
import java.util.*

/**
 * Created by Wangsw  2021/3/18 16:14.
 */
class SplashPresenter(private val view: SplashView) : BaseMvpPresenter(view) {

    val yellowPageAllCategories: Unit
        get() {
            val categoryIds: MutableList<Int> = ArrayList()
            categoryIds.add(10)
            categoryIds.add(17)
            categoryIds.add(21)
            categoryIds.add(27)
            categoryIds.add(94)
            categoryIds.add(114)
            categoryIds.add(209)
            categoryIds.add(226)


            requestApi(mRetrofit.getAllChildCategories(1, categoryIds.toString()),
                object : BaseMvpObserver<List<CategoryResponse>>(view,false) {
                    override fun onSuccess(response: List<CategoryResponse>) {
                        if (CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_YELLOW_PAGE_CATEGORIES) == "" && response.size > 0) {
                            CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_YELLOW_PAGE_CATEGORIES, response)
                        }
                    }
                })
        }


    /**
     * 获取剪切板中的邀请码
     */
    fun handleClipboard() {

      //  邀请链接，H5剪切板中数据格式（json字符串）：{"whereCode": "code"}
        val inviteJson = ClipboardUtils.getText().toString()
        if (inviteJson.isBlank()) {
            return
        }

        try {
            val data = JsonUtil.getInstance().fromJson(inviteJson, WhereInvite::class.java)
            val whereCode = data.whereCode
            if (whereCode.isNullOrBlank()) {
                return
            }
            SPUtils.getInstance().put(SPKey.K_INVITE_CODE , whereCode)
        } catch (e: JsonSyntaxException) {

        }
        ClipboardUtils.copyText("")
    }
}

class WhereInvite {
    var whereCode: String? = null
}