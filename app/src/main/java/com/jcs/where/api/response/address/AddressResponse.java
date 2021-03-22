package com.jcs.where.api.response.address;

/**
 * Created by Wangsw  2021/3/22 13:49.
 */
public class AddressResponse {

/*    {
        "id": 5,
            "address": "xxxxxx.xxxx.xxx",
            "contact_name": "xxxxx",
            "sex": 1,
            "contact_number": "123456789"
    }*/


    public int id;

    /**
     * 性别（1：先生，2：女士）
     */
    public int sex;
    public String address = "";
    public String contact_name = "";
    public String contact_number = "";

}
