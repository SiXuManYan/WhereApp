package com.jcs.where.api.response.recommend;

import java.nio.channels.FileLock;

/**
 * Created by Wangsw  2021/3/1 17:26.
 * 推荐
 */
public class RecommendResponse {

    /*    {
                "id": -5560003263243012,
                "module_type": -8358912549756568,
                "images": [],
                "title": "kBM^uHE",
                "grade": 7626899780435573,
                "comment_num": 4827186162277688,
                "tags": [],
                "address": ")bJj",
                "lat": -8466032590978560,
                "lng": 7850346910417496,
                "price": -1591808220071440.2,
                "remain_room_num": -7736963560453408,
                "facebook_link": "qdfHsn",
                "star_level": 3238374759773920,
                "distance": -2562233931618145,
                "per_price": -1354097720749708.5,
                "take_out_status": -4164900290038488,
                "restaurant_type": "CKb",
                "area_name": "x89M3b",
                "created_at": "uRRkr"
        }*/
    public String id = "";

    public int module_type = 0;
    public String[] images = {};

    public String title = "";
    public String grade = "";
    public String comment_num = "";

    public String[] tags = {};
    public String address = "";
    public String lat = "";
    public String lng = "";
    public String price = "";
    public String remain_room_num = "";
    public String facebook_link = "";
    public String star_level = "";
    public float distance = 0;
    public String per_price = "";
    public int take_out_status = 0;
    public String restaurant_type = "";
    public String area_name = "";
    public String created_at = "";
}
