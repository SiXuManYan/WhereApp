package com.jiechengsheng.city.features.main


import android.content.Intent
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.MtjDuration
import com.jiechengsheng.city.api.response.home.TabEntity
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.category.CategoryFragment2
import com.jiechengsheng.city.features.home.AppBarStateChanged
import com.jiechengsheng.city.features.home.HomeFragment
import com.jiechengsheng.city.features.mine.MineFragment
import com.jiechengsheng.city.features.order.parent.OrderFragment
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.SPKey
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by Wangsw  2021/8/12 15:25.
 *
 */
class MainActivity : BaseMvpActivity<MainPresenter>(), MainView, AppBarStateChanged {

    private var mTapTime = 0L

    private val frList = ArrayList<Fragment>()

    /** Tab数据 */

    private var tabIndex = 0

    private val tabs = ArrayList<CustomTabEntity>()

    private lateinit var homeTab: TabEntity

    private var isTopStyle = false

    override fun getLayoutId() = R.layout.activity_main

    override fun initView() = initTabs()

    override fun initData() {
        presenter = MainPresenter(this)
    }

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
                EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_SCROLL_TO_TOP))
            }
        }
    }



    private fun initTabs() {

        tabs.apply {
            homeTab = TabEntity(getString(R.string.main_tab_title_home), R.mipmap.ic_home_press, R.mipmap.ic_home_normal)
            add(homeTab)
            add(TabEntity(getString(R.string.main_tab_title_category),
                R.mipmap.ic_home_category_selected,
                R.mipmap.ic_home_category_normal))
            add(TabEntity(getString(R.string.main_tab_title_order), R.mipmap.ic_home_order_selected, R.mipmap.ic_home_order_normal))
            add(TabEntity(getString(R.string.main_tab_title_mine), R.mipmap.ic_mine_press, R.mipmap.ic_mine_normal))
        }

        frList.apply {
            val homeFragment = HomeFragment()
            homeFragment.appBarStateChangeListener = this@MainActivity
            add(homeFragment)
            add(CategoryFragment2())
            add(OrderFragment())
            add(MineFragment())
        }

        tabs_navigator.setTabData(tabs, this, R.id.fl_content, frList)

        tabs_navigator.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                tabs_navigator.currentTab = position
                val titleView = tabs_navigator.getTitleView(0)

                if (isTopStyle || position == 0) {
                    titleView.setText(R.string.main_tab_title_home_top)
                } else {
                    titleView.setText(R.string.main_tab_title_home)
                }

            }

            override fun onTabReselect(position: Int) {
                if (position == 0) {
                    EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_SCROLL_TO_TOP))
                }
            }
        })

        val languageTabIndex = SPUtils.getInstance().getInt(SPKey.K_LANGUAGE_TAB, 0)
        if (languageTabIndex > 0) {
            tabs_navigator.currentTab = languageTabIndex
            SPUtils.getInstance().put(SPKey.K_LANGUAGE_TAB, 0)
        }

    }


    private fun setTopStyle(isTopStyle: Boolean) {
        if (isTopStyle) {
            homeTab.selectedResId = R.mipmap.ic_home_press_top
            homeTab.titleStr = getString(R.string.main_tab_title_home_top)
        } else {
            homeTab.selectedResId = R.mipmap.ic_home_press
            homeTab.titleStr = getString(R.string.main_tab_title_home)
        }
        tabs_navigator.notifyDataSetChanged()
    }

    override fun scrolling() = setTopStyle(true)

    override fun expanded() = setTopStyle(false)

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        val code = baseEvent.code

        when (code) {
            EventCode.EVENT_REFRESH_LANGUAGE  -> {
                SPUtils.getInstance().put(SPKey.K_LANGUAGE_TAB, 3)
                recreate()
            }
            EventCode.EVENT_MTJ_DURATION ->{
                val mtjDuration = baseEvent.data as MtjDuration
                presenter.mtjDuration(mtjDuration)

            }
            else -> {}
        }


    }



}