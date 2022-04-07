package com.jcs.where.bean;

import java.util.Objects;

public class CityResponse {
    public String id;
    public String name;
    public String pinyin;
    public boolean isHot;
    public double lat;
    public double lng;


    public CityResponse() {
    }

    public CityResponse(String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
        this.id = id;
    }

    public CityResponse(String id, String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
        this.id = id;
    }

    public CityResponse(String id, String name, String pinyin, boolean isHot) {
        this.name = name;
        this.pinyin = pinyin;
        this.id = id;
        this.isHot = isHot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityResponse cityResponse = (CityResponse) o;
        return Objects.equals(name, cityResponse.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
