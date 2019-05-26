package com.wxmp.utils;

import lombok.Data;

/**
 * @author TWS
 * @title: ResultUtil
 * @projectName smartwx
 * @description: 返回
 * @date 2019/5/25 22:18
 */
@Data
public class ResultUtil {


    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public ResultUtil() {
    }

    public static ResultUtil build(Integer status, String msg, Object data) {
        return new ResultUtil(status, msg, data);
    }

    public static ResultUtil ok(Object data) {
        return new ResultUtil(data);
    }

    public static ResultUtil ok() {
        return new ResultUtil(null);
    }

    public static ResultUtil errorMsg(String msg) {
        return new ResultUtil(500, msg, null);
    }

    public static ResultUtil errorMap(Object data) {
        return new ResultUtil(501, "error", data);
    }

    public static ResultUtil errorTokenMsg(String msg) {
        return new ResultUtil(502, msg, null);
    }

    public static ResultUtil errorException(String msg) {
        return new ResultUtil(555, msg, null);
    }

    public ResultUtil(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ResultUtil(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }


}
