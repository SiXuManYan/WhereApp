package com.jcs.where.features.mall.detail.sku

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallAttributeValue
import com.jcs.where.api.response.mall.MallGoodDetail
import com.jcs.where.base.mvp.BaseBottomSheetDialogFragment
import com.jcs.where.base.mvp.FixedHeightBottomSheetDialog
import com.jcs.where.widget.NumberView2
import kotlinx.android.synthetic.main.fragment_mall_sku.*

/**
 * Created by Wangsw  2021/12/13 14:45.
 *
 */
class MallSkuFragment : BaseBottomSheetDialogFragment<MallSkuPresenter>(), MallSkuView {


    lateinit var data: MallGoodDetail

    private lateinit var mAdapter: SkuFirstAdapter

    private   var value = ArrayList<MallAttributeValue>()

    override fun getLayoutId() = R.layout.fragment_mall_sku


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        FixedHeightBottomSheetDialog(requireContext(), theme, ScreenUtils.getScreenHeight() * 9 / 10)

    override fun initView(parent: View) {

        Glide.with(requireContext()).load(data.main_image).into(good_iv)

        ("₱" + data.min_price + " - " + "₱" + data.max_price).also { price_tv.text = it }

        // content
        mAdapter = SkuFirstAdapter()
        content_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = true
        }
        mAdapter.setNewInstance(data.attribute_list)

        number_view.apply {
            alwaysEnableCut = false
            MIN_GOOD_NUM = 1
            cut_iv.setImageResource(R.mipmap.ic_cut_blue)
            add_iv.setImageResource(R.mipmap.ic_add_blue)
            updateNumberJudgeMin(1)
            cut_iv.visibility = View.VISIBLE
            valueChangeListener = object : NumberView2.OnValueChangeListener {
                override fun onNumberChange(goodNum: Int, isAdd: Boolean) {

                }

            }
        }

    }

    override fun initData() {
        presenter = MallSkuPresenter(this)
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            dismiss()
        }
    }
}