package com.jcs.where.features.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
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
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.activity_address

    override fun initView() {

        handleItemClick = intent.getBooleanExtra(Constant.PARAM_HANDLE_SELECT, false)

        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_un_address)
            setEmptyMessage(R.string.no_address_yet)
            setEmptyHint(R.string.please_add_shipping_address)
            addEmptyList(this)
        }

        mAdapter = AddressAdapter().apply {
            setEmptyView(emptyView)
            addChildClickViewIds(R.id.edit_iv)
            setOnItemChildClickListener(this@AddressActivity)
            setOnItemClickListener(this@AddressActivity)
        }


        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(itemDecoration)
    }

    override fun isStatusDark(): Boolean {
        return true
    }

    override fun initData() {
        presenter = AddressPresenter(this)
        presenter.list
    }

    override fun bindList(response: ArrayList<AddressResponse>) {
        if (response.isEmpty()) {
            emptyView.showEmptyContainer()
        }
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
                putString(Constant.PARAM_AREA_NAME, data.city_name)
                putInt(Constant.PARAM_AREA_ID, data.city_id)
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
        if (address.city_id == 0) {
            ToastUtils.showShort(getString(R.string.address_city_empty))
            startActivity(AddressEditActivity::class.java, Bundle().apply {
                putString(Constant.PARAM_ADDRESS_ID, address.id)
                putInt(Constant.PARAM_SEX, address.sex)
                putString(Constant.PARAM_ADDRESS, address.address)
                putString(Constant.PARAM_RECIPIENT, address.contact_name)
                putString(Constant.PARAM_PHONE, address.contact_number)
                putString(Constant.PARAM_AREA_NAME, address.city_name)
                putInt(Constant.PARAM_AREA_ID, address.city_id)
            })
        }else{
            setResult(Activity.RESULT_OK, Intent().putExtras(Bundle().apply {
                putSerializable(Constant.PARAM_DATA, address)
            }))
            finish()
        }




    }
}