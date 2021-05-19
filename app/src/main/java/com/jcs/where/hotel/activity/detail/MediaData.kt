package com.jcs.where.hotel.activity.detail

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by Wangsw  2021/5/19 14:34.
 *
 */
class MediaData : MultiItemEntity {




    override val itemType: Int get() = type

    /**
     * 0 视频
     * 1 图片
     */
    var type = 0

    /**
     * 视频或图片地址
     */
    var src = ""

    /**
     * 视频封面
     */
    var cover = ""

    companion object {
        @kotlin.jvm.JvmField
        var VIDEO: Int = 0

        @kotlin.jvm.JvmField
        val IMAGE = 1
    }


}