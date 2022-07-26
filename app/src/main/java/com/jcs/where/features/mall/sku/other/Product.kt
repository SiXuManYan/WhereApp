package com.jcs.where.features.mall.sku.other;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.features.mall.sku.bean.Sku;

import java.util.List;

/**
 * Created by liufei on 2017/11/30.
 */

public class Product {
    private String id;
    private String title;
    private String status;
    private String main_image;
    private long price;
    private long original_cost;
    private String currencyUnit;
    private String measurementUnit;
    private int sold;
    private int stock;
    private List<String> medias;
    private List<Sku> skus;

    public static Product get(Context context) {
        String json = context.getString(R.string.product);
        return new Gson().fromJson(json, new TypeToken<Product>() {
        }.getType());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getOriginal_cost() {
        return original_cost;
    }

    public void setOriginal_cost(long original_cost) {
        this.original_cost = original_cost;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<String> getMedias() {
        return medias;
    }

    public void setMedias(List<String> medias) {
        this.medias = medias;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
