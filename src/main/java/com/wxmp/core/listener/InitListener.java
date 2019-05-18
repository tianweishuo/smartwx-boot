/*
 * FileName：InitListener.java 
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
package com.wxmp.core.listener;

import com.alibaba.fastjson.JSON;
import com.wxmp.core.spring.SpringContextHolder;
import com.wxmp.core.util.CacheUtils;
import com.wxmp.wxapi.process.WxMemoryCacheClient;
import com.wxmp.wxcms.domain.Account;
import com.wxmp.wxcms.domain.SysConfig;
import com.wxmp.wxcms.service.AccountService;
import com.wxmp.wxcms.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author hermit
 * @version 2.0
 * @date 2018-04-17 10:54:58
 */
@Slf4j
@Component
public class InitListener implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        try {
            //放入公众号
            log.info("【初始化数据开始】");
            AccountService accountService = SpringContextHolder.getBean("accountService");
            List<Account> accounts = accountService.listForPage(new Account());
            log.info("公共号:{}", JSON.toJSONString(accounts));
            WxMemoryCacheClient.addMpAccount(accounts);
            //读取所有缓存
            SysConfigService configService = SpringContextHolder.getBean("sysConfigService");
            List<SysConfig> configList = configService.getConfigList();
            for (SysConfig config : configList) {
                log.info("系统配置信息:key:{}  ,  value:{}",config.getJkey().toUpperCase(),config.getJvalue());
                CacheUtils.put(config.getJkey().toUpperCase(), config.getJvalue());
            }
            log.info("【初始化数据结束】");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
