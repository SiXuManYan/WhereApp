package com.jcs.where.features.job.form.city

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.CityPickerResponse
import com.jcs.where.base.mvp.BaseBottomSheetDialogFragment
import com.jcs.where.base.mvp.FixedHeightBottomSheetDialog
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.dialog_cv_city.*

/**
 * Created by Wangsw  2022/9/29 16:49.
 *
 */
class CvCityFragment : BaseBottomSheetDialogFragment<CvCityPresenter>(), CvCityView {

    var lastSelectedCityId = 0
    var lastCityData = ArrayList<CityPickerResponse.CityChild>()
    private lateinit var mAdapter: CvCityAdapter
    private lateinit var emptyView: EmptyView
    var onSelectedCity: OnSelectedCity? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        FixedHeightBottomSheetDialog(requireContext(), theme, ScreenUtils.getScreenHeight() * 80 / 100)

    override fun getLayoutId() = R.layout.dialog_cv_city

    @SuppressLint("NotifyDataSetChanged")
    override fun initView(parent: View) {
        emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()

        mAdapter = CvCityAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener { _, _, position ->
                val allData = mAdapter.data
                val cityChild = allData[position]
                allData.forEach {
                    it.nativeIsSelected = it.id == cityChild.id
                }
                mAdapter.notifyDataSetChanged()
                onSelectedCity?.onSelectedCity(cityChild, allData)
                dismissAllowingStateLoss()
            }
        }

        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = manager
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5),
                1,
                SizeUtils.dp2px(15f),
                0))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        presenter = CvCityPresenter(this)
        if (lastCityData.isNullOrEmpty()) {
            presenter.getCityData()
        } else {
            mAdapter.setNewInstance(lastCityData)


            val allData = mAdapter.data
            allData.forEach {
                it.nativeIsSelected = it.id == (lastSelectedCityId.toString())
            }
            mAdapter.notifyDataSetChanged()

        }

    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            dismiss()
        }
    }

    override fun bindData(toMutableList: MutableList<CityPickerResponse.CityChild>) {
        mAdapter.setNewInstance(toMutableList)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        onSelectedCity?.onDismiss()
    }



}

interface OnSelectedCity {
    fun onSelectedCity(cityChild: CityPickerResponse.CityChild, allData: MutableList<CityPickerResponse.CityChild>)
    fun onDismiss()

}

interface CvCityView : BaseMvpView {
    fun bindData(toMutableList: MutableList<CityPickerResponse.CityChild>)

}


class CvCityPresenter(private var view: CvCityView) : BaseMvpPresenter(view) {


    fun getCityData() {
        requestApi(mRetrofit.getCityPickers("list"), object : BaseMvpObserver<CityPickerResponse>(view) {
            override fun onSuccess(response: CityPickerResponse) {
                view.bindData(response.lists.toMutableList())
            }

        })
    }

}