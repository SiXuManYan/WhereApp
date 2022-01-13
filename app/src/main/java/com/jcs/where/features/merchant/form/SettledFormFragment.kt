package com.jcs.where.features.merchant.form

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.request.merchant.MerchantSettledData
import com.jcs.where.api.request.merchant.MerchantSettledPost
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.hotel.activity.CityPickerActivity
import com.jcs.where.mine.activity.merchant_settled.SettledTypeActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.RequestResultCode
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.fragment_form_settled.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/11/19 15:43.
 * 商家入住表单
 */
class SettledFormFragment : BaseMvpFragment<SettledFormPresenter>(), SettledFormView {


    private var merTypeId = 0
    private var areaId = 0

    /** 是否有实体店（1：有，2：没有） */
    private var hasPhysicalStore = 0
    private var isReCommit = false
    private var reCommitMerchantId = 0


    private lateinit var mImageAdapter: StoreRefundAdapter


    override fun getLayoutId() = R.layout.fragment_form_settled


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val bundle = it.data?.extras ?: return@registerForActivityResult
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                area_tv.text = bundle.getString(Constant.PARAM_SELECT_AREA_NAME)
                val area = bundle.getString(Constant.PARAM_SELECT_AREA_ID, "0")
                areaId = area.toInt()
            }
            RequestResultCode.RESULT_SETTLED_TYPE_TO_MERCHANT_SETTLED -> {
                mer_type_tv.text = bundle.getString("typeName", "")
                merTypeId = bundle.getInt("typeId", 0)
            }
        }

    }

    override fun initView(view: View) {

        mImageAdapter = StoreRefundAdapter().apply {
            addChildClickViewIds(R.id.delete_iv)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.delete_iv -> mImageAdapter.removeAt(position)
                }
            }
        }

        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun initData() {
        presenter = SettledFormPresenter(this)
    }

    override fun bindListener() {
        select_iv.setOnClickListener {
            val size = mImageAdapter.data.size
            if (size == 2) {
                ToastUtils.showShort(R.string.refund_image_max_2)
                return@setOnClickListener
            }
            val max = 2 - size
            FeaturesUtil.handleMediaSelect4Fragment(this, Constant.IMG, max)
        }

        area_tv.setOnClickListener {
            launcher.launch(Intent(requireContext(), CityPickerActivity::class.java))
        }
        mer_type_tv.setOnClickListener {
            launcher.launch(Intent(requireContext(), SettledTypeActivity::class.java))
        }

        physical_store_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.yes_rb -> {
                    hasPhysicalStore = 1
                }
                R.id.no_rb -> {
                    hasPhysicalStore = 2
                }


                else -> {
                }
            }

        }


        commit_tv.setOnClickListener {

            val apply = MerchantSettledPost().apply {
                first_name = first_name_et.text.toString()
                middle_name = middle_name_et.text.toString()
                last_name = last_name_et.text.toString()
                contact_number = contact_number_et.text.toString()
                email = email_et.text.toString()
                mer_name = mer_name_et.text.toString().trim()
                mer_address = mer_address_et.text.toString()
                mer_tel = mer_tel_et.text.toString()

                mer_type_id = merTypeId
                area_id = areaId
                has_physical_store = hasPhysicalStore
            }


            apply.apply {
                if (first_name.isBlank() || last_name.isBlank() || contact_number.isBlank()
                    || email.isBlank() || mer_name.isBlank() || mer_address.isBlank() || mer_tel.isBlank()
                ) {
                    ToastUtils.showShort(R.string.please_enter)
                    return@setOnClickListener
                }
                if (mer_type_id == 0 || area_id == 0 || has_physical_store == 0) {
                    ToastUtils.showShort(R.string.please_selected)
                    return@setOnClickListener
                }
            }

            if (mImageAdapter.data.size < 2) {
                ToastUtils.showShort(getString(R.string.please_upload))
                return@setOnClickListener
            }
            commit_tv.isClickable = false
            commit_tv.postDelayed(Runnable { commit_tv.isClickable = true }, 2000)
            presenter.upLoadImage(apply, ArrayList(mImageAdapter.data), isReCommit, reCommitMerchantId)
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val elements = Matisse.obtainPathResult(data)
        elements.forEach {
            mImageAdapter.addData(it)
        }

    }

    override fun postResult(result: Boolean) {
        commit_tv.isClickable = true
        EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_MERCHANT_POST_SUCCESS))
        clearCache()
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        when (baseEvent.code) {
            EventCode.EVENT_MERCHANT_CHANGE_TYPE -> {
                val response = baseEvent.data as MerchantSettledData
                isReCommit = true
                response.apply {
                    reCommitMerchantId = id
                    first_name_et.setText(first_name)
                    middle_name_et.setText(middle_name)
                    last_name_et.setText(last_name)
                    contact_number_et.setText(contact_number)
                    email_et.setText(email)
                    mer_name_et.setText(mer_name)
                    mer_address_et.setText(mer_address)
                    mer_tel_et.setText(mer_tel)
                }
            }
            else -> {

            }
        }
    }

    private fun clearCache() {
        for (index in 0 until container_ll.childCount) {

            val childView = container_ll.getChildAt(index)
            if (childView is AppCompatEditText) {
                childView.setText("")
            }
        }
        mer_type_tv.text = ""
        area_tv.text = ""
        yes_rb.isChecked = false
        no_rb.isChecked = false
        merTypeId = 0
        areaId = 0
        hasPhysicalStore = 0
        isReCommit = false
        reCommitMerchantId = 0
        mImageAdapter.setNewInstance(null)

    }


}

