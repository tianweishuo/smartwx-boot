package com.wxmp.wxapi.enums.order;

import lombok.Getter;

@Getter
public enum  PayStatusEnum {
    UNSENT(0,"未发起结算未支付"),
    WAIT(1,"已发起结算等待支付"),
    SUCESS(2,"支付成功"),
    ;


    private Integer code;
    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
