package com.jcs.where.view

import com.flyco.tablayout.listener.CustomTabEntity

/**
* 导航栏项目数据类
*/
class TabItem(private var tabTitle: String,private val tabUnselectedIcon:Int,private val tabSelectedIcon:Int,val clx:Class<*>) : CustomTabEntity {


//    //未选图标
//    private var tabUnselectedIcon = 0
//    //选中图标
//    private var tabSelectedIcon = 0
    /**
     * 构造函数
     * @param tabTitle            名
     * @param tabUnselectedIcon   未选图标
     * @param tabSelectedIcon     选中图标
     */
//    constructor(tabTitle: String, tabUnselectedIcon:Int, tabSelectedIcon:Int,clx:Class<*>):this(tabTitle) {
//        this.tabUnselectedIcon = tabUnselectedIcon
//        this.tabSelectedIcon = tabSelectedIcon
//        mClx=clx
//    }

    override fun getTabUnselectedIcon() = tabUnselectedIcon

    override fun getTabSelectedIcon() = tabSelectedIcon

    override fun getTabTitle() = tabTitle

}