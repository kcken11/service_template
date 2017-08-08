//package com.melot.talkee.service.impl;
//import java.util.Date;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import com.google.gson.Gson;
//import com.melot.module.ModuleService;
//import com.melot.module.iprepository.driver.service.IpRepositoryService;
//import com.melot.module.kkrpc.starter.Main;
//import com.melot.resource.domain.Resource;
//import com.melot.resource.service.ResourceService;
//import com.melot.sdk.core.util.MelotBeanFactory;
//import com.sun.org.apache.bcel.internal.generic.NEW;
//
//
//public class ResourceServiceTest {
//	
//	private static ResourceService resourceService;
//	
//   @BeforeClass
//    public static void setUpBeforeClass() throws Exception {
//	   Main.startClient("classpath*:conf/client-spring-config.xml");
//	   resourceService = MelotBeanFactory.getBean("resourceService", ResourceService.class);
//    }
//   	
//    @AfterClass
//    public static void tearDownAfterClass() throws Exception {
//        ModuleService.destroy();
//    }
//    
//    @Before
//    public void setUp() throws Exception {
//    }
//    
//    @After
//    public void tearDown() throws Exception {
//    }
//    
//
//	@Test
//	public void testSaveResource() {
//		
//		try {
//			Resource resource = new Resource();
//			resource.setImageUrl("/picture/20151030/16/95955014_4617.mp4");
//			resource.setSpecificUrl("/audio/20151030/16/95955014_4617.mp4");
//			resource.setState(0);
//			resource.setType(3);
//			resource.setTitle("test");
//			resource.setFileHeight(1080);
//			resource.setFileWidth(1920);
//			resource.setDuration(122);
//			System.out.println(new Gson().toJson(resourceService.saveResource(resource)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testCheckResource() {
//		try {
//			System.out.println(new Gson().toJson(resourceService.checkResource(2243, 3, -1, "aaa", 101)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	@Test
//	public void testDelResource() {
//		try {
//			System.out.println(new Gson().toJson(resourceService.delResource(2242, 3)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Test
//	public void testGetResource() {
//		try {
//			System.out.println(new Gson().toJson(resourceService.getResource(2243, 3)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
