package com.jcs.where.api.request.merchant

/**
 * Created by Wangsw  2021/11/19 16:28.
 * 商家入驻表单提交
 */
class MerchantSettledData {

/*
  first_name:名
    middle_name:中间名
    last_name:姓
    contact_number:联系电话
    email:邮箱
    mer_name:商家名称
    mer_type_id:商家类型ID
    area_id:区域ID
    mer_address:商家地址
    mer_tel:商家电话
    mer_extension_tel:商家电话分机
    mer_site:商家网站
    mer_facebook:商家FaceBook
    mer_email:商家邮箱
    has_physical_store:是否有实体店（1：有，2：没有）
    business_license:营业执照（json数组形式）
    */

    var first_name = ""
    var middle_name = ""
    var last_name = ""
    var contact_number = ""
    var email = ""
    var mer_name = ""
    var mer_type_id = 0
    var area_id = 0
    var mer_address = ""
    var mer_tel = ""
    var mer_extension_tel = ""
    var mer_site = ""
    var mer_facebook = ""
    var mer_email = ""
    var has_physical_store = 0
    var business_license :ArrayList<String> = ArrayList()


    /// 获取信息

    var id = 0

    /**
     * 审核状态（1：待审核，2：审核通过，3：审核未通过）
     */
    var is_verify = 0
    var created_at = ""

    /**
     * 备注
     */
    var notes: String = ""



}