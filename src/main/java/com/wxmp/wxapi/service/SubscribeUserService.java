package com.wxmp.wxapi.service;

import com.wxmp.wxapi.dto.UserDTO;
import com.wxmp.wxcms.domain.SubscribeUser;

/**
 * @author TWS
 * @title: SubscribeUserService
 * @projectName smartwx
 * @description: TODO
 * @date 2019/5/19 15:18
 */
public interface SubscribeUserService {

    /**
     * 检测是否存在
     * @return
     */
    boolean findUserByFromUserName(String openid);


    /**
     * 保存手机号
     * @param phone
     * @param openid
     * @return
     */
    boolean updateOrSavePhone(String openid,String phone);



    UserDTO findUserByOpenid(String openid);


}
