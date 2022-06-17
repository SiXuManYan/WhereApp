package com.jcs.where.features.bills.charges

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.bills.FieldDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.bean.FaceValue
import com.jcs.where.features.bills.form.BillsFormActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_call_charges.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2022/6/15 16:55.
 * 话费充值
 */
class CallChargesActivity : BaseMvpActivity<CallChargesPresenter>(), CallChargesView, OnItemClickListener {



    /** 渠道名称 */
    private var billerTag = ""

    /** 账单类型 */
    private var billsType = 0

    /** 渠道描述 */
    private var description = ""

    /** 渠道服务费（加上充值费用为支付费用） */
    private var serviceCharge = BigDecimal.ZERO

    private var userInputMoney = BigDecimal.ZERO

    private var fieldDetail = ArrayList<FieldDetail>()

    lateinit var mAdapter: CallChargesAdapter


    companion object {

        fun navigation(
            context: Context,
            billerTag: String,
            description: String,
            serviceCharge: Double,
            fieldDetail: ArrayList<FieldDetail>,
            billsType: Int,
        ) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_TYPE, billsType)
                putString(Constant.PARAM_TAG, billerTag)
                putString(Constant.PARAM_DESCRIPTION, description)
                putDouble(Constant.PARAM_SERVICE_CHARGE, serviceCharge)
                putParcelableArrayList(Constant.PARAM_DATA, fieldDetail)
            }
            val intent = Intent(context, CallChargesActivity::class.java).putExtras(bundle)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_call_charges

    override fun initView() {
        initExtra()
        initList()
    }

    private fun initExtra() {
        intent.extras?.let {

            billsType = it.getInt(Constant.PARAM_TYPE, 0)
            billerTag = it.getString(Constant.PARAM_TAG, "")
            description = it.getString(Constant.PARAM_DESCRIPTION, "")

            val parcelableArrayList = it.getParcelableArrayList<FieldDetail>(Constant.PARAM_DATA)
            fieldDetail.addAll(parcelableArrayList!!)
            serviceCharge = BigDecimal(it.getDouble(Constant.PARAM_SERVICE_CHARGE, 0.0))
        }
        rule_tv.text = getString(R.string.service_charge_format, serviceCharge)
    }


    private fun initList() {

        mAdapter = CallChargesAdapter().apply {
            setOnItemClickListener(this@CallChargesActivity)
        }

        val gridLayoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0)
        face_value_rv.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }

        // 输入

    }

    override fun initData() {
        presenter =  CallChargesPresenter(this)
        presenter.getFakerData()
    }

    override fun bindListener() {

    }

    override fun bindFakerData(data: ArrayList<FaceValue>) {
        mAdapter.setNewInstance(data)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val faceValue = mAdapter.data[position]
        userInputMoney = BigDecimal(faceValue.value)
        amount_et.setText(BusinessUtils.formatPrice(userInputMoney))
    }


}