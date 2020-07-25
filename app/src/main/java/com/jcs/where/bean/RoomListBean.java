package com.jcs.where.bean;

import java.util.List;

public class RoomListBean {

    /**
     * id : 1
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"]
     * name : 大床房
     * hotel_room_type : 名称1
     * breakfast_type : 2
     * room_area : 18-22
     * room_num : 10
     * remain_room_num : null
     * price : 266
     * is_cancel : 1
     * tags : [{"zh_cn_name":"标签1","pivot":{"hotel_room_id":1,"hotel_room_tag_id":1}},{"zh_cn_name":"标签2","pivot":{"hotel_room_id":1,"hotel_room_tag_id":2}}]
     */

    private int id;
    private String name;
    private String hotel_room_type;
    private int breakfast_type;
    private String room_area;
    private int room_num;
    private Object remain_room_num;
    private int price;
    private int is_cancel;
    private List<String> images;
    private List<TagsBean> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHotel_room_type() {
        return hotel_room_type;
    }

    public void setHotel_room_type(String hotel_room_type) {
        this.hotel_room_type = hotel_room_type;
    }

    public int getBreakfast_type() {
        return breakfast_type;
    }

    public void setBreakfast_type(int breakfast_type) {
        this.breakfast_type = breakfast_type;
    }

    public String getRoom_area() {
        return room_area;
    }

    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }

    public int getRoom_num() {
        return room_num;
    }

    public void setRoom_num(int room_num) {
        this.room_num = room_num;
    }

    public Object getRemain_room_num() {
        return remain_room_num;
    }

    public void setRemain_room_num(Object remain_room_num) {
        this.remain_room_num = remain_room_num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        /**
         * zh_cn_name : 标签1
         * pivot : {"hotel_room_id":1,"hotel_room_tag_id":1}
         */

        private String zh_cn_name;
        private PivotBean pivot;

        public String getZh_cn_name() {
            return zh_cn_name;
        }

        public void setZh_cn_name(String zh_cn_name) {
            this.zh_cn_name = zh_cn_name;
        }

        public PivotBean getPivot() {
            return pivot;
        }

        public void setPivot(PivotBean pivot) {
            this.pivot = pivot;
        }

        public static class PivotBean {
            /**
             * hotel_room_id : 1
             * hotel_room_tag_id : 1
             */

            private int hotel_room_id;
            private int hotel_room_tag_id;

            public int getHotel_room_id() {
                return hotel_room_id;
            }

            public void setHotel_room_id(int hotel_room_id) {
                this.hotel_room_id = hotel_room_id;
            }

            public int getHotel_room_tag_id() {
                return hotel_room_tag_id;
            }

            public void setHotel_room_tag_id(int hotel_room_tag_id) {
                this.hotel_room_tag_id = hotel_room_tag_id;
            }
        }
    }
}
