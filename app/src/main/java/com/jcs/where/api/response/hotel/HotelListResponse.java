package com.jcs.where.api.response.hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/3/16 15:02.
 * 酒店列表返回
 */
public class HotelListResponse {

/*
  {
                "id": 79,
                "images": [
                    "https://whereoss.oss-accelerate.aliyuncs.com/hotels/192958857.jpg"
                ],
                "name": "Rogahn-Muller Hotel",
                "grade": 3,
                "comment_counts": 17,
                "tags": [
                    {
                        "name": "商务出行"
                    },
                    {
                        "name": "儿童乐园"
                    },
                    {
                        "name": "机场周边"
                    }
                ],
                "address": "674 Kris Isle Apt. 752\r\nLake Laurieton, TX 49496",
                "lat": 14.6441688,
                "lng": 120.5044721,
                "price": 121,
                "distance": 9.3,
                "remain_room_num": 0,
                "facebook_link": "https://www.facebook.com",
                "star_level": 4
            },

    */

    public int id;
    public ArrayList<String> images = new ArrayList<>();
    public String name;
    public float grade;
    public String comment_counts;
    public String address;
    public String lat;
    public String lng;
    public String price;
    public String distance;
    public String remain_room_num;
    public String facebook_link;
    public String star_level = "";

    public ArrayList<Tags> tags = new ArrayList<>();


    public class Tags {
        public String name = "";
    }

}
