package com.jcs.where.features.mall.detail

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.MallGoodDetail
import com.jcs.where.api.response.mall.request.MallAddCart
import com.jcs.where.api.response.mall.request.MallCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Wangsw  2021/12/10 14:57.
 *
 */
interface MallDetailView : BaseMvpView {
    fun bindDetail(response: MallGoodDetail)
    fun collectionHandleSuccess(collectionStatus: Boolean)
}

class MallDetailPresenter(private var view: MallDetailView) : BaseMvpPresenter(view) {

    fun getDetail(goodId: Int) {

        requestApi(mRetrofit.getMallGoodDetail(goodId), object : BaseMvpObserver<MallGoodDetail>(view) {
            override fun onSuccess(response: MallGoodDetail) {
                view.bindDetail(response)
            }
        })
    }


    fun collection(shopId: Int) {
        val request = MallCollection().apply {
            shop_id = shopId
        }

        requestApi(mRetrofit.collectsMallShop(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(true)
            }
        })
    }

    fun unCollection(shopId: Int) {
        val request = MallCollection().apply {
            shop_id = shopId
        }
        requestApi(mRetrofit.unCollectsMallShop(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(false)
            }
        })
    }


    fun addCart(goodId: Int, goodNumber: Int, specsId: Int) {
        val apply = MallAddCart().apply {
            good_id = goodId
            good_num = goodNumber
            specs_id = specsId
        }
        requestApi(mRetrofit.mallAddCart(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                ToastUtils.showShort("add success")
            }

        })
    }


}

class ImageGetter(
    private var context: Context,
    private val res: Resources,
    private val htmlTextView: TextView
) : Html.ImageGetter {

    // Function needs to overridden when extending [Html.ImageGetter] , which will download the image
    override fun getDrawable(url: String): Drawable {

        val holder = BitmapDrawablePlaceHolder(res, null)

        // Coroutine Scope to download image in Background
        GlobalScope.launch(Dispatchers.IO) {
            runCatching {

                val bitmap = Glide.with(context).asBitmap().load(url).submit().get()

                val drawable = BitmapDrawable(res, bitmap)

                // 为了确保图像不会超出屏幕，设置宽度小于屏幕宽度，您可以根据需要更改图像大小
                val width = getScreenWidth() - 150

                // Images may stretch out if you will only resize width,
                // hence resize height to according to aspect ratio
                val aspectRatio: Float =
                    (drawable.intrinsicWidth.toFloat()) / (drawable.intrinsicHeight.toFloat())
                val height = width / aspectRatio
                drawable.setBounds(10, 20, width, height.toInt())
                holder.setDrawable(drawable)
                holder.setBounds(10, 20, width, height.toInt())
                withContext(Dispatchers.Main) {
                    htmlTextView.text = htmlTextView.text
                }
            }
        }
        return holder
    }

    // Actually Putting images
    internal class BitmapDrawablePlaceHolder(res: Resources, bitmap: Bitmap?) :
        BitmapDrawable(res, bitmap) {
        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.run { draw(canvas) }
        }

        fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
        }
    }

    // Function to get screenWidth used above
    fun getScreenWidth() =
        Resources.getSystem().displayMetrics.widthPixels
}


class QuoteSpanClass(
    private val backgroundColor: Int,
    private val stripeColor: Int,
    private val stripeWidth: Float,
    private val gap: Float
) : LeadingMarginSpan, LineBackgroundSpan {

    // Margin for the block quote tag
    override fun getLeadingMargin(first: Boolean): Int {
        return (stripeWidth + gap).toInt()
    }

    // this function draws the margin.
    override fun drawLeadingMargin(
        c: Canvas,
        p: Paint,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout
    ) {

        val style = p.style
        val paintColor = p.color
        p.style = Paint.Style.FILL
        p.color = stripeColor

        // Creating margin according to color and stripewidth it recieves
        // Press CTRL+Q on function name to read more
        c.drawRect(x.toFloat(), top.toFloat(), x + dir * stripeWidth, bottom.toFloat(), p)
        p.style = style
        p.color = paintColor
    }

    override fun drawBackground(
        c: Canvas,
        p: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lnum: Int
    ) {
        val paintColor = p.color
        p.color = backgroundColor

        // It draws the background on which blockquote text is written
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), p)
        p.color = paintColor
    }
}