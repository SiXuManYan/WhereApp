package com.jcs.where.features.job.form.edu

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.job.Degree
import com.jcs.where.api.response.job.EduDet
import com.jcs.where.api.response.job.EduRequest
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.form.CvFormPresenter
import com.jcs.where.features.job.form.CvFormView
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_job_cv_edu.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/10/20 14:48.
 * 教育背景
 */
class CvFormEduActivity : BaseMvpActivity<CvFormPresenter>(), CvFormView {

    /**
     * 教育经历id
     * 不为0时为修改内容
     */
    private var draftEduId = 0

    private var eduRequest = EduRequest()

    /** 专业标题 */
    private var extendTitle: String? = ""

    var lastDegreeId = 0

    private var degreeDialog: BottomSheetDialog? = null

    private lateinit var mDegreeAdapter: DegreeAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_edu

    override fun initView() {
        draftEduId = intent.getIntExtra(Constant.PARAM_ID, 0)
        if (draftEduId > 0) {
            delete_tv.visibility = View.VISIBLE
        }
        initDegree()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initDegree() {
        mDegreeAdapter = DegreeAdapter().apply {

            setOnItemClickListener { _, _, position ->

                // 学历
                val degree = mDegreeAdapter.data[position]
                val id = degree.id
                eduRequest.educational_level_id = id
                degree_tv.text = degree.educational_level

                mDegreeAdapter.data.forEach {
                    it.nativeSelected = it.id == id
                }
                mDegreeAdapter.notifyDataSetChanged()

                // 显示专业
                extendTitle = degree.extend_title
                if (extendTitle.isNullOrBlank()) {
                    extend_title_ll.visibility = View.GONE
                } else {
                    extend_title_ll.visibility = View.VISIBLE
                    extend_title_tv.text = extendTitle
                }
                extend_value_et.setText("")
                eduRequest.vocational_course = ""

                degreeDialog?.dismiss()

            }
        }
    }

    override fun initData() {
        presenter = CvFormPresenter(this)
        presenter.getEduDet(draftEduId)
        presenter.getDegreeList()
    }

    override fun bindListener() {
        school_name_et.addTextChangedListener(afterTextChanged = {
            eduRequest.educational_attainment = it.toString().trim()
            handleClickable()
        })

        extend_value_et.addTextChangedListener(afterTextChanged = {
            eduRequest.vocational_course = it.toString().trim()
            handleClickable()
        })
        degree_tv.setOnClickListener {


            if (degreeDialog != null) {
                degreeDialog?.show()
            } else {
                showDegree()
            }
        }

        save_tv.setOnClickListener(object : ClickUtils.OnDebouncingClickListener(500) {
            override fun onDebouncingClick(v: View?) {
                presenter.handleSaveEdu(draftEduId, eduRequest)
            }
        })


        delete_tv.setOnClickListener {
            AlertDialog.Builder(this, R.style.JobAlertDialogTheme)
                .setCancelable(false)
                .setTitle(R.string.hint)
                .setMessage(R.string.delete_hint)
                .setPositiveButton(R.string.confirm) { dialog: DialogInterface, which: Int ->
                    presenter.deleteEducation(draftEduId)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .create().show()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun bindEduDet(response: EduDet) {

        val schoolName = response.educational_attainment
        school_name_et.setText(schoolName)


        // 学历
        val degree = response.educational_level
        lastDegreeId = degree.id
        degree_tv.text = degree.educational_level
        extend_title_tv.text = degree.extend_title

        mDegreeAdapter.data.forEach {
            if (it.id == lastDegreeId) {
                it.nativeSelected = true
            }
        }
        mDegreeAdapter.notifyDataSetChanged()


        val vocationalCourse = response.vocational_course
        if (vocationalCourse.isNullOrBlank()) {
            extend_title_ll.visibility = View.GONE
        } else {
            extend_value_et.setText(vocationalCourse)
            extend_title_ll.visibility = View.VISIBLE
        }


        // 修改备用
        eduRequest.apply {
            this.educational_attainment = schoolName
            this.educational_level_id = degree.id
            this.vocational_course = vocationalCourse
        }

        // 处理历史数据是否可提交
        handleClickable()

    }


    private fun handleClickable() {
        val school = eduRequest.educational_attainment
        val levelId = eduRequest.educational_level_id
        val course = eduRequest.vocational_course

        if (school.isNotBlank() && levelId != 0) {


            if (TextUtils.isEmpty(extendTitle)) {
                save_tv.isClickable = true
                save_tv.alpha = 1.0f
            } else {
                if (course.isNullOrBlank()) {
                    save_tv.isClickable = false
                    save_tv.alpha = 0.5f
                } else {
                    save_tv.isClickable = true
                    save_tv.alpha = 1.0f
                }
            }

        } else {
            save_tv.isClickable = false
            save_tv.alpha = 0.5f
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    override fun bindDegreeList(response: ArrayList<Degree>) {
        mDegreeAdapter.setNewInstance(response)

        mDegreeAdapter.data.forEach {
            if (it.id == lastDegreeId) {
                it.nativeSelected = true
            }
        }
        mDegreeAdapter.notifyDataSetChanged()

    }


    private fun showDegree() {
        val timeDialog = BottomSheetDialog(this)
        this.degreeDialog = timeDialog
        val view = LayoutInflater.from(this).inflate(R.layout.layout_select_time, null)
        val title_tv = view.findViewById<TextView>(R.id.title_tv)
        title_tv.setText(R.string.select_degree)
        timeDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val rv = view.findViewById<RecyclerView>(R.id.recycler_view)
        rv.apply {
            adapter = mDegreeAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5), 1, SizeUtils.dp2px(15f), 0))
        }

        view.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
            timeDialog.dismiss()
        }
        timeDialog.show()
    }


    override fun handleSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_CV_EDU))
        finish()
    }

    override fun deleteEducationSuccess() {
        EventBus.getDefault().post(BaseEvent(EventCode.EVENT_DELETE_CV_EDU, draftEduId))
        finish()
    }

}