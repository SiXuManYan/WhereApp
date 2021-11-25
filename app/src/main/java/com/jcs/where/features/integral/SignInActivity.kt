package com.jcs.where.features.integral

import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatCheckBox
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.response.SignListResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.LocalLanguageUtil
import kotlinx.android.synthetic.main.sign_in.*

/**
 * Created by Wangsw  2021/11/24 16:32.
 * 每日签到
 */
class SignInActivity : BaseMvpActivity<SignInPresenter>(), SignInView {

    override fun getLayoutId() = R.layout.sign_in

    override fun initView() {

        initLanguage()
    }

    private fun initLanguage() {
        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this)
        if (languageLocale.language == "zh") {
            ad_iv.setImageResource(R.mipmap.ic_sign_in_ad_chn)
        } else {
            ad_iv.setImageResource(R.mipmap.ic_sign_in_ad_en)
        }
    }

    override fun initData() {
        presenter = SignInPresenter(this)
        presenter.getUserInfo()
        presenter.getSignInList()
    }

    override fun bindListener() {
        rule_tv.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.sign_in_rule)
                .setMessage(R.string.sign_in_rule_message)
                .setCancelable(false)
                .setPositiveButton(R.string.ensure) { dialogInterface, i -> dialogInterface.dismiss() }
                .create().show()
        }
        sign_in_now_tv.setOnClickListener {
            presenter.signIn()
        }
        sign_in_tv.setOnClickListener {
            sign_in_now_tv.performClick()
        }
    }

    override fun bindUserIntegral(isSigned: Boolean) {
        if (isSigned) {
            sign_in_now_tv.apply {
                setText(R.string.already_sign_in)
                isClickable = false
            }
            sign_in_tv.apply {
                setText(R.string.finished)
                isClickable = false
            }
        } else {
            sign_in_now_tv.setText(R.string.sign_in_now)
            sign_in_tv.setText(R.string.to_finish)
        }
        sign_in_tv.isChecked = isSigned
    }


    override fun bindSignInList(response: SignListResponse) {

        val data = response.data

        data.forEachIndexed { index, dataBean ->

            if (index <= 3) {
                bindCommonItem(index, dataBean, first_date_ll)
            }
            if (index == 4 || index == 5) {
                bindCommonItem(index - 4, dataBean, second_date_ll)
            }
            if (index == 6) {
                val signStatus = dataBean.signStatus
                val integral = dataBean.integral
                final_date_tv.isChecked = signStatus == 1
                final_score_tv.isChecked = signStatus == 1
                final_score_tv.text = StringUtils.getString(R.string.add_format, integral)
                if (signStatus == 1) {
                    final_container_rl.setBackgroundResource(R.drawable.shape_sign_in_radius_4_selected)
                } else {
                    final_container_rl.setBackgroundResource(R.drawable.shape_sign_in_radius_4_normal)
                }

            }
        }

    }

    private fun bindCommonItem(index: Int, dataBean: SignListResponse.DataBean, container: LinearLayout) {

        val signStatus = dataBean.signStatus
        val integral = dataBean.integral

        val firstItem = container.getChildAt(index) as LinearLayout
        val date = firstItem.getChildAt(0) as CheckedTextView
        val tagIv = firstItem.getChildAt(1) as AppCompatCheckBox
        val score = firstItem.getChildAt(2) as CheckedTextView
        date.isChecked = signStatus == 1
        tagIv.isChecked = signStatus == 1
        score.isChecked = signStatus == 1
        score.text = StringUtils.getString(R.string.add_format, integral)

        if (signStatus == 1) {
            firstItem.setBackgroundResource(R.drawable.shape_sign_in_radius_4_selected)
        } else {
            firstItem.setBackgroundResource(R.drawable.shape_sign_in_radius_4_normal)
        }
    }


    override fun signInSuccess() = bindUserIntegral(true)

}