package com.wxmp.wxck.params;

import lombok.Data;

/**
 * @author TWS
 * @title: UserInfoParams
 * @projectName smartwx
 * @description: TODO
 * @date 2019/5/28 21:50
 */
@Data
public class UserInfoParam {

   private String nickName;
   private Integer gender;
   private String language;
   private String city;
   private String province;
   private String country;
   private String avatarUrl;

}
