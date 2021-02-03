package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * create by zyf on 2021/2/3 11:06 下午
 */
public class FootprintResponse {

    /**
     * id : 1652
     * type : 1
     * module_data : {"id":6,"title":"Hermiston and Sons Hotel","images":["https://whereoss.oss-accelerate.aliyuncs.com/hotels/192958857.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/Bellagio-Hotel-Casino-Las-Vegas.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/bomsa-exterior-0023-hor-feat.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/hkgwh-pool-exterior-5271-hor-wide.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/hotel-michael-pool.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/hotel-presidente-4s.jpg"]}
     * created_at : 2021-02-01 10:56:25
     */

    @SerializedName("id")
    private Integer id;
    /**
     * 根据这个分类区分酒店、餐厅、景点等
     */
    @SerializedName("type")
    private Integer type;
    /**
     * 这个Bean承载了足迹要展示的内容
     */
    @SerializedName("module_data")
    private ModuleDataDTO moduleData;
    @SerializedName("created_at")
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ModuleDataDTO getModuleData() {
        return moduleData;
    }

    public void setModuleData(ModuleDataDTO moduleData) {
        this.moduleData = moduleData;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class ModuleDataDTO {
        /**
         * id : 6
         * title : Hermiston and Sons Hotel
         * images : ["https://whereoss.oss-accelerate.aliyuncs.com/hotels/192958857.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/Bellagio-Hotel-Casino-Las-Vegas.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/bomsa-exterior-0023-hor-feat.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/hkgwh-pool-exterior-5271-hor-wide.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/hotel-michael-pool.jpg","https://whereoss.oss-accelerate.aliyuncs.com/hotels/hotel-presidente-4s.jpg"]
         */

        @SerializedName("id")
        private Integer id;
        @SerializedName("title")
        private String title;
        @SerializedName("images")
        private List<String> images;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
