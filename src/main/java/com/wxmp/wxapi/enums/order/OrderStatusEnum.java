package com.wxmp.wxapi.enums.order;

import lombok.Getter;

@Getter
public enum  OrderStatusEnum {

    INCALL(0,"呼叫中"),
    USERCAHCEL(1,"乘客取消订单"),
    DRIVERCAHCEL(2,"司机取消订单"),
    RECEIVED(3,"司机已接单"),
    INDRIVING(4,"行驶中"),
    ENDSOFSTROKE(5,"行程已结束");

    private Integer code;

    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
