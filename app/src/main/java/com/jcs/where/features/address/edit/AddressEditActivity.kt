package com.jcs.where.features.address.edit

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.home.watcher.AfterTextChangeWatcher
import com.jcs.where.features.city.CityPickerActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_address_edit.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/3/22 14:19.
 * 收货地址编辑
 */
class AddressEditActivity : BaseMvpActivity<AddressEditPresenter>(), AddressEditView {

    private var mAddress: String? = null
    private var mRecipient: String? = null
    private var mSex = 0
    private var mPhone: String? = null
    private var mAddressId: String? = null
    private var mAreaId = 0
    private var mAreaName = ""

    /**
     * 是否是修改
     */
    private var isChange = false

    override fun getLayoutId() = R.layout.activity_address_edit

    override fun initView() {

    }

    override fun isStatusDark(): Boolean = true

    override fun initData() {
        presenter = AddressEditPresenter(this)
        val bundle = intent.extras
        if (bundle != null) {
            isChange = true
            delete_tv.visibility = View.VISIBLE
            mAddressId = bundle.getString(Constant.PARAM_ADDRESS_ID, "")
            mAddress = bundle.getString(Constant.PARAM_ADDRESS, "")
            mRecipient = bundle.getString(Constant.PARAM_RECIPIENT, "")
            mSex = bundle.getInt(Constant.PARAM_SEX, 0)
            mPhone = bundle.getString(Constant.PARAM_PHONE, "")
            mAreaId = bundle.getInt(Constant.PARAM_AREA_ID, 0)
            mAreaName = bundle.getString(Constant.PARAM_AREA_NAME, "")
            city_tv.text = mAreaName

            address_et.setText(mAddress)
            recipient_et.setText(mRecipient)
            if (mSex == 1) {
                man_rb.isChecked = true
            } else {
                woman_rb.isChecked = true
            }
            phone_et.setText(mPhone)
            title_tv.setText(R.string.edit_address)
            return
        }
        delete_tv.visibility = View.GONE
        title_tv.setText(R.string.add_address)
    }

    override fun bindListener() {
        address_et.addTextChangedListener(object : AfterTextChangeWatcher() {
            override fun onAfterTextChanged(editable: Editable) {
                checkEnable()
            }
        })
        recipient_et.addTextChangedListener(object : AfterTextChangeWatcher() {
            override fun onAfterTextChanged(editable: Editable) {
                checkEnable()
            }
        })
        phone_et.addTextChangedListener(object : AfterTextChangeWatcher() {
            override fun onAfterTextChanged(editable: Editable) {
                checkEnable()
            }
        })
        save_tv.setOnClickListener { handleSave() }
        delete_tv.setOnClickListener { deleteAddress() }
        city_tv.setOnClickListener {
            launcher.launch(Intent(this, CityPickerActivity::class.java).putExtra(Constant.PARAM_HIDE_CURRENT_LOCATION,true))
        }
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val bundle = it.data?.extras ?: return@registerForActivityResult
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                mAreaName = bundle.getString(Constant.PARAM_SELECT_AREA_NAME,"")
                val areaId = bundle.getString(Constant.PARAM_SELECT_AREA_ID, "0")
                mAreaId = areaId.toInt()
                city_tv.text = mAreaName
            }

        }

    }

    private fun checkEnable() {
        val address = address_et.text.toString().trim()
        val recipient = recipient_et.text.toString().trim()
        val phone = phone_et.text.toString().trim()
        if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(recipient) && !TextUtils.isEmpty(phone)) {
            save_tv.alpha = 1.0f
        } else {
            save_tv.alpha = 0.5f
        }
    }

    private fun handleSave() {
        val address = address_et.text.toString().trim()
        val recipient = recipient_et.text.toString().trim()
        val phone = phone_et.text.toString().trim()
        mSex = if (man_rb.isChecked) {
            1
        } else {
            2
        }

        presenter.handleSave(address, recipient, phone, mSex, isChange, mAddressId, mAreaId)
    }

    private fun deleteAddress() {
        AlertDialog.Builder(this)
            .setTitle(R.string.hint)
            .setCancelable(false)
            .setMessage(getString(R.string.confirm_delete_address))
            .setPositiveButton(R.string.confirm) { _: DialogInterface?, _: Int -> presenter.deleteAddress(mAddressId) }
            .setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            .create().show()
    }

    override fun editSuccess() {
        ToastUtils.showShort("edit success")
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_ADDRESS))
        finish()
    }

    override fun addAddressSuccess() {
        ToastUtils.showShort("add success")
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_ADDRESS))
        finish()
    }

    override fun deleteAddressSuccess() {
        ToastUtils.showShort("delete success")
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_ADDRESS))
        finish()
    }
}