package com.jcs.where.api.response;

import java.util.List;

/**
 * create by zyf on 2020/12/11 7:57 PM
 */
public class OrderListResponse {


    /**
     * current_page : 1
     * data : [{"id":235,"order_type":1,"trade_no":"18202012102047041717","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-10 20:47:04","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月10日","end_date":"12月11日","room_price":415}},{"id":234,"order_type":1,"trade_no":"18202012102046086302","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-10 20:46:08","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月10日","end_date":"12月11日","room_price":415}},{"id":233,"order_type":1,"trade_no":"18202012102043509177","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-10 20:43:50","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月10日","end_date":"12月11日","room_price":415}},{"id":232,"order_type":1,"trade_no":"18202012102039597156","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-10 20:39:59","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月10日","end_date":"12月11日","room_price":415}},{"id":231,"order_type":1,"trade_no":"18202012102030094299","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-10 20:30:09","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月10日","end_date":"12月11日","room_price":415}},{"id":230,"order_type":1,"trade_no":"18202012082038093211","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-08 20:38:09","price":830,"order_status":5,"model_data":{"room_num":2,"room_type":"2张床","start_date":"12月08日","end_date":"12月09日","room_price":415}},{"id":229,"order_type":1,"trade_no":"18202012081809134668","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-08 18:09:13","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月08日","end_date":"12月09日","room_price":415}},{"id":228,"order_type":1,"trade_no":"18202012081807312981","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-08 18:07:31","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月08日","end_date":"12月09日","room_price":415}},{"id":226,"order_type":1,"trade_no":"18202012081804294457","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-08 18:04:29","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月08日","end_date":"12月09日","room_price":415}},{"id":225,"order_type":1,"trade_no":"18202012081801345852","model_id":75,"title":"Wilkinson-Parker Hotel","image":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"created_at":"2020-12-08 18:01:34","price":415,"order_status":5,"model_data":{"room_num":1,"room_type":"2张床","start_date":"12月08日","end_date":"12月09日","room_price":415}}]
     * first_page_url : https://api.jcstest.com/commonapi/v1/orders?page=1
     * from : 1
     * last_page : 3
     * last_page_url : https://api.jcstest.com/commonapi/v1/orders?page=3
     * next_page_url : https://api.jcstest.com/commonapi/v1/orders?page=2
     * path : https://api.jcstest.com/commonapi/v1/orders
     * per_page : 10
     * prev_page_url : null
     * to : 10
     * total : 21
     */

    private Integer current_page;
    private String first_page_url;
    private Integer from;
    private Integer last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private Integer per_page;
    private Object prev_page_url;
    private Integer to;
    private Integer total;
    private List<DataBean> data;

    public Integer getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Integer current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getLast_page() {
        return last_page;
    }

    public void setLast_page(Integer last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPer_page() {
        return per_page;
    }

    public void setPer_page(Integer per_page) {
        this.per_page = per_page;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 235
         * order_type : 1
         * trade_no : 18202012102047041717
         * model_id : 75
         * title : Wilkinson-Parker Hotel
         * image : ["https://whereoss.oss-accelerate.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"]
         * created_at : 2020-12-10 20:47:04
         * price : 415
         * order_status : 5
         * model_data : {"room_num":1,"room_type":"2张床","start_date":"12月10日","end_date":"12月11日","room_price":415}
         */

        private Integer id;
        private Integer order_type;
        private String trade_no;
        private Integer model_id;
        private String title;
        private String created_at;
        private Integer price;
        private Integer order_status;
        private ModelDataBean model_data;
        private List<String> image;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getOrder_type() {
            return order_type;
        }

        public void setOrder_type(Integer order_type) {
            this.order_type = order_type;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public Integer getModel_id() {
            return model_id;
        }

        public void setModel_id(Integer model_id) {
            this.model_id = model_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getOrder_status() {
            return order_status;
        }

        public void setOrder_status(Integer order_status) {
            this.order_status = order_status;
        }

        public ModelDataBean getModel_data() {
            return model_data;
        }

        public void setModel_data(ModelDataBean model_data) {
            this.model_data = model_data;
        }

        public List<String> getImage() {
            return image;
        }

        public void setImage(List<String> image) {
            this.image = image;
        }

        public static class ModelDataBean {
            /**
             * room_num : 1
             * room_type : 2张床
             * start_date : 12月10日
             * end_date : 12月11日
             * room_price : 415
             */

            private Integer room_num;
            private String room_type;
            private String start_date;
            private String end_date;
            private Integer room_price;

            public Integer getRoom_num() {
                return room_num;
            }

            public void setRoom_num(Integer room_num) {
                this.room_num = room_num;
            }

            public String getRoom_type() {
                return room_type;
            }

            public void setRoom_type(String room_type) {
                this.room_type = room_type;
            }

            public String getStart_date() {
                return start_date;
            }

            public void setStart_date(String start_date) {
                this.start_date = start_date;
            }

            public String getEnd_date() {
                return end_date;
            }

            public void setEnd_date(String end_date) {
                this.end_date = end_date;
            }

            public Integer getRoom_price() {
                return room_price;
            }

            public void setRoom_price(Integer room_price) {
                this.room_price = room_price;
            }
        }
    }
}
