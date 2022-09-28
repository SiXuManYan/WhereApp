package com.jcs.where.features.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.result.JobSearchResultActivity
import com.jcs.where.features.mall.result.MallSearchResultActivity
import com.jcs.where.features.search.result.SearchAllResultActivity
import com.jcs.where.features.search.yellow.YellowPageSearchResultActivity
import com.jcs.where.features.store.history.SearchHistoryAdapter
import com.jcs.where.features.store.search.StoreSearchActivity
import com.jcs.where.government.activity.ConvenienceServiceSearchActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.MyLayoutManager
import kotlinx.android.synthetic.main.activity_search_all.*

/**
 * Created by Wangsw  2021/2/25 10:25.
 * 搜索
 */
class SearchAllActivity : BaseMvpActivity<SearchAllPresenter>(), SearchAllView {

    /**
     * 0 全部搜索
     * 1 企业黄页
     * 2 综合服务
     * 3.政府地图
     * 4.酒店地图
     * 5.商城
     * 6.新版商城
     * 7.美食地图
     * 8.新版商城通过店铺搜索商品
     * 9.招聘
     */
    private var type = 0

    /**
     * 分类id
     */
    private var categoryId: String? = ""

    /** 商城店铺id */
    private var shopId = 0


    private var hideHistory = false

    private lateinit var mAdapter: SearchHistoryAdapter

    override fun getLayoutId() = R.layout.activity_search_all

    override fun isStatusDark() = true

    override fun initView() {

        type = intent.getIntExtra(Constant.PARAM_TYPE, 0)
        categoryId = intent.getStringExtra(Constant.PARAM_CATEGORY_ID)
        shopId = intent.getIntExtra(Constant.PARAM_SHOP_ID, 0)
        hideHistory = intent.getBooleanExtra(Constant.PARAM_HIDE, false)

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

        if (hideHistory) {
            history_rl.visibility = View.GONE
            history_rv.visibility = View.GONE
        }

        KeyboardUtils.showSoftInput(search_aet)

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

            val create = AlertDialog.Builder(this)
                .setTitle(R.string.hint)
                .setCancelable(false)
                .setMessage(getString(R.string.clear_history_hint))
                .setPositiveButton(R.string.confirm) { dialog, _ ->
                    mAdapter.setNewInstance(null)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.confirm) { dialog, _ ->
                    dialog.dismiss()
                }

                .create()
            create

                .show()


        }
    }

    private fun handleSearch(finalInput: String) {
        if (TextUtils.isEmpty(finalInput)) {
            return
        }
        val bundle = Bundle()
        bundle.putString(Constant.PARAM_NAME, finalInput)

        when (type) {
            0 -> {
                startActivity(SearchAllResultActivity::class.java, bundle)
            }
            1 -> {
                bundle.putString(Constant.PARAM_CATEGORY_ID, categoryId)
                startActivity(YellowPageSearchResultActivity::class.java, bundle)
            }
            2 -> {
                bundle.putString(Constant.PARAM_CATEGORY_ID, categoryId)
                startActivity(ConvenienceServiceSearchActivity::class.java, bundle)
            }
            3, 4, 7 -> {
                setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
                finish()
            }
            5 -> {
                startActivity(StoreSearchActivity::class.java, bundle)
            }
            6 -> {
                bundle.putString(Constant.PARAM_CATEGORY_ID, categoryId)
                startActivity(MallSearchResultActivity::class.java, bundle)
            }
            8 -> {
                bundle.putInt(Constant.PARAM_SHOP_ID, shopId)
                startActivity(MallSearchResultActivity::class.java, bundle)
            }
            9 -> {
                startActivity(JobSearchResultActivity::class.java, bundle)
            }
            else -> {
            }
        }


    }

    override fun onDestroy() {
        KeyboardUtils.hideSoftInput(search_aet)
        super.onDestroy()
    }
}