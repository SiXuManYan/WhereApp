package com.jcs.where.features.store.search.history

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.store.search.StoreSearchActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.view.MyLayoutManager
import kotlinx.android.synthetic.main.activity_search_history.*

/**
 * Created by Wangsw  2021/6/7 10:28.
 * 搜索历史
 */
class SearchHistoryActivity : BaseActivity() {

    private lateinit var history: ArrayList<String>
    private lateinit var mAdapter: SearchHistoryAdapter

    override fun getLayoutId() = R.layout.activity_search_history

    override fun isStatusDark() = true

    override fun initView() {
        mAdapter = SearchHistoryAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val s = mAdapter.data[position]
                startActivity(StoreSearchActivity::class.java, Bundle().apply {
                    putString(Constant.PARAM_TITLE, s)
                })
            }
        }

        history_rv.apply {
            adapter = mAdapter
            layoutManager = MyLayoutManager()
        }

        search_aet.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val result = search_aet.text.toString().trim()
                    if (result.isNotBlank()) {
                        startActivity(StoreSearchActivity::class.java, Bundle().apply {
                            putString(Constant.PARAM_TITLE, result)
                        })
                        FeaturesUtil.saveHistory(result)
                        mAdapter.addData(result)
                    }
                    return true
                }
                return false
            }

        })

    }

    override fun initData() {
        history = FeaturesUtil.getSearchHistory()
        mAdapter.setNewInstance(history)
    }

    override fun bindListener() {
        clear_history_tv.setOnClickListener {
            mAdapter.setNewInstance(null)
            FeaturesUtil.clearSearchHistory()

        }
    }
}