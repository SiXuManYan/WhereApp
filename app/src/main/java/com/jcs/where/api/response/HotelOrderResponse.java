package com.jcs.where.api.response;

import java.io.Serializable;

public class HotelOrderResponse implements Serializable {

    /**
     * id : 221
     * trade_no : 18202012081705215482
     */

    private int id;
    private String trade_no;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }
}
