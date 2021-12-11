package com.jcs.where.features.mall.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallGoodDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_mall_good_detail.*
import android.graphics.drawable.Drawable

import android.text.Html.ImageGetter
import android.text.method.LinkMovementMethod
import androidx.core.text.HtmlCompat.fromHtml
import java.lang.Exception
import java.net.URL


/**
 * Created by Wangsw  2021/12/10 14:57.
 * 商品详情
 */
class MallDetailActivity : BaseMvpActivity<MallDetailPresenter>(), MallDetailView {

    private var goodId = 0


    companion object {

        fun navigation(context: Context, categoryId: Int) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, categoryId)
            }
            val intent = Intent(context, MallDetailActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_good_detail

    override fun initView() {
        goodId = intent.getIntExtra(Constant.PARAM_ID, 0)
    }

    override fun initData() {
        presenter = MallDetailPresenter(this)
        presenter.getDetail(goodId)
    }

    override fun bindListener() {

    }

    override fun bindDetail(response: MallGoodDetail) {
        html_tv.movementMethod = LinkMovementMethod.getInstance()
        html_tv.text = Html.fromHtml(response.desc,imgGetter,null)
    }


    var imgGetter = Html.ImageGetter { source ->

        var drawable: Drawable? = null
        val url: URL
        try {
            url = URL(source)

            drawable = Drawable.createFromStream(url.openStream(), "") // 获取网路图片
        } catch (e: Exception) {
            e.printStackTrace()
            return@ImageGetter null
        }
        drawable.setBounds(
            0, 0, drawable.intrinsicWidth,
            drawable.intrinsicHeight
        )

        drawable
    }


}