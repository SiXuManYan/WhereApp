package com.jcs.where.features.bills.form

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.bills.BillAccount
import com.jcs.where.api.response.bills.FieldDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.account.BillAccountActivity
import com.jcs.where.features.bills.place.BillsPlaceOrderActivity
import com.jcs.where.utils.AnimationUtils
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_form.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2022/6/8 17:33.
 * 水电表单
 */
class BillsFormActivity : BaseMvpActivity<BillsFormPresenter>(), BillsFormView, AfterEditChanged {


    /** 渠道名称 */
    private var billerTag = ""

    /** 账单类型（1-话费，2-水费，3-电费，4-网费） */
    private var billsType = 0

    /** 渠道描述 */
    private var description = ""

    /** 渠道服务费（加上充值费用为支付费用） */
    private var serviceCharge = BigDecimal.ZERO

    private var userInputMoney = BigDecimal.ZERO

    private var fieldDetail = ArrayList<FieldDetail>()

    private lateinit var mAdapter: BillsFormAdapter
    private lateinit var mDiscountAdapter: BillsDiscountAdapter


    private var firstMax = 100
    private var secondMax = 100

    override fun isStatusDark() = true

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
            val intent = Intent(context, BillsFormActivity::class.java).putExtras(bundle)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    /** 处理选择地址 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras ?: return@registerForActivityResult
            val first = bundle.getString(Constant.PARAM_FIRST, "")
            val second = bundle.getString(Constant.PARAM_SECOND, "")
            setCacheData(first, second)
        }
    }

    override fun getLayoutId() = R.layout.activity_bills_form

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initExtra()
        initList()

        val alphaAnimation = AnimationUtils.getAlphaAnimation(0.5f, 0.9f, 1000)
        hint_ll.startAnimation(alphaAnimation)


        Handler(mainLooper).postDelayed({
            val animation = AnimationUtils.getAlphaAnimation(0.9f, 0f, 1000, object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) = Unit

                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                    hint_ll.visibility = View.GONE
                }

            })
            hint_ll.startAnimation(animation)
        }, 4500)

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

        channel_tv.text = billerTag
        desc_tv.text = description

        mAdapter = BillsFormAdapter().apply {
            setNewInstance(fieldDetail)
            afterEditChanged = this@BillsFormActivity
            fieldDetail.forEachIndexed { index, it ->
                if (index == 0) {
                    firstMax = it.Width
                }
                if (index == 1) {
                    secondMax = it.Width
                }
            }
        }

        channel_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@BillsFormActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
                SizeUtils.dp2px(15f),
                0))
        }

        // 折扣
        mDiscountAdapter = BillsDiscountAdapter()
        discount_rv.apply {
            adapter = mDiscountAdapter
            layoutManager = LinearLayoutManager(this@BillsFormActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.transparency),
                SizeUtils.dp2px(12f),
                0,
                0))
        }

    }

    override fun initData() {
        presenter = BillsFormPresenter(this)
        presenter.getDiscountList(billsType)
        presenter.getDefaultAccount(billsType)
    }

    override fun bindDiscountList(response: ArrayList<String>) {
        mDiscountAdapter.setNewInstance(response)
    }

    override fun bindDefaultAccount(response: BillAccount) {
        setCacheData(response.first_field, response.second_field)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCacheData(first: String, second: String) {

        mAdapter.data.forEachIndexed { index, fieldDetail ->
            if (first.length > fieldDetail.Width || second.length > fieldDetail.Width) {
                next_tv.alpha = 0.6f
            }
            if (index == 0) {
                fieldDetail.nativeCache = first
            }
            if (index == 1) {
                fieldDetail.nativeCache = second
                return@forEachIndexed
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun bindListener() {


        amount_et.addTextChangedListener(
            afterTextChanged = {
                val input = it.toString()
                checkAlpha(input)
                userInputMoney = BusinessUtils.getSafeBigDecimal(input)
            }
        )

//        amount_et.setText(BusinessUtils.formatPrice(userInputMoney))

        next_tv.setOnClickListener {

            val text = amount_et.text.toString().trim()
            if (text.isNullOrBlank()) {
                ToastUtils.showShort("Please enter the amount.")
                return@setOnClickListener
            }

            if (text.contains(".")) {
                // 多个小数点
                if (text.split(".").size > 2) {
                    ToastUtils.showShort(R.string.please_input_right_amount)
                    return@setOnClickListener
                }
                val lastIndexOf = text.lastIndexOf(".")
                if (lastIndexOf > -1) {
                    val length = text.substring(lastIndexOf + 1).length
                    // 小数点后位数超过2
                    if (length > 2) {
                        ToastUtils.showShort(R.string.please_input_right_amount)
                        return@setOnClickListener
                    }
                }
            }

            // ^[0-9]+(.[0-9]{2})?\$ 保留两位小数
            // ^[0-9]{6}(.[0-9]{2})?$    金额输入规则：0.00 ～ 999999.99
            val safeBigDecimal = BusinessUtils.getSafeBigDecimal(text)
            if (safeBigDecimal.compareTo(BigDecimal.ZERO) != 1 || safeBigDecimal.compareTo(BigDecimal(999999.99)) == 1) {
                // 0.00 ～ 999999.99
                ToastUtils.showShort(R.string.please_input_right_amount)
                return@setOnClickListener
            }

            val data = mAdapter.data
            data.forEach {
                if (it.nativeUserInput.isBlank()) {
                    ToastUtils.showShort("Please enter " + it.Tag)
                    return@setOnClickListener
                }
                if (it.nativeUserInput.length > it.Width) {

                    return@setOnClickListener
                }
            }

            BillsPlaceOrderActivity.navigation(this,
                billerTag,
                userInputMoney.toDouble(),
                serviceCharge.toDouble(),
                fieldDetail,
                billsType)
        }

        account_tv.setOnClickListener {
            searchLauncher.launch(Intent(this, BillAccountActivity::class.java).putExtras(Bundle().apply {
                putInt(Constant.PARAM_TYPE, billsType)
            }))
        }

    }

    private fun checkAlpha(input: String) {
        var lengthOverCount = 0
        mAdapter.data.forEach { item ->
            if (item.lengthOver) {
                lengthOverCount++
            }
        }

        if (input.isNotBlank() && lengthOverCount == 0) {
            BusinessUtils.setViewClickable(true, next_tv)
        } else {
            BusinessUtils.setViewClickable(false, next_tv)
        }
    }


    override fun onBackPressed() {
        onBackClick()
    }

    private fun onBackClick() {
        AlertDialog.Builder(this)
            .setTitle(R.string.prompt)
            .setMessage(R.string.give_up_bills_payment_hint)
            .setCancelable(false)
            .setPositiveButton(R.string.confirm) { dialogInterface, i ->
                dialogInterface.dismiss()
                finish()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create().show()
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                finish()
            }
        }

    }

    override fun afterTextChanged(trim: String) {
        checkAlpha(amount_et.text.toString().trim())
    }

}