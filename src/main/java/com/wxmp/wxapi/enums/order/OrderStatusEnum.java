package com.wxmp.wxapi.enums.order;

import lombok.Getter;

@Getter
public enum  OrderStatusEnum {

    INCALL(0,"呼叫中"),
    RECEIVED(1,"司机已接单"),
    USERCAHCEL(2,"已取消"),
    DRIVERCAHCEL(3,"司机取消订单"),
    ENDSOFSTROKE(4,"行程已结束");

    private Integer code;

    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
