package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/3/13 9:10 下午
 */
public class TagResponse {

    /**
     * name : tag4
     * pivot : {"scenic_id":51,"tag_id":4}
     */

    @SerializedName("name")
    private String name;
    @SerializedName("pivot")
    private PivotDTO pivot;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PivotDTO getPivot() {
        return pivot;
    }

    public void setPivot(PivotDTO pivot) {
        this.pivot = pivot;
    }

    public static class PivotDTO {
        /**
         * scenic_id : 51
         * tag_id : 4
         */

        @SerializedName("scenic_id")
        private Integer scenicId;
        @SerializedName("tag_id")
        private Integer tagId;

        public Integer getScenicId() {
            return scenicId;
        }

        public void setScenicId(Integer scenicId) {
            this.scenicId = scenicId;
        }

        public Integer getTagId() {
            return tagId;
        }

        public void setTagId(Integer tagId) {
            this.tagId = tagId;
        }
    }
}
