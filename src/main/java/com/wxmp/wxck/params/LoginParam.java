package com.wxmp.wxck.params;

import lombok.Data;
import lombok.ToString;

/**
 * @author TWS
 * @title: LoginParam
 * @projectName smartwx
 * @description: 接收参数
 * @date 2019/5/25 15:33
 */
@Data
@ToString
public class LoginParam {

    private String code;

    private String errMsg;

    private String rawData;

    private UserInfoParam userInfo;

    private String signature;

    private String encryptedData;

    private String iv;

}
