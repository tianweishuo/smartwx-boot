package com.wxmp.wxapi.vo;

import lombok.Data;

/**
 * 公众号回复给用户的消息 - 链接消息
 */

@Data
public class MsgResponseUrl extends MsgResponse{
    //消息标题
    private String title;
    //消息描述
    private String description;
    //消息链接
    private String url;
    //消息id，64位整型
    private String msgId;
}
