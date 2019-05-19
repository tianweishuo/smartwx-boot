/*
 * FileName：MyServiceImpl.java 
 * <p>
 * Copyright (c) 2017-2020, <a href="http://www.webcsn.com">hermit (794890569@qq.com)</a>.
 * <p>
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl-3.0.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.wxmp.wxapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wxmp.core.exception.WxErrorException;
import com.wxmp.core.util.wx.WxUtil;
import com.wxmp.idworker.Id;
import com.wxmp.idworker.Sid;
import com.wxmp.idworker.utils.Utils;
import com.wxmp.wxapi.process.*;
import com.wxmp.wxapi.service.MyService;
import com.wxmp.wxapi.vo.Matchrule;
import com.wxmp.wxapi.vo.MsgRequest;
import com.wxmp.wxapi.vo.MsgResponseText;
import com.wxmp.wxapi.vo.MsgResponseUrl;
import com.wxmp.wxcms.domain.*;
import com.wxmp.wxcms.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 业务消息处理
 * 开发者根据自己的业务自行处理消息的接收与回复；
 */

@Slf4j
@Service
public class MyServiceImpl implements MyService {

    @Resource
    private MsgBaseDao msgBaseDao;

    @Resource
    private MsgNewsDao msgNewsDao;

    @Resource
    private AccountMenuDao menuDao;

    @Resource
    private AccountMenuGroupDao menuGroupDao;

    @Resource
    private AccountFansDao fansDao;

    @Resource
    private UserTagDao userTagDao;

    @Resource
    private SubscribeUserDao subscribeUserDao;

    @Resource
    private MsgEventDao msgEventDao;


    /**
     * 处理消息
     * 开发者可以根据用户发送的消息和自己的业务，自行返回合适的消息；
     * @param msgRequest : 接收到的消息
     * @param mpAccount ： appId
     */
    public String processMsg(MsgRequest msgRequest, MpAccount mpAccount)  throws WxErrorException {
        log.info("【消息处理】消息内容:{}", JSON.toJSONString(msgRequest));
        String msgtype = msgRequest.getMsgType();// 接收到的消息类型
        log.info("【消息处理】消息类型:{}",MsgType.getValue(msgtype));
        String respXml = null;// 返回的内容
        SubscribeUser subscribeUser = subscribeUserDao.findByFromUserName(msgRequest.getFromUserName());
        //消息类型
        if(MsgType.Event.name.equals(msgtype)){
            MsgEvent msgEvent = new MsgEvent();
            //事件
            if(MsgType.SUBSCRIBE.name.equals(msgRequest.getEvent())){
                log.info("订阅事件FromUserName:{}",msgRequest.getFromUserName());
                SubscribeUser user = new SubscribeUser();
                //订阅事件
                if(subscribeUser != null){
                    //判断手机号是否存在
                    if(subscribeUser.getPhone() == null){
                        log.info("【非首次订阅】:检测到手机号为空,需要用户注册填写手机号!");
                        MsgText text = msgBaseDao.getMsgTextByInputCode(MsgType.Location.toString());
                        MsgResponseText msgResponseText = WxMessageBuilder.getMsgResponseText(msgRequest, text);
                        respXml = MsgXmlUtil.textToXml(msgResponseText);
                    }
                    subscribeUserDao.updateState(msgRequest.getFromUserName(),SubscribeUser.FOLLOW);
                }else {
                    log.info("【首次订阅】:检测到手机号为空,需要用户注册填写手机号!");
                    user.setId(new Sid().nextShort());
                    user.setToUserName(msgRequest.getToUserName());
                    user.setFromUserName(msgRequest.getFromUserName());
                    user.setPhone(null);
                    user.setState(SubscribeUser.FOLLOW);
                    user.setCreateTime(new Date());
                    subscribeUserDao.insert(user);
                    MsgText text = msgBaseDao.getMsgTextByInputCode(MsgType.Location.toString());
                    MsgResponseText msgResponseText = WxMessageBuilder.getMsgResponseText(msgRequest, text);
                    respXml = MsgXmlUtil.textToXml(msgResponseText);
                }
            }else if(MsgType.UNSUBSCRIBE.name.equals(msgRequest.getEvent())){
                //取消订阅
                log.info("【消息处理】取消订阅FromUserName:{}",msgRequest.getFromUserName());
                if(subscribeUser != null){
                    subscribeUser.setState(SubscribeUser.CANCEL);
                    subscribeUser.setUnfollowTime(new Date());
                    subscribeUserDao.update(subscribeUser);
                }
            }
        }else if(MsgType.Location.name.equals(msgtype)){
            log.info("地理位置消息");
            if(subscribeUser != null){
                String phone = subscribeUser.getPhone();
                if(phone == null){
                    log.info("手机号为绑定");
                    MsgText text = msgBaseDao.getMsgTextByInputCode(MsgType.Location.toString());
                    MsgResponseText msgResponseText = WxMessageBuilder.getMsgResponseText(msgRequest, text);
                    respXml = MsgXmlUtil.textToXml(msgResponseText);
                }
            }
        }
        return respXml;
    }

