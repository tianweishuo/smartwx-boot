package com.wxmp.wxapi.service.impl;

import com.wxmp.wxapi.dto.UserDTO;
import com.wxmp.wxapi.service.SubscribeUserService;
import com.wxmp.wxcms.domain.SubscribeUser;
import com.wxmp.wxcms.mapper.SubscribeUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TWS
 * @title: SubscribeUserServiceImpl
 * @projectName smartwx
 * @description: TODO
 * @date 2019/5/19 15:18
 */
@Service
public class SubscribeUserServiceImpl implements SubscribeUserService {

    @Autowired
    private SubscribeUserDao subscribeUserDao;


    /**
     * 通过openid检测是否存在
     * @return
     */
    @Override
    public boolean findUserByFromUserName(String openid) {
        boolean flag = false;
        SubscribeUser subscribeUser = subscribeUserDao.findByFromUserName(openid);
        if(subscribeUser != null){
            flag  = true;
        }
        return flag;
    }

    /**
     * 保存手机号
     * @param phone
     * @param openid
     * @return
     */
    @Override
    public boolean updateOrSavePhone(String openid,String phone) {
        boolean flag = false;
        Integer integer = subscribeUserDao.updatePhoneByFormUserName(openid, phone);
        if(integer > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public UserDTO findUserByOpenid(String openid) {
        UserDTO userDTO = new UserDTO();
        SubscribeUser subscribeUser = subscribeUserDao.findByFromUserName(openid);
        if(subscribeUser != null){
            userDTO.setPhone(subscribeUser.getPhone());
        }
        return userDTO;
    }
}
