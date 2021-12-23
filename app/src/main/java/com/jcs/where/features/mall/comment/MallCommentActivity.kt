package com.jcs.where.features.mall.comment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.StringUtils
import com.google.android.material.radiobutton.MaterialRadioButton
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_hotel_comment_2.*

import java.util.*

/**
 * Created by Wangsw  2021/12/23 13:57.
 * 商城评价
 */
class MallCommentActivity : BaseMvpActivity<MallCommentPresenter>(), MallCommentView {

    val TAB_TITLES =
        arrayOf(
            StringUtils.getString(R.string.all),
            StringUtils.getString(R.string.picture),
            StringUtils.getString(R.string.newest_default),
            StringUtils.getString(R.string.low_rating)
        )

    private var goodId = 0

    override fun isStatusDark(): Boolean = true


    override fun getLayoutId() = R.layout.activity_hotel_comment_2

    override fun initView() {
        goodId = intent.getIntExtra(Constant.PARAM_ID, 0)

        bad_reviews_rb.text = StringUtils.getString(R.string.newest_default)
        high_option_rb.text = StringUtils.getString(R.string.low_rating)


        pager.offscreenPageLimit = TAB_TITLES.size
        pager.adapter = InnerPagerAdapter(supportFragmentManager, 0)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                val rb = sort_rg.getChildAt(position) as MaterialRadioButton
                rb.isChecked = true
            }

        })

    }

    override fun initData() {
        presenter = MallCommentPresenter(this)
        presenter.getCommentCount(goodId)
    }

    override fun bindListener() {
        sort_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.all_rb -> {
                    pager.currentItem = 0
                }
                R.id.picture_rb -> {
                    pager.currentItem = 1
                }
                R.id.bad_reviews_rb -> {
                    pager.currentItem = 2
                }
                R.id.high_option_rb -> {
                    pager.currentItem = 3
                }


                else -> {
                }
            }


        }

    }

    override fun bindCount(response: ArrayList<Int>) {
        if (response.size >= 4) {
            all_rb.text = getString(R.string.all_format, response[0])
            picture_rb.text = getString(R.string.has_image_format, response[1])
            bad_reviews_rb.text = getString(R.string.newest_format, response[2])
            high_option_rb.text = getString(R.string.low_rating_with_number, response[3])
        }
    }

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]


        override fun getItem(position: Int): Fragment =
            MallCommentFragment.newInstance(goodId, position)

        override fun getCount(): Int = TAB_TITLES.size
    }

}

interface MallCommentView : BaseMvpView {
    fun bindCount(response: ArrayList<Int>)

}

class MallCommentPresenter(private var view: MallCommentView) : BaseMvpPresenter(view) {

    fun getCommentCount(goodId: Int) {

        requestApi(mRetrofit.mallCommentCount(goodId), object : BaseMvpObserver<ArrayList<Int>>(view) {
            override fun onSuccess(response: ArrayList<Int>) {
                view.bindCount(response)
            }

        })
    }

}