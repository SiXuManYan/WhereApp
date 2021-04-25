package com.jcs.where.features.gourmet.takeaway

import androidx.core.content.ContextCompat
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.takeaway.TakeawayDetailResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_take_away.*

/**
 * Created by Wangsw  2021/4/25 10:04.
 * 外卖详情
 */
class TakeawayActivity : BaseMvpActivity<TakeawayPresenter>(), TakeawayView {


    private lateinit var restaurant_id: String
    private var like = 1
    private var toolbarStatus = 0

    override fun getLayoutId() = R.layout.activity_take_away

    override fun initView() {

        val id = intent.getStringExtra(Constant.PARAM_ID)
        if (id.isNullOrBlank()) {
            finish()
            return
        }
        restaurant_id = id

        initScroll()


    }

    private fun initScroll() {
        useView.background.alpha = 0
        toolbar.background.alpha = 0
        scrollView.setOnScrollChangeListener { _, _, y, _, _ ->
            val headHeight = image_iv.measuredHeight - toolbar.measuredHeight
            var alpha = (y.toFloat() / headHeight * 255).toInt()
            if (alpha >= 255) {
                alpha = 255
            }
            if (alpha <= 5) {
                alpha = 0
            }
            if (alpha > 130) {
                setLikeImage()
                shareIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_share_black))
                back_iv.setImageResource(R.drawable.ic_back_black)
                toolbarStatus = 1
            } else {
                setLikeImage()
                shareIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_share_white))
                back_iv.setImageResource(R.drawable.ic_back_white)
                toolbarStatus = 0
            }
            useView.background.alpha = alpha
            toolbar.background.alpha = alpha
            if (alpha == 255) {
                titleTv.text = restaurant_tv.text
            }
            if (alpha == 0) {
                titleTv.text = ""
            }
            changeStatusTextColor()
        }


    }

    private fun setLikeImage() {
        if (like == 1) {
            likeIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hotelwhitelike))
        } else {
            likeIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hotelwhiteunlike))
        }
    }

    override fun initData() {


        presenter = TakeawayPresenter(this)
        presenter.getDetailData(restaurant_id)

    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }

        shareIv.setOnClickListener {

        }
        likeIv.setOnClickListener {

        }


    }

    override fun bindData(data: TakeawayDetailResponse) {
        GlideUtil.load(this, data.take_out_image, image_iv)
        like = data.collect_status
        setLikeImage()
        restaurant_tv.text = data.restaurant_name
        time_tv.text = getString(R.string.delivery_time_format, data.delivery_time)
        spread_tv.text = getString(R.string.spread_format, data.delivery_cost.toPlainString())

    }

}