package com.jcs.where.features.bills.charges

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.bills.CallChargeChannel
import com.jcs.where.api.response.bills.CallChargeChannelItem
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.place.phone.PhonePlaceOrderActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.PermissionUtils
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_call_charges.*


/**
 * Created by Wangsw  2022/6/15 16:55.
 * 话费充值
 */
class CallChargesActivity : BaseMvpActivity<CallChargesPresenter>(), CallChargesView, OnItemClickListener {


    lateinit var mAdapter: CallChargesChanelItemAdapter

    private lateinit var selectItem: CallChargeChannelItem


    companion object {

        fun navigation(context: Context, data: CallChargeChannel) {
            val bundle = Bundle().apply {
                putParcelable(Constant.PARAM_DATA, data)
            }
            val intent = Intent(context, CallChargesActivity::class.java).putExtras(bundle)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_call_charges

    override fun initView() {
        initList()
        initExtra()
    }

    private fun initExtra() {
        intent.extras?.let { bundle->
            val data = bundle.getParcelable<CallChargeChannel>(Constant.PARAM_DATA)

            data?.let {
                channel_tv.text = it.channelName
                mAdapter.setNewInstance(it.channelItem)
            }
        }
    }


    private fun initList() {

        mAdapter = CallChargesChanelItemAdapter().apply {
            setOnItemClickListener(this@CallChargesActivity)
        }

        val decoration = DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), 1, SizeUtils.dp2px(15f), 0)
        face_value_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@CallChargesActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(decoration)
        }

        phone_et.addTextChangedListener(
            afterTextChanged = {
                val trim = it.toString().trim()
                if (trim.isNotBlank()) {
                    next_tv.alpha = 1.0f
                } else {
                    next_tv.alpha = 0.5f
                }
            }
        )

    }

    override fun initData() {
        presenter = CallChargesPresenter(this)

    }

    override fun bindListener() {
        connect_iv.setOnClickListener {

            PermissionUtils.permissionAny(this, {
                if (it) {
                    val uri: Uri = ContactsContract.Contacts.CONTENT_URI
                    val intent = Intent(Intent.ACTION_PICK, uri)
                    startActivityForResult(intent, 0)
                } else {
                    AppUtils.launchAppDetailsSettings()
                }


            }, Manifest.permission.READ_CONTACTS)

        }

        next_tv.setOnClickListener {
            val phone = phone_et.text.toString().trim()
            if (phone.isBlank() || !phone.startsWith("0") || phone.length != 11) {
                ToastUtils.showShort(R.string.recharge_phone_format)
                return@setOnClickListener
            }

            if (!::selectItem.isInitialized) {
                ToastUtils.showShort(R.string.select_money)
                return@setOnClickListener
            }

            PhonePlaceOrderActivity.navigation(this, phone, selectItem)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        selectItem = data


        mAdapter.data.forEach {
            it.isChecked = it == data
        }
        mAdapter.notifyDataSetChanged()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == 0) {
            if (intent == null) return
            val uri = intent.data
            val contacts = getPhoneContacts(uri!!)

            contacts?.let {

                val replace = contacts[1]?.replace(" ","")
                phone_et.setText(replace)
            }


        } else {
            super.onActivityResult(requestCode, resultCode, intent)
        }
    }

    private fun getPhoneContacts(uri: Uri): Array<String?>? {

        val contact = arrayOfNulls<String>(2)
        val cr = contentResolver
        // 取得电话本中开始一项的光标
        val cursor = cr.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            // 姓名
            val nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            contact[0] = cursor.getString(nameFieldColumnIndex)
            // 号码
            val ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId,
                null,
                null)
            if (phone != null) {
                phone.moveToFirst()
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
            phone!!.close()
            cursor.close()
        } else {
            return null
        }
        return contact
    }


}