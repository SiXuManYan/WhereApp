package com.jcs.where.features.coupon.user.child

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jcs.where.R
import com.jcs.where.api.response.UserCoupon
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.coupon.good.CouponGoodActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2022/3/2 15:09.
 *
 */
class MyCouponFragment : BaseMvpFragment<MyCouponPresenter>(), MyCouponView {


    /** type 类型  1未使用 2 已使用 3已过期 */
    var type = 0

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: MyCouponAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View?) {
        swipe_layout.apply {

            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                loadOnVisible()
            }
        }

        emptyView = EmptyView(requireContext()).apply {
            initEmpty(R.mipmap.ic_empty_card_coupon, R.string.no_coupon)
        }

        mAdapter = MyCouponAdapter().apply {
            setEmptyView(emptyView)
            addChildClickViewIds(R.id.rule_tv, R.id.use_tv)
            setOnItemChildClickListener(this@MyCouponFragment)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                loadOnVisible()
            }

        }

        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0))
        }

    }

    override fun initData() {
        presenter = MyCouponPresenter(this)
    }

    override fun bindListener() = Unit

    override fun loadOnVisible() {
        presenter.getData(page, type)
    }

    override fun bindData(data: MutableList<UserCoupon>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }

        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(data)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(data)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val userCoupon = mAdapter.data[position]

        when (view.id) {
            R.id.rule_tv -> {
                showRule(userCoupon.rule)
            }
            R.id.use_tv -> {
                startActivity(CouponGoodActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ID, userCoupon.id)
                })
            }
            else -> {}
        }
    }

    private fun showRule(rule: String) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_coupon_rule, null, false)
        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()

        val title_tv = view.findViewById<TextView>(R.id.title_tv)
        val content_tv = view.findViewById<TextView>(R.id.content_tv)
        val contconfirm_tvent_tv = view.findViewById<TextView>(R.id.confirm_tv)

        title_tv.setText(R.string.rules_of_use)
        content_tv.text = rule
        contconfirm_tvent_tv.setText(R.string.sure)
        contconfirm_tvent_tv.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setLayout(ScreenUtils.getScreenWidth() / 10 * 9, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}