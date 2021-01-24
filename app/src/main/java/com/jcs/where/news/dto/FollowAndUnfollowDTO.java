package com.jcs.where.news.dto;

import com.jcs.where.api.response.NewsChannelResponse;

import java.io.Serializable;
import java.util.List;

/**
 * create by zyf on 2021/1/23 5:08 下午
 */
public class FollowAndUnfollowDTO implements Serializable {

    public List<NewsChannelResponse> followed;
    public List<NewsChannelResponse> more;

    public int showPosition;

}
