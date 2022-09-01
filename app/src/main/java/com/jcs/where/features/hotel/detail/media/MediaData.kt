package com.jcs.where.features.hotel.detail.media

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/**
 * Created by Wangsw  2021/5/19 14:34.
 *
 */
class MediaData : MultiItemEntity, Serializable {


    override val itemType: Int get() = type

    /**
     * 0 视频
     * 1 图片
     * 2.可伸缩图片
     */
    var type = 0

    /**
     * 视频地址
     */
    var src = ""

    /**
     * 视频封面 或图片地址
     */
    var cover = ""


    companion object {

        @kotlin.jvm.JvmField
        var VIDEO: Int = 0

        @kotlin.jvm.JvmField
        val IMAGE = 1

        @kotlin.jvm.JvmField
        val IMAGE_FOR_MEDIA_DETAIL = 2

        @kotlin.jvm.JvmField
        val VIDEO_FOR_MEDIA_DETAIL = 3

    }


}