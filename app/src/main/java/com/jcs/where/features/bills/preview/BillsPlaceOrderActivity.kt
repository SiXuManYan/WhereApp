package com.jcs.where.features.bills.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.bills.FieldDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_place_order.*

/**
 * Created by Wangsw  2022/6/9 14:53.
 * 统一下单
 */
class BillsPlaceOrderActivity : BaseMvpActivity<BillsPlaceOrderPresenter>(), BillsPlaceOrderView {


    private var billerTag = ""
    private var firstField = ""
    private var secondField = ""
    private var money: Double = 0.0
    private var fieldDetail = ArrayList<FieldDetail>()

    private lateinit var mAdapter: BillsPlaceOrderAdapter

    companion object {

        fun navigation(context: Context, billerTag: String, money: Double, fieldDetail: ArrayList<FieldDetail>) {
            val bundle = Bundle().apply {
                putString(Constant.PARAM_TAG, billerTag)
                putDouble(Constant.PARAM_AMOUNT, money)
                putParcelableArrayList(Constant.PARAM_DATA, fieldDetail)
            }
            val intent = Intent(context, BillsPlaceOrderActivity::class.java).putExtras(bundle)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_bills_place_order

    override fun initView() {
        initExtra()
        initList()
    }


    private fun initExtra() {
        intent.extras?.let {
            billerTag = it.getString(Constant.PARAM_TAG, "")
            money = it.getDouble(Constant.PARAM_AMOUNT, 0.0)
            val parcelableArrayList = it.getParcelableArrayList<FieldDetail>(Constant.PARAM_DATA)
            fieldDetail.addAll(parcelableArrayList!!)

        }
    }

    private fun initList() {
        mAdapter = BillsPlaceOrderAdapter().apply {
            setNewInstance(fieldDetail)
        }
        content_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@BillsPlaceOrderActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f), SizeUtils.dp2px(15f), 0))
        }
    }


    override fun initData() {
        presenter = BillsPlaceOrderPresenter(this)
    }

    override fun bindListener() {
        confirm_tv.setOnClickListener {
            mAdapter.data.forEachIndexed { index, fieldDetail ->
                when (index) {
                    0 -> {
                        firstField = fieldDetail.nativeUserInput
                    }
                    1 -> {
                        secondField = fieldDetail.nativeUserInput
                    }
                }
            }
            presenter.placeOrder(billerTag,firstField,secondField,money)
        }
    }
}