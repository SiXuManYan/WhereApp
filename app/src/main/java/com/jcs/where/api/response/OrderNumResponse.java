package com.jcs.where.api.response;

/**
 * create by zyf on 2020/12/11 8:12 PM
 */
public class OrderNumResponse {

    /**
     * all : 21
     * pay : 0
     * use : 1
     * comment : 1
     * refund : 0
     */

    private Integer all;
    private Integer pay;
    private Integer use;
    private Integer comment;
    private Integer refund;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public Integer getUse() {
        return use;
    }

    public void setUse(Integer use) {
        this.use = use;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Integer getRefund() {
        return refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }
}
