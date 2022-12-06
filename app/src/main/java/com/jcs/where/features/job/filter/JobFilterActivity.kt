package com.jcs.where.features.job.filter

import android.annotation.SuppressLint
import android.os.Bundle
import com.jcs.where.R
import com.jcs.where.api.response.job.FilterItem
import com.jcs.where.api.response.job.JobFilter
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.view.MyLayoutManager
import kotlinx.android.synthetic.main.activity_job_filter.*

/**
 * Created by Wangsw  2022/12/1 14:58.
 *  职位筛选
 */
class JobFilterActivity : BaseMvpActivity<JobFilterPresenter>(), JobFilterView {


    /** 薪资类型 0不限 1月薪 2年薪 3时薪*/
    private var salaryType = 0

    /** 最低薪水 */
    private var minSalary = 0

    /** 最高薪水 */
    private var maxSalary = 0

    /** 地区 */
    private var areas = ArrayList<Int>()

    /** 公司类型 */
    private var companyTypes = ArrayList<Int>()

    /** 学历 */
    private var eduLevel = ArrayList<Int>()

    /** 工作经验 */
    private var experienceLevel = ArrayList<Int>()


    private lateinit var salaryAdapter: JobFilterAdapter
    private lateinit var areaAdapter: JobFilterAdapter
    private lateinit var companyTypeAdapter: JobFilterAdapter
    private lateinit var eduAdapter: JobFilterAdapter
    private lateinit var experienceAdapter: JobFilterAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out)
    }

    override fun initView() {
        initFilter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initFilter() {

        // 薪资
        salaryAdapter = JobFilterAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val item = salaryAdapter.data[position]
                val itemId = item.id
                salaryType = itemId
                salaryAdapter.data.forEach {
                    it.nativeSelected = it == item
                }
                salaryAdapter.notifyDataSetChanged()

                if (itemId == 0) {
                    presenter.setEditAble(min_salary_et, false)
                    presenter.setEditAble(max_salary_et, false)
                    minSalary = 0
                    maxSalary = 0
                } else {
                    presenter.setEditAble(min_salary_et, true)
                    presenter.setEditAble(max_salary_et, true)
                }
            }
        }

        salary_rv.apply {
            adapter = salaryAdapter
            layoutManager = MyLayoutManager()
            isNestedScrollingEnabled = true
        }

        // 地区
        areaAdapter = JobFilterAdapter().apply {
            setOnItemClickListener { _, _, position ->
                presenter.handleMultipleChoice(position, areaAdapter, areas)
            }
        }
        city_rv.apply {
            adapter = areaAdapter
            layoutManager = MyLayoutManager()
            isNestedScrollingEnabled = true
        }

        // 公司类型
        companyTypeAdapter = JobFilterAdapter().apply {
            setOnItemClickListener { _, _, position ->
                presenter.handleMultipleChoice(position, companyTypeAdapter, companyTypes)
            }
        }
        type_rv.apply {
            adapter = companyTypeAdapter
            layoutManager = MyLayoutManager()
            isNestedScrollingEnabled = true
        }

        // 学历
        eduAdapter = JobFilterAdapter().apply {
            setOnItemClickListener { _, _, position ->
                presenter.handleMultipleChoice(position, eduAdapter, eduLevel)
            }
        }
        edu_level_rv.apply {
            adapter = eduAdapter
            layoutManager = MyLayoutManager()
            isNestedScrollingEnabled = true
        }

        // 经验
        experienceAdapter = JobFilterAdapter().apply {
            setOnItemClickListener { _, _, position ->
                presenter.handleMultipleChoice(position, experienceAdapter, experienceLevel)
            }
        }
        edu_level_rv.apply {
            adapter = experienceAdapter
            layoutManager = MyLayoutManager()
            isNestedScrollingEnabled = true
        }


    }


    override fun initData() {

    }

    override fun bindListener() {

    }

    override fun bindFilterItem(response: JobFilter, salaryType: ArrayList<FilterItem>) {

        salaryAdapter.setNewInstance(salaryType)
        areaAdapter.setNewInstance(response.area)
        companyTypeAdapter.setNewInstance(response.companyType)
        eduAdapter.setNewInstance(response.jobResumeEducationLevel)
        experienceAdapter.setNewInstance(response.experience)
    }
}