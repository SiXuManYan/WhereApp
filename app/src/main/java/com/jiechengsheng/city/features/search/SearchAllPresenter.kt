package com.jiechengsheng.city.features.search

import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.Constant
import java.util.*

/**
 * Created by Wangsw  2021/2/25 10:26.
 */
class SearchAllPresenter(private val view: SearchAllView) : BaseMvpPresenter(view) {

    /**
     * 保存搜索记录
     */
    fun saveSearchHistory(finalInput: String) {
        val shareDefault = CacheUtil.getShareDefault()
        val key = Constant.SP_SEARCH_HISTORY
        val history = shareDefault.getString(key, "")
        if (history.isEmpty()) {
            shareDefault.put(key, finalInput)
            return
        }
        shareDefault.put(key, "$history,$finalInput")
    }

    /**
     * 获取搜索历史记录
     *
     * @return
     */
    val searchHistory: ArrayList<String>
        get() {
            val historyList = ArrayList<String>()
            val shareDefault = CacheUtil.getShareDefault()
            val key = Constant.SP_SEARCH_HISTORY
            val history = shareDefault.getString(key, "")
            if (history.contains(",")) {
                val split = history.split(",").toTypedArray()
                historyList.addAll(Arrays.asList(*split))
            } else if (history.isNotEmpty()) {
                historyList.add(history)
            }

            return historyList
        }

    /**
     * 清空搜索记录
     */
    fun clearSearchHistory() {
        CacheUtil.getShareDefault().put(Constant.SP_SEARCH_HISTORY, "")
        ToastUtils.showShort(R.string.clear_success)
    }
}