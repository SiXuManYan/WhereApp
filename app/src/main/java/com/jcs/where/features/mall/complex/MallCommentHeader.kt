package com.jcs.where.features.mall.complex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.forEach
import com.blankj.utilcode.util.ResourceUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCommentCount

/**
 * Created by Wangsw  2022/5/19 15:47.
 *
 */
class MallCommentHeader(context: Context?) : LinearLayout(context) {

    init {
        initView()
    }

    private var containerList = ArrayList<LinearLayout>()
    private var starContainerList = ArrayList<LinearLayout>()

    var containerClickListener: CommentHeaderClickListener? = null

    lateinit var all_ll: LinearLayout
    lateinit var newest_ll: LinearLayout
    lateinit var has_image_ll: LinearLayout

    lateinit var star_five_ll: LinearLayout
    lateinit var star_four_ll: LinearLayout
    lateinit var star_three_ll: LinearLayout
    lateinit var star_two_ll: LinearLayout
    lateinit var star_one_ll: LinearLayout

    lateinit var all_num_tv: CheckedTextView
    lateinit var newest_num_tv: CheckedTextView
    lateinit var has_image_num_tv: CheckedTextView

    lateinit var star_five_num_tv: CheckedTextView
    lateinit var star_four_num_tv: CheckedTextView
    lateinit var star_three_num_tv: CheckedTextView
    lateinit var star_two_num_tv: CheckedTextView
    lateinit var star_one_num_tv: CheckedTextView

    private fun initView() {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_mall_comment_header, this, true)

        all_ll = view.findViewById(R.id.all_ll)
        newest_ll = view.findViewById(R.id.newest_ll)
        has_image_ll = view.findViewById(R.id.has_image_ll)

        star_five_ll = view.findViewById(R.id.star_five_ll)
        star_four_ll = view.findViewById(R.id.star_four_ll)
        star_three_ll = view.findViewById(R.id.star_three_ll)
        star_two_ll = view.findViewById(R.id.star_two_ll)
        star_one_ll = view.findViewById(R.id.star_one_ll)


        all_num_tv = view.findViewById(R.id.all_num_tv)
        newest_num_tv = view.findViewById(R.id.newest_num_tv)
        has_image_num_tv = view.findViewById(R.id.has_image_num_tv)


        star_five_num_tv = view.findViewById(R.id.star_five_num_tv)
        star_four_num_tv = view.findViewById(R.id.star_four_num_tv)
        star_three_num_tv = view.findViewById(R.id.star_three_num_tv)
        star_two_num_tv = view.findViewById(R.id.star_two_num_tv)
        star_one_num_tv = view.findViewById(R.id.star_one_num_tv)


    }

    fun initData(isDiamond: Boolean) {

        containerList.add(all_ll)
        containerList.add(newest_ll)
        containerList.add(has_image_ll)
        containerList.add(star_five_ll)
        containerList.add(star_four_ll)
        containerList.add(star_three_ll)
        containerList.add(star_two_ll)
        containerList.add(star_one_ll)

        // star
        starContainerList.add(star_five_ll)
        starContainerList.add(star_four_ll)
        starContainerList.add(star_three_ll)
        starContainerList.add(star_two_ll)
        starContainerList.add(star_one_ll)

        setClick()
        if (isDiamond) {
            starContainerList.forEach {
                it.forEach { child ->
                    if (child is LinearLayout) {
                        child.forEach { image ->
                            if (image is ImageView) {
                                image.setImageResource(R.mipmap.ic_comment_diamond_yellow)
                            }
                        }
                    }
                }
            }
        }

    }

    private fun setClick() {
        containerList.forEach {
            it.setOnClickListener { view ->
                clearSelected()
                containerClickListener?.onClick(view)
                setContainerSelected(it, true)
                setChildSelected(it, true)

            }
        }

    }


    private fun clearSelected() {
        containerList.forEach {
            setContainerSelected(it, false)
            setChildSelected(it, false)
        }
    }


    private fun setContainerSelected(container: LinearLayout, selected: Boolean) {
        if (selected) {
            container.background = ResourceUtils.getDrawable(R.drawable.stock_blue_radius_4)
        } else {
            container.background = ResourceUtils.getDrawable(R.drawable.shape_gray_radius_4)
        }
    }


    private fun setChildSelected(it: LinearLayout, isSelected: Boolean) {
        it.forEach { child ->
            if (child is CheckedTextView) {
                child.isChecked = isSelected
            }
        }
    }


    fun setNumber(response: MallCommentCount) {
        updateNumber(all_num_tv, response.all)
        updateNumber(newest_num_tv, response.newest)
        updateNumber(has_image_num_tv, response.photo)

        updateNumber(star_five_num_tv, response.five)
        updateNumber(star_four_num_tv, response.four)
        updateNumber(star_three_num_tv, response.three)
        updateNumber(star_two_num_tv, response.two)
        updateNumber(star_one_num_tv, response.one)
    }

    private fun updateNumber(textView: TextView, number: Int) {
        textView.text = "($number)"
    }


    interface CommentHeaderClickListener {
        fun onClick(it: View)
    }


}