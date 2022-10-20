package com.jcs.where.features.job.form.edu

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.job.EduDet
import com.jcs.where.api.response.job.EduRequest
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.form.CvFormPresenter
import com.jcs.where.features.job.form.CvFormView
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_job_cv_edu.*

/**
 * Created by Wangsw  2022/10/20 14:48.
 * 教育背景
 */
class CvFormEduActivity : BaseMvpActivity<CvFormPresenter>(), CvFormView {


    private var eduId = 0

    private var eduRequest = EduRequest()


    private var degreeDialog: BottomSheetDialog? = null

    private lateinit var mDegreeAdapter: DegreeAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_cv_edu

    override fun initView() {
        eduId = intent.getIntExtra(Constant.PARAM_ID, 0)

    }

    override fun initData() {
        presenter = CvFormPresenter(this)
        presenter.getEduDet(eduId)
        presenter.getEduLevelList()
    }

    override fun bindListener() {

    }

    override fun bindEduDet(response: EduDet) {
        val educationalAttainment = response.educational_attainment
        school_name_et.setText(educationalAttainment)


        val eduLevelItem = response.educational_level_item
        edu_level_tv.text = eduLevelItem.educational_level
        extend_title_tv.text = eduLevelItem.extend_title

        val vocationalCourse = response.vocational_course
        extend_value_et.setText(vocationalCourse)

        // 修改备用
        eduRequest.apply {
            this.educational_attainment = educationalAttainment
            this.educational_level_id = eduLevelItem.id
            this.vocational_course = vocationalCourse
        }

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

}