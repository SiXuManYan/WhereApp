package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/1/9 2:29 下午
 */
public class LoginResponse {


    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYXBpLmpjc3Rlc3QuY29tXC91c2VyYXBpXC92MVwvbG9naW4iLCJpYXQiOjE2MTAxNzM1MjQsImV4cCI6MTYxMjc2NTUyNCwibmJmIjoxNjEwMTczNTI0LCJqdGkiOiJQakpPUUE4eEhhN1FFWjVzIiwic3ViIjoxMSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.gSL2VsTxtdAY3KyR7z_SgE7a7Mj-M6aHWZJihcc4sYc
     */

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
