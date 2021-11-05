package com.jcs.where.features.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.address.edit.AddressEditActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_address.*

/**
 * Created by Wangsw  2021/3/22 10:33.
 * 收货地址
 */
class AddressActivity : BaseMvpActivity<AddressPresenter>(), AddressView, OnItemChildClickListener, OnItemClickListener {

    var handleItemClick = false

    private lateinit var mAdapter: AddressAdapter

    override fun getLayoutId() = R.layout.activity_address

    override fun initView() {

        handleItemClick = intent.getBooleanExtra(Constant.PARAM_HANDLE_ADDRESS_SELECT, false)

        val emptyView = EmptyView(this).apply {
            showEmptyDefault()
        }

        mAdapter = AddressAdapter().apply {
            setEmptyView(emptyView)
            addChildClickViewIds(R.id.edit_iv)
            setOnItemChildClickListener(this@AddressActivity)
            setOnItemClickListener(this@AddressActivity)
        }


        recycler_view.setAdapter(mAdapter)
        recycler_view.addItemDecoration(itemDecoration)
    }

    override fun isStatusDark(): Boolean {
        return true
    }

    override fun initData() {
        presenter = AddressPresenter(this)
        presenter.list
    }

    override fun bindList(response: List<AddressResponse>) {
        mAdapter.setNewInstance(response.toMutableList())
    }

    override fun bindListener() {
        add_tv.setOnClickListener {
            startActivity(AddressEditActivity::class.java)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        if (view.id == R.id.edit_iv) {
            startActivity(AddressEditActivity::class.java, Bundle().apply {
                putString(Constant.PARAM_ADDRESS_ID, data.id)
                putInt(Constant.PARAM_SEX, data.sex)
                putString(Constant.PARAM_ADDRESS, data.address)
                putString(Constant.PARAM_RECIPIENT, data.contact_name)
                putString(Constant.PARAM_PHONE, data.contact_number)
            })
        }
    }

    private val itemDecoration: ItemDecoration
        get() {
            return DividerDecoration(
                ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
                0,
                0
            )
        }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        if (baseEvent.code == EventCode.EVENT_ADDRESS) {
            presenter.list
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (!handleItemClick) {
            return
        }
        val address = mAdapter.data[position]
        setResult(Activity.RESULT_OK, Intent().putExtras(Bundle().apply {
            putSerializable(Constant.PARAM_DATA, address)
        }))
        finish()


    }
}