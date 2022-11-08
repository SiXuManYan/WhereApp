package com.jcs.where.features.media

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.jcs.where.R
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.hotel.detail.media.DetailMediaAdapter
import com.jcs.where.features.hotel.detail.media.MediaData
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.activity_media_detail.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/8/23 14:13.
 * 媒体详情页
 */
class MediaDetailActivity : BaseMvpActivity<MediaDetailPresenter>(), MediaDetailView {

    private var selectedPosition = 0
    private var source = ArrayList<MediaData>()

    private lateinit var mMediaAdapter: DetailMediaAdapter
    private var numberType = false

    override fun getLayoutId() = R.layout.activity_media_detail

    companion object {

        fun navigationOnlyStringImage(
            context: Context,
            currentPosition: Int,
            data: MutableList<String>,
            numberType: Boolean? = false,
        ) {
            if (data.isEmpty()) {
                return
            }
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_POSITION, currentPosition)
                val mediaList = ArrayList<MediaData>()
                data.forEach {
                    val media = MediaData()
                    media.type = MediaData.IMAGE
                    media.cover = it
                    media.src = it
                    mediaList.add(media)
                }
                putSerializable(Constant.PARAM_DATA, ArrayList<MediaData>(mediaList))
                numberType?.let {
                    putBoolean(Constant.PARAM_TYPE, it)
                }
            }
            val intent = Intent(context, MediaDetailActivity::class.java)
                .putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }


        /**
         * 视频 + 图片
         */
        fun navigationAllMediaType(context: Context, currentPosition: Int, data: MutableList<MediaData>) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_POSITION, currentPosition)
                putSerializable(Constant.PARAM_DATA, ArrayList<MediaData>(data))
            }
            val intent = Intent(context, MediaDetailActivity::class.java)
                .putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        /**
         * 只展示图片
         */
        fun navigationOnlyImage(context: Context, currentPosition: Int, data: MutableList<MediaData>) {

            // 去除视频，调整坐标
            var position = currentPosition

            val allImage = BusinessUtils.getAllImage(data)
            if (allImage.size < data.size) {
                // 去除了视频
                position -= 1
            }

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_POSITION, position)
                putSerializable(Constant.PARAM_DATA, allImage)
            }
            val intent = Intent(context, MediaDetailActivity::class.java)
                .putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }


        /**
         * 只展示视频
         */
        fun navigationOnlyVideo(context: Context, data: MutableList<MediaData>) {

            val allImage = BusinessUtils.getFirstVideo(data)

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_POSITION, 0)
                putSerializable(Constant.PARAM_DATA, allImage)
            }
            val intent = Intent(context, MediaDetailActivity::class.java)
                .putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

    }

    override fun initView() {
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent)
        BarUtils.setStatusBarVisibility(this, false)
        BarUtils.setNavBarColor(this, ColorUtils.getColor(R.color.black_000000))
        initExtra()
        initMedia()
    }

    private fun initExtra() {
        intent.extras?.let { bundle ->
            val arrayList = bundle.getSerializable(Constant.PARAM_DATA) as ArrayList<MediaData>
            arrayList.forEach {
                if (it.type == MediaData.IMAGE) {
                    it.type = MediaData.IMAGE_FOR_MEDIA_DETAIL
                }
                if (it.type == MediaData.VIDEO) {
                    it.type = MediaData.VIDEO_FOR_MEDIA_DETAIL
                }
            }

            source.addAll(arrayList)

            selectedPosition = bundle.getInt(Constant.PARAM_POSITION, 0)

            numberType = bundle.getBoolean(Constant.PARAM_TYPE, false)
            if (numberType) {
                number_position_tv.visibility = View.VISIBLE
                point_view.visibility = View.GONE
            } else {
                number_position_tv.visibility = View.GONE
                number_position_tv.visibility = View.VISIBLE
            }

        }
    }

    override fun initData() {
        presenter = MediaDetailPresenter(this)
    }

    override fun bindListener() = Unit

    private fun initMedia() {
        mMediaAdapter = DetailMediaAdapter()
        PagerSnapHelper().attachToRecyclerView(media_rv)

        media_rv.apply {
            layoutManager = LinearLayoutManager(this@MediaDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mMediaAdapter
        }

        media_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastItemPosition = layoutManager.findLastVisibleItemPosition()

                if (GSYVideoManager.instance().playPosition >= 0) {
                    val position = GSYVideoManager.instance().playPosition
                    if (GSYVideoManager.instance().playTag == DetailMediaAdapter.TAG && (position < firstItemPosition || position > lastItemPosition)) {
                        if (GSYVideoManager.isFullState(this@MediaDetailActivity)) {
                            return
                        }
                        GSYVideoManager.releaseAllVideos()
                        mMediaAdapter.notifyDataSetChanged()
                    }
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    point_view.onPageSelected(firstItemPosition)
                    number_position_tv.text = getString(R.string.image_index, firstItemPosition+1, source.size)

                    selectedPosition = firstItemPosition
                }
            }
        })

        mMediaAdapter.setNewInstance(source)

        point_view.apply {
            commonDrawableResId = R.drawable.shape_point_normal_e7e7e7
            selectedDrawableResId = R.drawable.shape_point_selected_377bff
            setPointCount(source.size)
        }
        media_rv.scrollToPosition(selectedPosition)
        point_view.onPageSelected(selectedPosition)
        number_position_tv.text = getString(R.string.image_index, selectedPosition + 1, source.size)

    }


    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }


    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        EventBus.getDefault().post(BaseEvent<Int>(EventCode.EVENT_POSITION, selectedPosition))
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out)
    }

}