package com.melot.talkee.driver.service;

import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.BuyPeriods;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by mn on 2017/5/22.
 */
public class TalkTradeOrderServiceTest {

    private static TalkTradeOrderService talkTradeOrderService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Main.startClient("classpath*:conf/client-spring-config.xml");
        talkTradeOrderService = MelotBeanFactory.getBean("talkTradeOrderService", TalkTradeOrderService.class);
    }

    @Test
    public void testAddTradeOver() {
        BuyPeriods buyPeriods = new BuyPeriods();
        buyPeriods.setStatus(0);
        buyPeriods.setAmount(500);
        buyPeriods.setCallbakOrderid("alipay002");
        buyPeriods.setIpaddr("10.0.0.100");
        buyPeriods.setPaymentMode(2);
        buyPeriods.setPeriods(20);
        buyPeriods.setUserId(85);
        buyPeriods.setFreeTimes(5);
        talkTradeOrderService.addTradeOrder(buyPeriods);
    }

    @Test
    public void testConfirmTradeOver() {
        String orderId="";

        int state = 1;

        talkTradeOrderService.confirmTradeOrder(orderId, state,5);
    }
}
