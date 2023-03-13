package com.jiechengsheng.city.features.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.features.complex.ConvenienceServiceActivity
import com.jiechengsheng.city.features.enterprise.EnterprisePageActivity
import com.jiechengsheng.city.features.gourmet.restaurant.list.RestaurantHomeActivity
import com.jiechengsheng.city.features.hotel.home.HotelHomeActivity
import com.jiechengsheng.city.features.map.government.GovernmentActivity
import com.jiechengsheng.city.features.travel.map.TravelMapActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.list.DividerDecoration

/**
 * Created by Wangsw  2021/11/12 16:01.
 *
 */
class CategoryAdapter2 : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_category2) {


    var newWidth: Int = 0

    /**
     * 点击 item 跳转到 综合服务 页面
     */
    private val TYPE_SERVICE = 0

    /**
     * 点击 item 跳转到 酒店 页面
     */
    private val TYPE_HOTEL = 1

    /**
     * 点击 item 跳转到 旅游 页面
     */
    private val TYPE_TOURISM = 2

    /**
     * 点击 item 跳转到 政府地图 页面
     */
    private val TYPE_GOVERNMENT = 3

    /**
     * 点击 item 跳转到 旅游旅行 页面
     */
    private val TYPE_TRAVEL = 4

    /**
     * 点击 item 跳转到 餐厅 页面
     */
    private val TYPE_RESTAURANT = 5

    /**
     * 点击 item 跳转到 企业黄页 页面
     */
    private val TYPE_YELLOW_PAGE = 6

    init {
        newWidth = (ScreenUtils.getScreenWidth() / 3.5).toInt()
    }

    private var showEdit = false

    public fun showEditButton(show: Boolean) {
        showEdit = show
    }

    override fun convert(holder: BaseViewHolder, item: Category) {
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val child_rv = holder.getView<RecyclerView>(R.id.child_rv)
        val edit_sw = holder.getView<ViewSwitcher>(R.id.edit_sw)

        if (showEdit) {

            if (item.follow_status) {
                edit_sw.displayedChild = 1

                if (item.is_default) {
                    edit_sw.visibility = View.GONE
                } else {
                    edit_sw.visibility = View.VISIBLE
                }

            } else {
                edit_sw.displayedChild = 0
                edit_sw.visibility = View.VISIBLE
            }

        } else {
            edit_sw.visibility = View.GONE
        }


        // title
        title_tv.text = item.name

        // child
        bindChild(child_rv, item)
    }

    private fun bindChild(child_rv: RecyclerView, parent: Category) {

        val childAdapter = CategoryChildAdapter()
        child_rv.apply {
            adapter = childAdapter
            layoutManager = object : GridLayoutManager(context, 4, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
            if (itemDecorationCount == 0) {
                addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(8f), 0, 0))
            }

        }


        childAdapter.setNewInstance(parent.child_categories)
        childAdapter.setOnItemClickListener { _, _, position ->
            val childCategory = childAdapter.data[position]
            when (childCategory.type) {
                TYPE_SERVICE -> startActivity(ConvenienceServiceActivity::class.java, Bundle().apply {
                    putString(ConvenienceServiceActivity.K_SERVICE_NAME, parent.name)
                    putString(ConvenienceServiceActivity.K_CATEGORIES, parent.id.toString())
                    putString(ConvenienceServiceActivity.K_CHILD_CATEGORY_ID, childCategory.id.toString())
                })
                TYPE_HOTEL -> startActivity(HotelHomeActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_CATEGORY_ID, childCategory.id)
                })
                TYPE_TOURISM -> TravelMapActivity.navigation(context, childCategory.id)
                TYPE_GOVERNMENT -> startActivity(GovernmentActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_CHILD_CATEGORY_ID, childCategory.id)
                })

                TYPE_TRAVEL -> startActivity(TravelMapActivity::class.java,null)
                TYPE_RESTAURANT -> startActivity(RestaurantHomeActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_PID, childCategory.id)
                    putString(Constant.PARAM_PID_NAME, childCategory.name)
                })
                TYPE_YELLOW_PAGE -> EnterprisePageActivity.navigation(context, currentCategoryId= childCategory.id)
                else -> ToastUtils.showLong(StringUtils.getString(R.string.coming_soon))
            }
        }

    }


    private fun startActivity(target: Class<*>, bundle: Bundle?) {
        if (bundle != null) {
            context.startActivity(Intent(context, target).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            context.startActivity(Intent(context, target).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

}