package com.jiechengsheng.city.features.job.company

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2022/11/2 16:31.
 *
 */
class CompanyPhotoAdapter : BaseMultiItemQuickAdapter<CompanyPhoto, BaseViewHolder>() {

    init {
        addItemType(CompanyPhoto.HORIZONTAL_IMAGE, R.layout.item_company_photo)
        addItemType(CompanyPhoto.HORIZONTAL_IMAGE_LOOK_MORE, R.layout.item_company_photo_more)
    }

    override fun convert(holder: BaseViewHolder, item: CompanyPhoto) {
        when (holder.itemViewType) {

            CompanyPhoto.HORIZONTAL_IMAGE -> {

                val image = holder.getView<ImageView>(R.id.company_image_iv)

                val currentPosition = holder.adapterPosition
                val layoutParams = image.layoutParams as RecyclerView.LayoutParams
                if (currentPosition == 0) {
                    layoutParams.marginStart = SizeUtils.dp2px(15f)
                } else {
                    layoutParams.marginStart = 0
                }

                GlideUtil.load(context, item.src, image, 4)
//                val photos = ArrayList<String>()
//                data.forEach {
//                    photos.add(it.src)
//                }
            }

            CompanyPhoto.HORIZONTAL_IMAGE_LOOK_MORE -> {
            }
            else -> {}
        }
    }
}


class CompanyPhoto : MultiItemEntity {

    companion object {


        /** 横向图片 */
        var HORIZONTAL_IMAGE = 0

        /** 查看更多 */
        var HORIZONTAL_IMAGE_LOOK_MORE = 1

    }

    var src = ""
    var type = 0

    override val itemType: Int
        get() = type
}


class CompanyAlbumAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_company_album) {


    var screenWidth = ScreenUtils.getScreenWidth()
    var itemWidth = (screenWidth - SizeUtils.dp2px(16f)) / 3

    override fun convert(holder: BaseViewHolder, item: String) {

        val imageIv = holder.getView<ImageView>(R.id.image_iv)

        // 重置宽高
        val layoutParams = imageIv.layoutParams as RecyclerView.LayoutParams
        layoutParams.height = itemWidth
        imageIv.layoutParams = layoutParams

        GlideUtil.load(context, item, imageIv)

    }

}