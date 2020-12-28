package com.jcs.where.api.response;

/**
 * create by zyf on 2020/12/28 10:53 PM
 */
public class LinksResponse {

    /**
     * first : https://api.jcstest.com/generalapi/v1/infos?page=1
     * last : https://api.jcstest.com/generalapi/v1/infos?page=116
     * prev : null
     * next : https://api.jcstest.com/generalapi/v1/infos?page=2
     */

    private String first;
    private String last;
    private Object prev;
    private String next;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Object getPrev() {
        return prev;
    }

    public void setPrev(Object prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
