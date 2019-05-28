package com.wxmp.wxck.ctrl;

import com.alibaba.fastjson.JSON;
import com.wxmp.core.util.CacheUtils;
import com.wxmp.core.util.HttpClientUtils;
import com.wxmp.core.util.MD5Util;
import com.wxmp.utils.AESDecryptUtil;
import com.wxmp.utils.MD5Utils;
import com.wxmp.utils.ResultUtil;
import com.wxmp.wxck.params.LoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TWS
 * @title: WxCk
 * @projectName smartwx
 * @description: 微信乘客
 * @date 2019/5/25 15:13
 */
@Slf4j
@RestController
@RequestMapping("/wxck")
public class WxCkController {

    public static String appId = "wx5cd47c19cbac6cc0";
    public static String appSecret = "0b35ecc6b5c90473b0b836dfc57741c9";


    @PostMapping("/getWxSession")
    public ResultUtil getWxSession(@RequestBody LoginParam param) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+appSecret+"&js_code="+param.getCode()+"&grant_type=authorization_code";
        String res = HttpClientUtils.sendHttpGet(url);
        Map map = JSON.parseObject(res, Map.class);
        log.info("微信小程序注册:返回报文{}",res);
        //将用户返回存入缓存中
        Map<String,String> token = new HashMap<>();
        token.put("openid",map.get("openid").toString());
        token.put("session_key",map.get("session_key").toString());
        CacheUtils.put(MD5Utils.getMD5Str(map.get("openid").toString()+map.get("session_key").toString()),token);
        String reultToken = MD5Utils.getMD5Str(map.get("openid").toString() + map.get("session_key").toString());
        Map result = new HashMap<>();
        result.put("token",reultToken);
        return ResultUtil.ok(result);
    }

    @PostMapping("/login")
    public ResultUtil login(@RequestBody LoginParam param){
        log.info("信息：[{}]",param);
        return ResultUtil.ok();
    }


}
