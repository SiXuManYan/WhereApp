package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * create by zyf on 2020/12/11 7:57 PM
 */
public class OrderListResponse {

    /**
     * id : 227
     * order_type : 1
     * trade_no : 18202012081804475429
     * model_id : 19
     * title : Blick Inc Hotel
     * image : ["https://whereoss.oss-accelerate.aliyuncs.com/hotels/531665e3a379a2b576.jpg"]
     * created_at : 2020-12-08 18:04:47
     * price : 324
     * model_data : {"order_status":5,"room_num":1,"room_type":"单人床","start_date":"12月08日","end_date":"12月09日","room_price":324}
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("order_type")
    private Integer orderType;
    @SerializedName("trade_no")
    private String tradeNo;
    @SerializedName("model_id")
    private Integer modelId;
    @SerializedName("title")
    private String title;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("price")
    private Integer price;
    @SerializedName("model_data")
    private ModelDataDTO modelData;
    @SerializedName("image")
    private List<String> image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ModelDataDTO getModelData() {
        return modelData;
    }

    public void setModelData(ModelDataDTO modelData) {
        this.modelData = modelData;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    /**
     * 模式类型（酒店、餐饮-堂食、餐饮-外卖）
     */
    public static class ModelDataDTO {
        /**
         * order_status : 5
         * room_num : 1
         * room_type : 单人床
         * start_date : 12月08日
         * end_date : 12月09日
         * room_price : 324
         */

        @SerializedName("order_status")
        private Integer orderStatus;
        @SerializedName("room_num")
        private Integer roomNum;
        @SerializedName("room_type")
        private String roomType;
        @SerializedName("start_date")
        private String startDate;
        @SerializedName("end_date")
        private String endDate;
        @SerializedName("room_price")
        private Integer roomPrice;

        public Integer getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(Integer orderStatus) {
            this.orderStatus = orderStatus;
        }

        public Integer getRoomNum() {
            return roomNum;
        }

        public void setRoomNum(Integer roomNum) {
            this.roomNum = roomNum;
        }

        public String getRoomType() {
            return roomType;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public Integer getRoomPrice() {
            return roomPrice;
        }

        public void setRoomPrice(Integer roomPrice) {
            this.roomPrice = roomPrice;
        }
    }
}
