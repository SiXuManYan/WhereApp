package com.jcs.where.api.response.merchant

/**
 * Created by Wangsw  2022/1/11 15:01.
 *
 */
class MerchantApplyRecord {
    var id  = 0

    /**  审核状态 1：待审核，2：审核通过，3：审核未通过 */
    var is_verify  = 0
    var mer_name  = ""
    var created_at  = ""
}