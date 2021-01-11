package com.jcs.where.base;

/**
 * create by zyf on 2021/1/11 10:04 下午
 */
public class BaseEvent<T> {
    public int code;
    public String message;
    public T data;

    public BaseEvent() {
    }

    public BaseEvent(int code) {
        this.code = code;
    }

    public BaseEvent(String message) {
        this.message = message;
    }

    public BaseEvent(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseEvent(T data) {
        this.data = data;
    }

    public BaseEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
