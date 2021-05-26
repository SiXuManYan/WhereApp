package com.jcs.where.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zyf on 2020/12/27 3:30 PM
 */
public class HotelRoomListResponse {

    /**
     * id : ID
     * images : 图片
     * name : 房间名称
     * hotel_room_type : 酒店类型
     * breakfast_type : 早餐类型（1：有，2：没有）
     * room_area : 房间面积
     * room_num : 房间数量
     * remain_room_num : 剩余房间数量
     * price : 价格
     * is_cancel : 是否可取消（1：可取消，2：不可取消）
     */

    public int id;
    public List<String> images = new ArrayList<>();
    public String name;
    public String hotel_room_type;
    public int breakfast_type;
    public String room_area;
    public String room_num;
    public int remain_room_num;
    public int price;
    public String is_cancel;
    public List<TagsBean> tags = new ArrayList<>();


    public static class TagsBean {
        /**
         * zh_cn_name : 标签1
         */

        public String zh_cn_name;

    }
}
