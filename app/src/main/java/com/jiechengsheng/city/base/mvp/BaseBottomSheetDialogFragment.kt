package com.jiechengsheng.city.base.mvp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.dialog.LoadingView
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Wangsw  2021/10/12 10:16.
 * 底部弹出
 */
abstract class BaseBottomSheetDialogFragment<T : BaseMvpPresenter> : BottomSheetDialogFragment(), BaseMvpView {

    lateinit var presenter: T


    protected var isViewCreated = false
    protected var isViewVisible = false
    protected var hasLoad = false
    private var lastClickTime = 0L
    private var clicked: HashSet<Int>? = null


    private  var loadingDialog: LoadingView? = null

    abstract fun getLayoutId(): Int
    abstract fun initView(parent: View)
    abstract fun initData()
    abstract fun bindListener()

    protected open fun loadOnVisible() = Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置背景透明，显示出圆角的布局，否则会有白色底（框）
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogThemeLight)

    }

/*    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 展开高度
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog?
            val bottomSheet = dialog!!.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior: BottomSheetBehavior<FrameLayout> = BottomSheetBehavior.from<FrameLayout>(bottomSheet)
            // behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight =  SizeUtils.dp2px(500f)
        }
        super.onViewCreated(view, savedInstanceState)
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        isViewCreated = true
        val eventBus = EventBus.getDefault()
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this)
        }
        initView(view)
        initData()
        bindListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lazyLoad()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isViewVisible = userVisibleHint
        lazyLoad()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val eventBus = EventBus.getDefault()
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this)
        }
        presenter?.detachView()
    }

    protected fun <T> startActivity(target: Class<T>) {
        startActivity(Intent(requireContext().applicationContext, target))
    }

    protected fun <T> startActivity(target: Class<T>, bundle: Bundle) {
        startActivity(Intent(requireContext().applicationContext, target).putExtras(bundle))
    }


    protected fun <T> startActivityForResultAfterLogin(target: Class<T>, bundle: Bundle, requestCode: Int) {
        if (User.isLogon()) {
            startActivityForResult(Intent(requireContext().applicationContext, target).putExtras(bundle), requestCode)
        } else {
            startActivity(LoginActivity::class.java)
        }
    }

    protected fun <T> startActivityAfterLogin(target: Class<T>) {
        if (User.isLogon()) {
            startActivity(target)
        } else {
            startActivity(LoginActivity::class.java)
        }
    }


    protected fun <T> startActivityWithArgument(target: Class<T>, key: String, value: Any) {
        val bundle = Bundle()
        when (value) {
            is String -> bundle.putString(key, value)
            is Long -> bundle.putLong(key, value)
            is Int -> bundle.putInt(key, value)
        }
        startActivity(target, bundle)
    }


    protected fun <T> startActivityAfterLogin(target: Class<T>, bundle: Bundle) {
        if (User.isLogon()) {
            startActivity(target, bundle)
        } else {
            startActivity(LoginActivity::class.java)
        }
    }


    protected fun onOnceClick(view: View): Boolean {
        if (clicked == null) {
            clicked = HashSet()
        }
        if (clicked!!.contains(view.id) && (System.currentTimeMillis() - lastClickTime) < Constant.CLICK_INTERVAL) {
            return false
        }
        lastClickTime = System.currentTimeMillis()
        clicked!!.add(view.id)
        return true
    }

    private fun lazyLoad() {
        if (!hasLoad && isViewVisible && isViewCreated) {
            hasLoad = true
            loadOnVisible()
        }
    }

    override fun onError(errorResponse: ErrorResponse?) {
        val errCode = errorResponse?.errCode
        val errMsg = errorResponse?.errMsg
        if (errCode!! <= 0) {
            ToastUtils.showShort(errMsg)
            return
        }
        if (errMsg.isNullOrBlank()) {
            ToastUtils.showShort(getString(R.string.request_error, errCode))
        } else {
            ToastUtils.showShort(errMsg)
        }
    }


    protected open fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingView.Builder(context).setCancelable(true).create()
        }
        loadingDialog?.let {
            if (it.isShowing) {
                it.show()
            }
        }


    }


    protected open fun showLoadingDialog(cancelable: Boolean) {
        if (loadingDialog == null) {
            loadingDialog = LoadingView.Builder(context).setCancelable(true).create()
        }
        loadingDialog?.let {
            if (it.isShowing) {
                it.show()
            }
        }
    }

    protected open fun dismissLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEventReceived(baseEvent: BaseEvent<*>) {


    }


    override fun showLoading() {
        showLoadingDialog()
    }

    override fun hideLoading() {
        dismissLoadingDialog()
    }


}