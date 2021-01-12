package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * create by zyf on 2021/1/12 10:22 下午
 */
public class CityPickerResponse {

    @SerializedName("lists")
    private List<ListsDTO> lists;
    @SerializedName("hots")
    private List<?> hots;

    public List<ListsDTO> getLists() {
        return lists;
    }

    public void setLists(List<ListsDTO> lists) {
        this.lists = lists;
    }

    public List<?> getHots() {
        return hots;
    }

    public void setHots(List<?> hots) {
        this.hots = hots;
    }

    public static class ListsDTO {
        /**
         * letter : A
         * areas : [{"id":"1","name":"阿布凯","lat":14.7128091,"lng":120.4933624}]
         */

        @SerializedName("letter")
        private String letter;
        @SerializedName("areas")
        private List<AreasDTO> areas;

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        public List<AreasDTO> getAreas() {
            return areas;
        }

        public void setAreas(List<AreasDTO> areas) {
            this.areas = areas;
        }

        public static class AreasDTO {
            /**
             * id : 1
             * name : 阿布凯
             * lat : 14.7128091
             * lng : 120.4933624
             */

            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("lat")
            private Double lat;
            @SerializedName("lng")
            private Double lng;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Double getLat() {
                return lat;
            }

            public void setLat(Double lat) {
                this.lat = lat;
            }

            public Double getLng() {
                return lng;
            }

            public void setLng(Double lng) {
                this.lng = lng;
            }
        }
    }
}
