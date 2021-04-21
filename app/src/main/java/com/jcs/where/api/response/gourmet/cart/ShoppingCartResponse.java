package com.jcs.where.api.response.gourmet.cart;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Wangsw  2021/4/7 11:23.
 */
public class ShoppingCartResponse implements Serializable {


    public String restaurant_id = "";
    public String restaurant_name = "";
    public ArrayList<Products> products = new ArrayList<>();
    public boolean nativeIsSelect = false;



}
