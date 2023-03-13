package com.jiechengsheng.city.features.job.company.album

import android.graphics.Color
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.job.company.CompanyAlbumAdapter
import com.jiechengsheng.city.features.job.company.CompanyPresenter
import com.jiechengsheng.city.features.job.company.CompanyView
import com.jiechengsheng.city.features.media.MediaDetailActivity
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.activity_no_refresh_list.*

/**
 * Created by Wangsw  2022/11/3 13:45.
 * 公司相册
 */
class CompanyAlbumActivity : BaseMvpActivity<CompanyPresenter>(), CompanyView {

    private var companyId = 0

    private lateinit var mAdapter: CompanyAlbumAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_no_refresh_list

    override fun initView() {

        intent.extras?.let {
            companyId = it.getInt(Constant.PARAM_ID, 0)
        }

        mJcsTitle.setMiddleTitle(R.string.company_album)
        container_ll.setBackgroundColor(Color.WHITE)
        recycler.setBackgroundColor(ColorUtils.getColor(R.color.colorPrimary))

        recycler.setPadding(SizeUtils.dp2px(2f), 0, SizeUtils.dp2px(2f), 0)


        mAdapter = CompanyAlbumAdapter().apply {
            setOnItemClickListener { _, _, position ->
                MediaDetailActivity.navigationOnlyStringImage(this@CompanyAlbumActivity, position, mAdapter.data, true)
            }
        }
        recycler.apply {
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            adapter = mAdapter
        }
    }

    override fun initData() {
        presenter = CompanyPresenter(this)
        presenter.getAlbum(companyId)

    }

    override fun bindListener() = Unit


    override fun bindCompanyAlbum(images: ArrayList<String>) {
        mAdapter.setNewInstance(images)
    }

}