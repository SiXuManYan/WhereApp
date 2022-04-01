package com.jcs.where.features.main


import android.content.Intent
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.jcs.where.R
import com.jcs.where.api.response.home.TabEntity
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.category.CategoryFragment2
import com.jcs.where.features.home.HomeFragment
import com.jcs.where.features.mine.MineFragment
import com.jcs.where.features.order.parent.OrderFragment
import com.jcs.where.utils.Constant
import com.jcs.where.utils.SPKey
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 * Created by Wangsw  2021/8/12 15:25.
 *
 */
class MainActivity : BaseMvpActivity<MainPresenter>(), MainView {

    private var mTapTime = 0L

    private val frList = ArrayList<Fragment>()

    /** Tab数据 */
    private val tabs = ArrayList<CustomTabEntity>()

    override fun getLayoutId() = R.layout.activity_main

    override fun initView() = initTabs()

    override fun initData() = Unit

    override fun bindListener() = Unit

    override fun onBackPressed() {

        if (System.currentTimeMillis() - mTapTime > 2000) {
            ToastUtils.showShort(getString(R.string.main_back_hint))
            mTapTime = System.currentTimeMillis()
        } else {
            // 保留应用状态
            moveTaskToBack(false)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val bundle = intent.extras
        if (bundle != null) {
            val tabIndex = bundle.getInt(Constant.PARAM_TAB, 0)
            tabs_navigator.currentTab = tabIndex
            if (tabIndex == 2) {
                EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
            }
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        val code = baseEvent.code
//        if (code == EventCode.EVENT_SIGN_OUT) {
//            finish()
//        }
        if (code == EventCode.EVENT_REFRESH_LANGUAGE) {
            SPUtils.getInstance().put(SPKey.K_LANGUAGE_TAB, 3)
            recreate()
        }


    }

    private fun initTabs() {

        tabs.apply {
            add(
                TabEntity(
                    getString(R.string.main_tab_title_home),
                    R.mipmap.ic_home_press, R.mipmap.ic_home_normal
                )
            )
            add(
                TabEntity(
                    getString(R.string.main_tab_title_category),
                    R.mipmap.ic_home_category_selected, R.mipmap.ic_home_category_normal
                )
            )
            add(
                TabEntity(
                    getString(R.string.main_tab_title_order),
                    R.mipmap.ic_home_order_selected, R.mipmap.ic_home_order_normal
                )
            )
            add(
                TabEntity(
                    getString(R.string.main_tab_title_mine),
                    R.mipmap.ic_mine_press, R.mipmap.ic_mine_normal
                )
            )
        }

        frList.apply {
            add(HomeFragment())
            add(CategoryFragment2())
            add(OrderFragment())
            add(MineFragment())
        }

        tabs_navigator.setTabData(tabs, this, R.id.fl_content, frList)

        tabs_navigator.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                tabs_navigator.currentTab = position
            }

            override fun onTabReselect(position: Int) = Unit
        })

        val languageTabIndex = SPUtils.getInstance().getInt(SPKey.K_LANGUAGE_TAB, 0)
        if (languageTabIndex > 0) {
            tabs_navigator.currentTab = languageTabIndex
            SPUtils.getInstance().put(SPKey.K_LANGUAGE_TAB, 0)
        }

    }


}