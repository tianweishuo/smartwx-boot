package com.wxmp.wxcms.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author TWS
 * @title: SubscribeUnsubscribe
 * @projectName smartwx
 * @description: 消息列表
 * @date 2019/5/2 12:46
 */
@Data
public class MsgEvent {
    //自增id
    private Integer id;

    //开发者微信号
    private String toUserName;

    //发送方帐号（一个OpenID）
    private String fromUserName;

    //消息类型，event
    private String msgType;

    //事件类型，subscribe(订阅)、unsubscribe(取消订阅)
    private String event;

    //创建时间
    private Date createTime;
}
