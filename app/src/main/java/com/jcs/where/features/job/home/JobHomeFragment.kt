package com.jcs.where.features.job.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.jcs.where.R
import com.jcs.where.api.response.job.FilterData
import com.jcs.where.api.response.job.Job
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.job.filter.JobFilterActivity
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocalLanguageUtil
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_job_home.*

/**
 * Created by Wangsw  2022/12/15 10:47.
 * 新版招聘首页
 *
 */
class JobHomeFragment : BaseMvpFragment<JobHomePresenter>(), JobHomeView, SwipeRefreshLayout.OnRefreshListener {


    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: JobHomeAdapter
    private lateinit var emptyView: EmptyView

    private var search: String? = null

    /** 筛选项 */
    private var selectedData: FilterData? = null
    private var selectedFilterJson = ""

    override fun getLayoutId() = R.layout.fragment_job_home


    /** 处理选择地址 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras ?: return@registerForActivityResult
            val selectedJson = bundle.getString(Constant.PARAM_DATA, "")
            if (selectedJson.isNullOrBlank()) return@registerForActivityResult
            selectedFilterJson = selectedJson
            val gson = Gson()
            selectedData = gson.fromJson(selectedJson, FilterData::class.java)
            onRefresh()

            selectedData?.let { select ->
                if (select.salaryType == 0 && select.areas.isEmpty() && select.companyTypes.isEmpty() &&
                    select.eduLevel.isEmpty() && select.experienceLevel.isEmpty()
                ) {
                    filter_iv.setImageResource(R.mipmap.ic_job_filter_normal)
                } else {
                    filter_iv.setImageResource(R.mipmap.ic_job_filter_blue)
                }
            }


        }
    }

    override fun initView(view: View?) {
        initContent()
    }


    private fun initContent() {

        swipe_layout.setOnRefreshListener(this)
        swipe_layout.setColorSchemeColors(ColorUtils.getColor(R.color.color_1c1380))
        emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)


        mAdapter = JobHomeAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getJobList(page, search, selectedData)
            }
        }


        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = manager
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5),
                SizeUtils.dp2px(4f),
                0,
                0))
        }
        initDefaultUI()
    }


    private fun initDefaultUI() {
        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(requireContext())
        if (languageLocale.language == "zh") {
            back_iv.setImageResource(R.mipmap.ic_job_cn)
        } else {
            back_iv.setImageResource(R.mipmap.ic_job_en)
        }

    }

    override fun initData() {
        presenter = JobHomePresenter(this)

    }

    override fun loadOnVisible() {
        onRefresh()
    }

    override fun bindListener() {

        back_iv.setOnClickListener {
            activity?.finish()
        }
        search_ll.setOnClickListener {
            startActivity(SearchAllActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 9)
                putBoolean(Constant.PARAM_HIDE, true)
            })
        }
//        cv_iv.setOnClickListener {
//            startActivityAfterLogin(CvHomeActivity::class.java)
//        }

        filter_iv.setOnClickListener {
            searchLauncher.launch(Intent(requireContext(), JobFilterActivity::class.java)
                .putExtras(Bundle().apply {
                    putString(Constant.PARAM_DATA, selectedFilterJson)
                }))
        }

    }


    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getJobList(page, search, selectedData)
    }

    override fun bindJobList(toMutableList: MutableList<Job>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }
        val loadMoreModule = mAdapter.loadMoreModule
        if (toMutableList.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(toMutableList)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(toMutableList)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }


}