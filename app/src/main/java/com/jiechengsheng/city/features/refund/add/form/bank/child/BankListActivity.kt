package com.jiechengsheng.city.features.refund.add.form.bank.child

import android.content.Intent
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.mall.RefundBankSelected
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_bank_list.*

/**
 * Created by Wangsw  2022/4/26 10:25.
 * 选择银行卡
 */
@Deprecated("新版绑定退款渠道不必区分银行和第三方")
class BankListActivity : BaseMvpActivity<BankListPresenter>(), BankListView {


    private var lastSelectedBankShortName = ""
    private lateinit var mAdapter: BankAdapter
    private lateinit var emptyView: EmptyView

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_bank_list

    override fun initView() {
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent)

        initExtra()
        initContent()

    }

    private fun initExtra() {

        intent.extras?.let {
            lastSelectedBankShortName = it.getString(Constant.PARAM_BANK_SHORT_NAME, "")
        }

    }

    private fun initContent() {
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            addEmptyList(this)
        }
        mAdapter = BankAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener { _, _, position ->
                val bank = mAdapter.data[position]
                setResult(RESULT_OK, Intent()
                    .putExtra(Constant.PARAM_BANK_ALL_NAME, bank.all)
                    .putExtra(Constant.PARAM_BANK_SHORT_NAME, bank.abbr))
                finish()

            }
        }
        bank_rv.adapter = mAdapter
    }

    override fun initData() {
        presenter = BankListPresenter(this)
        presenter.getBankList(lastSelectedBankShortName)
    }

    override fun bindListener() = Unit


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out)
    }

    override fun bindBank(response: ArrayList<RefundBankSelected>) {
        if (response.isNullOrEmpty()) {
            emptyView.showEmptyContainer()
        }
        mAdapter.setNewInstance(response)
    }

}

@Deprecated("新版绑定退款渠道不必区分银行和第三方")
interface BankListView : BaseMvpView {
    fun bindBank(response: ArrayList<RefundBankSelected>)
}

@Deprecated("新版绑定退款渠道不必区分银行和第三方")
class BankListPresenter(private var view: BankListView) : BaseMvpPresenter(view) {
    fun getBankList(lastSelectedBankShortName: String) {
        requestApi(mRetrofit.bankList, object : BaseMvpObserver<ArrayList<RefundBankSelected>>(view) {
            override fun onSuccess(response: ArrayList<RefundBankSelected>) {
                response.forEach {
                    if (it.abbr == lastSelectedBankShortName) {
                        it.isSelected = true
                    }
                }
                view.bindBank(response)
            }

        })
    }

}
