package com.wxmp.wxcms.mapper;

import com.wxmp.wxcms.domain.SubscribeUser;
import org.apache.ibatis.annotations.Param;

/**
 * @author TWS
 * @title: SubscribeUser
 * @projectName smartwx
 * @description: 订阅用户
 * @date 2019/5/2 12:52
 */
public interface SubscribeUserDao {


    void insert(SubscribeUser subscribeUser);

    /**
     * 通过微信用户号查询
     * @param fromUserName 发送方帐号（一个OpenID）
     * @return
     */
    SubscribeUser findByFromUserName(String fromUserName);

    /**
     * 更新
     * @param subscribeUser
     */
    void update(SubscribeUser subscribeUser);

    /**
     * 修改订阅状态
     * @param fromUserName 用户唯一ID
     * @param state 1.关注 2.取管
     */
    int updateState(@Param("fromUserName") String fromUserName,
                      @Param("state") Integer state);

    /**
     * 通过用户openid删除
     * @param fromUserName
     * @return
     */
    Integer deleteByFromUserName(String fromUserName);

    /**
     * 修改用户手机号
     * @param fromUserName
     * @param phone
     * @return
     */
    Integer updatePhoneByFormUserName(@Param("fromUserName") String fromUserName,
                                      @Param("phone") String phone);

}
