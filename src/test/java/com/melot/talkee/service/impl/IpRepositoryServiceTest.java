package com.melot.talkee.service.impl;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.melot.module.ModuleService;
import com.melot.module.iprepository.driver.service.IpRepositoryService;
import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.sun.org.apache.bcel.internal.generic.NEW;


public class IpRepositoryServiceTest {
	
    private static IpRepositoryService ipRepositoryService;
	
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	   Main.startClient("classpath*:conf/client-spring-config.xml");
	   ipRepositoryService = MelotBeanFactory.getBean("ipRepositoryService", IpRepositoryService.class);
    }
   	
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ModuleService.destroy();
    }
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void getIpInfo() {
    	
    	System.out.println(new Date().getTime());
    	
    	System.out.println(new Gson().toJson(ipRepositoryService.getIpInfo("122.224.137.164")));
    }
	
}
