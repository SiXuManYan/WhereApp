package com.jcs.where.features.search

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.search.result.SearchAllResultActivity
import com.jcs.where.features.store.history.SearchHistoryAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.MyLayoutManager
import kotlinx.android.synthetic.main.activity_search_all.*

/**
 * Created by Wangsw  2021/2/25 10:25.
 * 搜索
 */
class SearchAllActivity : BaseMvpActivity<SearchAllPresenter>(), SearchAllView {

    private lateinit var mAdapter: SearchHistoryAdapter

    override fun getLayoutId() = R.layout.activity_search_all

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        mAdapter = SearchHistoryAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val s = mAdapter.data[position]
                handleSearch(s)
            }
        }
        history_rv.apply {
            adapter = mAdapter
            layoutManager = MyLayoutManager()
        }

    }

    override fun initData() {
        presenter = SearchAllPresenter(this)
        val searchHistory = presenter.searchHistory
        mAdapter.setNewInstance(searchHistory)

    }

    override fun bindListener() {
        cancel_tv.setOnClickListener { finish() }

        search_aet.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val finalInput = search_aet.text.toString().trim()
                    handleSearch(finalInput)

                    if (finalInput.isNotBlank()) {
                        presenter.saveSearchHistory(finalInput)
                        mAdapter.addData(finalInput)
                    }
                    return true
                }
                return false
            }

        })

        delete_iv.setOnClickListener {
            mAdapter.setNewInstance(null)
        }
    }

    private fun handleSearch(finalInput: String) {
        if (TextUtils.isEmpty(finalInput)) {
            return
        }
        val bundle = Bundle()
        bundle.putString(Constant.PARAM_NAME, finalInput)
        startActivity(SearchAllResultActivity::class.java,bundle)
    }
}