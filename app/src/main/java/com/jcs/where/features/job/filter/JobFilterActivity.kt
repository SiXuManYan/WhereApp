package com.jcs.where.features.job.filter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.jcs.where.R
import com.jcs.where.api.response.job.FilterData
import com.jcs.where.api.response.job.FilterItem
import com.jcs.where.api.response.job.JobFilter
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.MyLayoutManager
import kotlinx.android.synthetic.main.activity_job_filter.*

/**
 * Created by Wangsw  2022/12/1 14:58.
 *  职位筛选
 */
class JobFilterActivity : BaseMvpActivity<JobFilterPresenter>(), JobFilterView {


    /** 薪资类型 0不限 1月薪 2年薪 3时薪 */
    private var salaryType = 0

    /** 最低薪水 */
    private var minSalary = ""

    /** 最高薪水 */
    private var maxSalary = ""

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
    private var gson = Gson()

    private var companyTypeHeight = 0
    private var isUp = false

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
                handleSalaryClick(position)
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
        experience_rv.apply {
            adapter = experienceAdapter
            layoutManager = MyLayoutManager()
            isNestedScrollingEnabled = true
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleSalaryClick(position: Int) {
        val item = salaryAdapter.data[position]
        val itemId = item.id
        salaryType = itemId
        salaryAdapter.data.forEach {
            it.nativeSelected = it == item
        }
        salaryAdapter.notifyDataSetChanged()
        handleSalaryValue(itemId)
    }

    private fun handleSalaryValue(itemId: Int) {
        if (itemId == 0) {
            presenter.setEditAble(min_salary_et, false)
            presenter.setEditAble(max_salary_et, false)
            minSalary = ""
            maxSalary = ""
        } else {
            presenter.setEditAble(min_salary_et, true)
            presenter.setEditAble(max_salary_et, true)
        }
    }


    override fun initData() {
        presenter = JobFilterPresenter(this)
        val cacheJson = intent.getStringExtra(Constant.PARAM_DATA)
        if (cacheJson.isNullOrBlank()) {
            presenter.getFilterItem()
        } else {

            // 记录上次选择
            val cache = gson.fromJson(cacheJson, FilterData::class.java)
//            Log.d("aaaa", "返回 == $cacheJson")
            salaryType = cache.salaryType
            minSalary = cache.minSalary
            maxSalary = cache.maxSalary
            areas.addAll(cache.areas)
            companyTypes.addAll(cache.companyTypes)
            eduLevel.addAll(cache.eduLevel)
            experienceLevel.addAll(cache.experienceLevel)

            salaryAdapter.setNewInstance(cache.salaryData)
            areaAdapter.setNewInstance(cache.areaData)
            companyTypeAdapter.setNewInstance(cache.companyTypeData)
            setSingle()
            eduAdapter.setNewInstance(cache.eduData)
            experienceAdapter.setNewInstance(cache.experienceData)

            min_salary_et.setText(minSalary)
            max_salary_et.setText(maxSalary)
            handleSalaryValue(salaryType)


            type_rv.post { companyTypeHeight = type_rv.measuredHeight }
        }

    }


    override fun bindFilterItem(response: JobFilter, salaryType: ArrayList<FilterItem>) {

        salaryAdapter.setNewInstance(salaryType)
        areaAdapter.setNewInstance(response.area)
        companyTypeAdapter.setNewInstance(response.companyType)
        setSingle()
        eduAdapter.setNewInstance(response.jobResumeEducationLevel)
        experienceAdapter.setNewInstance(response.experience)

    }

    override fun bindListener() {

        min_salary_et.addTextChangedListener(afterTextChanged = {
            minSalary = it.toString().trim()
        })

        max_salary_et.addTextChangedListener(afterTextChanged = {
            maxSalary = it.toString().trim()
        })

        more_company_type.setOnClickListener {
            val layoutParams = type_rv.layoutParams as LinearLayout.LayoutParams
            if (isUp) {
                // 收起
                isUp = false

                if (companyTypeHeight > SizeUtils.dp2px(112f)) {
                    layoutParams.height = SizeUtils.dp2px(112f)
                }
                more_company_type.setText(R.string.down)
            } else {
                isUp = true
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                more_company_type.setText(R.string.up)
            }
            type_rv.layoutParams = layoutParams

        }

        reset_tv.setOnClickListener {
            handleSalaryClick(0)
            areas.clear()
            companyTypes.clear()
            eduLevel.clear()
            experienceLevel.clear()

            presenter.clearSelected(areaAdapter)
            presenter.clearSelected(companyTypeAdapter)
            presenter.clearSelected(eduAdapter)
            presenter.clearSelected(experienceAdapter)

            setFilterData()
        }

        confirm_tv.setOnClickListener {
            setFilterData()
            finish()
        }

    }

    private fun setFilterData() {
        val select = FilterData().apply {
            salaryType = this@JobFilterActivity.salaryType
            minSalary = this@JobFilterActivity.minSalary
            maxSalary = this@JobFilterActivity.maxSalary
            areas.addAll(this@JobFilterActivity.areas)
            companyTypes.addAll(this@JobFilterActivity.companyTypes)
            eduLevel.addAll(this@JobFilterActivity.eduLevel)
            experienceLevel.addAll(this@JobFilterActivity.experienceLevel)

            salaryData.addAll(salaryAdapter.data)
            areaData.addAll(areaAdapter.data)
            companyTypeData.addAll(companyTypeAdapter.data)
            eduData.addAll(eduAdapter.data)
            experienceData.addAll(experienceAdapter.data)

        }
        val toJson = gson.toJson(select)
//        Log.d("aaaa", "传递 == $toJson")
        setResult(RESULT_OK, Intent().putExtra(Constant.PARAM_DATA, toJson))
    }

    private fun setSingle() {

        type_rv.post {
            companyTypeHeight = type_rv.measuredHeight
            if (companyTypeHeight > SizeUtils.dp2px(112f)) {
                val layoutParams = type_rv.layoutParams as LinearLayout.LayoutParams
                layoutParams.height = SizeUtils.dp2px(112f)
                type_rv.layoutParams = layoutParams
            }
        }

    }
}