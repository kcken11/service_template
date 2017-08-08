package com.melot.talkee.service.impl;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.service.TalkCommonService;


public class TalkCommonServiceImplTest {


    private static TalkCommonService talkCommonService;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
         Main.startClient("classpath*:conf/client-spring-config.xml");
         talkCommonService = MelotBeanFactory.getBean("talkCommonService", TalkCommonService.class);
    }

    @Test
    public void testGetSmsConfig() {
        fail("Not yet implemented");
    }

    @Test
    public void testConfigInfo() {
        talkCommonService.getConfigInfo(null, 1);
    }

    
    @Test
    public void testGetChannelList() {
        talkCommonService.getChannelList(null);
    }

}
