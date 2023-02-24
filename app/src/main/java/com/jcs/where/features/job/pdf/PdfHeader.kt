package com.jcs.where.features.job.pdf

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jcs.where.R

/**
 * Created by Wangsw  2023/2/23 14:38.
 *
 */
class PdfHeader : LinearLayout {


    lateinit var name: TextView
    lateinit var age: TextView
    lateinit var info: TextView
    lateinit var phone: TextView
    lateinit var email: TextView

     lateinit var avatar: ImageView

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context,
        attrs,
        defStyleAttr,
        defStyleRes) {
        initView()
    }


    private fun initView() {

        val view = LayoutInflater.from(context).inflate(R.layout.view_pdf_header, this, true)
        name = view.findViewById(R.id.name_tv)
        age = view.findViewById(R.id.age_tv)
        info = view.findViewById(R.id.info_tv)
        phone = view.findViewById(R.id.phone_tv)
        email = view.findViewById(R.id.email_tv)
        avatar = view.findViewById(R.id.avatar_iv)
    }

}