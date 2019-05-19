package com.wxmp.wxcms.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author TWS
 * @title: SubscribeUser
 * @projectName smartwx
 * @description: 订阅用户列表
 * @date 2019/5/2 12:34
 */
@Data
public class SubscribeUser {

    /**
     * 关注
     */
    public static Integer FOLLOW = 1;

    /**
     * 取消
     */
    public static Integer CANCEL = 2;


    //用户ID
    private String id;

    //开发者微信号
    private String toUserName;

    //发送方帐号（一个OpenID）
    private String fromUserName;

    //用户手机号
    private String phone;

    //1:关注,2:取关
    private Integer state;

    //上次取消关注时间
    private Date unfollowTime;

    //创建时间(关注时间)
    private Date createTime;

}
