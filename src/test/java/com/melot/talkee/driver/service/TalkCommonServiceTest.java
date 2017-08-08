package com.melot.talkee.driver.service;

import com.alibaba.fastjson.JSONArray;
import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.EmailAdviceTypeEnum;
import com.melot.talkee.driver.domain.SendConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * Created by mn on 2017/5/15.
 */
public class TalkCommonServiceTest {

     private static TalkCommonService talkCommonService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Main.startClient("classpath*:conf/client-spring-config.xml");
        talkCommonService = MelotBeanFactory.getBean("talkCommonService", TalkCommonService.class);
    }

  @Test
    public void testChannelList(){
      System.out.println(JSONArray.toJSONString(talkCommonService.getChannelList(1)));
    }
}
