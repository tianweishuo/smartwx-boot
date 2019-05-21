package com.wxmp.wxcms.domain;

import com.wxmp.wxapi.enums.order.OrderStatusEnum;
import com.wxmp.wxapi.enums.order.PayStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallOrder {
    //订单id
    private String  orderId;
    //用户id
    private String  userId;
    //司机ID
    private String driverId;
    //订单状态 0-呼叫中,1-司机已接单,2-用户取消订单,3-司机取消订单,4-行程已结束
    private Integer orderStatus;
    //0-未发起结算未支付,1-已发起结算等待支付,2-支付成功
    private Integer payStatus;
    //开始地理位置信息
    private String  startLabel;
    //开始地理位置维度
    private String  startLocationX;
    //开始地理位置维度
    private String  startLocationY;
    //结束地理位置信息
    private String  endLabel;
    //结束地理位置维度
    private String  endLocationX;
    //结束地理位置维度
    private String  endLocationY;
    //创建时间
    private Date    createTime;
}