    // 处理文本消息
    private String processTextMsg(MsgRequest msgRequest, MpAccount mpAccount) {
        String content = msgRequest.getContent();
        if (!StringUtils.isEmpty(content)) {// 文本消息
            String tmpContent = content.trim();
            List<MsgNews> msgNews = msgNewsDao.getRandomMsgByContent(tmpContent, mpAccount.getMsgcount());
            if (!CollectionUtils.isEmpty(msgNews)) {
                return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
            }
        }
        return null;
    }

    // 处理事件消息
    private String processEventMsg(MsgRequest msgRequest, MpAccount mpAccount) throws WxErrorException {
        String key = msgRequest.getEventKey();
        if (MsgType.SUBSCRIBE.toString().equals(msgRequest.getEvent())) {// 订阅消息
            log.info("关注者openId----------" + msgRequest.getFromUserName());
            String openId = msgRequest.getFromUserName();
            AccountFans fans = WxApiClient.syncAccountFans(openId, mpAccount);
            // 用户关注微信公众号后更新粉丝表
            if (null != fans) {
                AccountFans tmpFans = fansDao.getByOpenId(openId);
                if (tmpFans == null) {
                    fans.setAccount(mpAccount.getAccount());
                    fansDao.add(fans);
                } else {
                    fans.setId(tmpFans.getId());
                    fansDao.update(fans);
                }
            }
            MsgText text = msgBaseDao.getMsgTextBySubscribe();
            if (text != null) {
                return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
            }
        } else if (MsgType.UNSUBSCRIBE.toString().equals(msgRequest.getEvent())) {// 取消订阅消息
            MsgText text = msgBaseDao.getMsgTextByInputCode(MsgType.UNSUBSCRIBE.toString());
            if (text != null) {
                return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
            }
        } else {// 点击事件消息
            if (!StringUtils.isEmpty(key)) {
                /**
                 * 固定消息
                 * _fix_ ：在我们创建菜单的时候，做了限制，对应的event_key 加了 _fix_
                 * 
                 * 当然开发者也可以进行修改
                 */
                if (key.startsWith("_fix_")) {
                    String baseIds = key.substring("_fix_".length());
                    if (!StringUtils.isEmpty(baseIds)) {
                        String[] idArr = baseIds.split(",");
                        if (idArr.length > 1) {// 多条图文消息
                            List<MsgNews> msgNews = msgBaseDao.listMsgNewsByBaseId(idArr);
                            if (msgNews != null && msgNews.size() > 0) {
                                return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
                            }
                        } else {// 图文消息，或者文本消息
                            MsgBase msg = msgBaseDao.getById(baseIds);
                            if (msg.getMsgtype().equals(MsgType.Text.toString())) {
                                MsgText text = msgBaseDao.getMsgTextByBaseId(baseIds);
                                if (text != null) {
                                    return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
                                }
                            } else {
                                List<MsgNews> msgNews = msgBaseDao.listMsgNewsByBaseId(idArr);
                                if (msgNews != null && msgNews.size() > 0) {
                                    return MsgXmlUtil
                                            .newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    // 发布菜单
    public JSONObject publishMenu(MpAccount mpAccount) throws WxErrorException {
        // 获取数据库菜单
        List<AccountMenu> menus = menuDao.listWxMenus(new AccountMenu());
        Matchrule matchrule = new Matchrule();
        String menuJson = JSONObject.toJSONString(WxUtil.prepareMenus(menus, matchrule));
        log.info("创建菜单传参如下:" + menuJson);
        JSONObject rstObj = WxApiClient.publishMenus(menuJson, mpAccount);// 创建普通菜单
        log.info("创建菜单返回消息如下:" + rstObj.toString());
        // 以下为创建个性化菜单demo，只为男创建菜单；其他个性化需求 设置 Matchrule 属性即可
        // matchrule.setSex("1");//1-男 ；2-女
        // JSONObject rstObj = WxApiClient.publishAddconditionalMenus(menuJson,mpAccount);//创建个性化菜单

        // if(rstObj != null){//成功，更新菜单组
        // if(rstObj.containsKey("menu_id")){
        // menuGroupDao.updateMenuGroupDisable();
        // menuGroupDao.updateMenuGroupEnable(gid);
        // }else if(rstObj.containsKey("errcode") && rstObj.getInt("errcode") == 0){
        // menuGroupDao.updateMenuGroupDisable();
        // menuGroupDao.updateMenuGroupEnable(gid);
        // }
        // }
        return rstObj;
    }

    // 删除菜单
    public JSONObject deleteMenu(MpAccount mpAccount)  throws WxErrorException {
        JSONObject rstObj = WxApiClient.deleteMenu(mpAccount);
        if (rstObj != null && rstObj.getIntValue("errcode") == 0) {// 成功，更新菜单组
            menuGroupDao.updateMenuGroupDisable();
        }
        return rstObj;
    }

    // 获取用户列表
    public boolean syncAccountFansList(MpAccount mpAccount) throws WxErrorException {
        String nextOpenId = null;
        AccountFans lastFans = fansDao.getLastOpenId();
        if (lastFans != null) {
            nextOpenId = lastFans.getOpenId();
        }
        return doSyncAccountFansList(nextOpenId, mpAccount);
    }

    // 同步粉丝列表(开发者在这里可以使用递归处理)
    private boolean doSyncAccountFansList(String nextOpenId, MpAccount mpAccount) throws WxErrorException {
        String url = WxApi.getFansListUrl(WxApiClient.getAccessToken(mpAccount), nextOpenId);
        log.info("同步粉丝入参消息如下:" + url);
        JSONObject jsonObject = WxApi.httpsRequest(url, HttpMethod.POST, null);
        log.info("同步粉丝返回消息如下:" + jsonObject.toString());
        if (jsonObject.containsKey("errcode")) {
            return false;
        }
        List<AccountFans> fansList = new ArrayList<AccountFans>();
        if (jsonObject.containsKey("data")) {
            if (jsonObject.getJSONObject("data").containsKey("openid")) {
                JSONArray openidArr = jsonObject.getJSONObject("data").getJSONArray("openid");
                int length = openidArr.size();
                for (int i = 0; i < length; i++) {
                    Object openId = openidArr.get(i);
                    AccountFans fans = WxApiClient.syncAccountFans(openId.toString(), mpAccount);
                    // 设置公众号
                    fans.setAccount(WxMemoryCacheClient.getAccount());
                    fansList.add(fans);
                }
                // 批处理
                fansDao.addList(fansList);
            }
        }
        return true;
    }

    // 获取用户信息接口 - 必须是开通了认证服务，否则微信平台没有开放此功能
    public AccountFans syncAccountFans(String openId, MpAccount mpAccount, boolean merge) throws WxErrorException {
        AccountFans fans = WxApiClient.syncAccountFans(openId, mpAccount);
        if (merge && null != fans) {
            AccountFans tmpFans = fansDao.getByOpenId(openId);
            if (tmpFans == null) {
                fansDao.add(fans);
            } else {
                fans.setId(tmpFans.getId());
                fansDao.update(fans);
            }
        }
        return fans;
    }

    // 根据openid 获取粉丝，如果没有，同步粉丝
    public AccountFans getFansByOpenId(String openId, MpAccount mpAccount) throws WxErrorException {
        AccountFans fans = fansDao.getByOpenId(openId);
        if (fans == null) {// 如果没有，添加
            fans = WxApiClient.syncAccountFans(openId, mpAccount);
            if (null != fans) {
                fansDao.add(fans);
            }
        }
        return fans;
    }

    //同步粉丝列表
    public boolean syncUserTagList(MpAccount mpAccount) throws WxErrorException {
        String url=null;
        try {
            url = WxApi.getUserTagList(WxApiClient.getAccessToken(mpAccount));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        log.info("同步用户标签参消息如下:"+url);
        JSONObject jsonObject = WxApi.httpsRequest(url, HttpMethod.GET, null);
        log.info("同步用户标签消息如下:"+jsonObject.toString());
        if(jsonObject.containsKey("errcode")){
            return false;
        }
        JSONArray arr = jsonObject.getJSONArray("tags");//获取jsonArray对象
        String js=JSONObject.toJSONString(arr);//将array数组转换成字符串
        List<UserTag> userTagList=JSONObject.parseArray(js, UserTag.class);//把字符串转换成集合
        //判断是否已经同步
        Collections.sort(userTagList, new Comparator<UserTag>() {
            @Override
            public int compare(UserTag o1, UserTag o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
        UserTag userTag = userTagList.get(0);
        Integer maxIdInDb = userTagDao.getMaxId() == null ?  0 : userTagDao.getMaxId();//第一次同步，数据库没有数据返回null
        if (null == userTag.getId() || userTag.getId().intValue() == maxIdInDb.intValue()) {
            //说明已经同步
            return true;
        }else if( userTag.getId() > maxIdInDb ){
            //如果微信服务器新增用户标签，同步新增标签，新增标签的ID比本地库的ID大
            for (UserTag tag: userTagList) {
                if(tag.getId()<=maxIdInDb){
                    userTagList.remove(tag);
                }
            }
            userTagDao.addList(userTagList);
            return true;
        }
        return true;
    }
}
