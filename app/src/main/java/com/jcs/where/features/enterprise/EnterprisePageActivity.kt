package com.jcs.where.features.enterprise

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2022/9/13 16:20.
 * 企业黄页
 */
class EnterprisePageActivity : BaseMvpActivity<EnterprisePagePresenter>(), EnterprisePageView {


    /** 企业所有黄页一级分类id */
    val firstCategoryIds = ArrayList<Int>()

    /** 用户当前选中的 分类id */
    var currentCategoryId = 0


    companion object {

        /**
         * 打开企业黄页
         * @param allYellowPageFirstCategoryId 企业所有黄页一级分类id
         *                                     首页金刚区点击时可直接传，其他位置可不传，会从sp中获取
         *
         * @param currentCategoryId 当前选中的分类id（用于高亮）
         */
        fun navigation(context: Context, allYellowPageFirstCategoryId: ArrayList<Int>? = null, currentCategoryId: Int? = null) {

            val first = ArrayList<Int>()
            if (!allYellowPageFirstCategoryId.isNullOrEmpty()) {
                // 首页直接传递
                first.addAll(allYellowPageFirstCategoryId)
            } else {
                // 从sp中获取 黄页所有一级分类id
                val firstIds = CacheUtil.getShareDefault().getString(Constant.SP_YELLOW_PAGE_ALL_FIRST_CATEGORY_ID, "")
                if (!firstIds.isNullOrBlank()) {
                    val fromJson = Gson().fromJson<ArrayList<Int>>(firstIds, object : TypeToken<ArrayList<Int>>() {}.type)
                    first.addAll(fromJson)
                }
            }

            val bundle = Bundle().apply {
                putIntegerArrayList(Constant.PARAM_DATA, first)
                currentCategoryId?.let {
                    putInt(Constant.PARAM_CATEGORY_ID, currentCategoryId)
                }
            }

            val intent = Intent(context, EnterprisePageActivity::class.java)
                .putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_enterprise_page

    override fun initView() {

        initExtra()
    }

    private fun initExtra() {

        intent.extras?.let {
            val elements = it.getIntegerArrayList(Constant.PARAM_DATA)
            if (!elements.isNullOrEmpty()) {
                firstCategoryIds.addAll(elements)
            }
            currentCategoryId = it.getInt(Constant.PARAM_CATEGORY_ID, 0)
        }
    }

    override fun initData() {
        presenter = EnterprisePagePresenter(this)
        presenter.getAllCategory(firstCategoryIds,currentCategoryId)
    }

    override fun bindListener() {

    }

    override fun bindCategory(response:ArrayList<Category>) {

    }
}