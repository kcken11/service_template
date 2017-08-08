package com.melot.talkee.driver.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.StudentInfo;

public class TalkUserServiceTest {

	private static TalkUserService userService;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 Main.startClient("classpath*:conf/client-spring-config.xml");
		 userService = MelotBeanFactory.getBean("talkUserService", TalkUserService.class);
	}

	@Test
	public void testGetUserInfoByPhoneNumber() {
		System.out.println(new Gson().toJson(userService.getUserInfoByPhoneNumber("15868188955")));
	}

	@Test
	public void testGetUserInfoByUserId() {
		System.out.println(new Gson().toJson(userService.getUserInfoByUserId(12)));
	}

	@Test
	public void testGetUserInfoByLoginName() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void testGetUserPortrait() {
		
		String p = userService.getPortraitByUserId(81);
		
		System.out.println(p);;
	}

	@Test
	public void testGetPortraitByUserIdAndType() {

		String p = userService.getPortraitByUserIdAndType(81,3);

		System.out.println(p);;
	}

	@Test
	public void testGetUserInfoByEmail() {
		
		
		Map<Long, Long> map = new HashMap<Long, Long>();
		map.put(System.currentTimeMillis(), System.currentTimeMillis());
		map.put(System.currentTimeMillis()+1, System.currentTimeMillis()+1);
		map.put(System.currentTimeMillis()+2, System.currentTimeMillis()+2);
		System.out.println(new Gson().toJson(map));
		
	}

	@Test
	public void testRegisterViaPhoneNum() {

		int userId = userService.registerViaPhoneNum(1, null, 1, null, "15868188956", null, "1.0.0", "", "Abc123456");
		System.out.println(userId);
	}
	

	@Test
	public void testAdminRegister() {

		int userId = userService.adminRegister("陈磊", "li.chen", 101, "1111a1");
		System.out.println(userId);
	}

	@Test
	public void testChangeStudentInfo() {
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setCnNickname("李");
		studentInfo.setUserId(10);
		studentInfo.setEnNickname("Li");
		studentInfo.setGender(1);
	
		System.out.println(new Gson().toJson(userService.modifyStudentInfo(null,studentInfo)));
	}

	@Test
	public void testChangeTeacherInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testChangePassword() {
		fail("Not yet implemented");
	}

	@Test
	public void testFileUpload() {
		fail("Not yet implemented");
	}
	
	@Test
    public void testGetCity() {
	    userService.getAdminCityById(756);
    }
}
