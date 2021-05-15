package com.jcs.where.api.response.gourmet.restaurant;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Wangsw  2021/3/25 11:18.
 * 餐厅列表
 */
public class RestaurantResponse {

/*    {
        "id": 0,
            "images": [],
            "title": "",
            "grade": "",
            "comment_num": 0,
            "per_price": "",
            "distance": "",
            "type": "",
            "trading_area": "",
            "tags": [],
            "take_out_status": "",
            "lat": "",
            "lng": ""
    }*/


    public String id = "0";
    public ArrayList<String> images = new ArrayList<>();

    public String title = "";
    public float grade;

    public int comment_num;

    public BigDecimal per_price;
    public float distance;
    public String type;
    public String trading_area;

    public ArrayList<String> tags = new ArrayList<>();
    public int take_out_status;
    public double lat;
    public double lng;

}
