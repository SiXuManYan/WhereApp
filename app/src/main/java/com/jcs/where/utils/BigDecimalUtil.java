package com.jcs.where.utils;

import java.math.BigDecimal;

/**
 * Created by Wangsw on 2020/6/10.
 */
public class BigDecimalUtil {


    public static BigDecimal getValue(double a) {
        return getValue(a, 2);
    }

    public static BigDecimal getValue(double a, int len) {
        BigDecimal bigDecimal = new BigDecimal(a);
        return bigDecimal.setScale(len, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal add(double a, double b) {
        return add(a, b, 2);
    }

    public static BigDecimal add(double a, double b, int len) {
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.add(b2).setScale(len, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 保留两位不四舍五入
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal addUnNecessary(double a, double b) {
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.add(b2).setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 保留两位不四舍五入
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal addUnNecessary(BigDecimal a, BigDecimal b) {
        return a.add(b).setScale(2, BigDecimal.ROUND_DOWN);
    }


    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return add(a, b, 2);
    }

    public static BigDecimal add(BigDecimal... a) {

        BigDecimal temp = BigDecimal.ZERO;
        for (BigDecimal child : a) {
            temp = add(temp, child);
        }
        return temp;
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b, int len) {
        return a.add(b).setScale(len, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal sub(double a, double b) {
        return sub(a, b, 2);
    }

    public static BigDecimal sub(double d1, double d2, int len) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).setScale(len, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return sub(a, b, 2);
    }

    public static BigDecimal sub(BigDecimal d1, BigDecimal d2, int len) {
        return d1.subtract(d2).setScale(len, BigDecimal.ROUND_HALF_UP);
    }


    //


    public static BigDecimal mul(double a, double b) {
        return mul(a, b, 2);
    }


    public static BigDecimal mul(double d1, double d2, int len) {

        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).setScale(len, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal mul(BigDecimal a, BigDecimal b) {
        return mul(a, b, 2);
    }


    public static BigDecimal mul(BigDecimal d1, BigDecimal d2, int len) {
        return d1.multiply(d2).setScale(len, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal div(double a, double b) {

        return div(a, b, 2);
    }

    public static BigDecimal div(double d1, double d2, int len) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal div(BigDecimal a, BigDecimal b) {
        return div(a, b, 2);
    }

    public static BigDecimal div(BigDecimal d1, BigDecimal d2, int len) {
        return d1.divide(d2, len, BigDecimal.ROUND_HALF_UP);
    }


}
