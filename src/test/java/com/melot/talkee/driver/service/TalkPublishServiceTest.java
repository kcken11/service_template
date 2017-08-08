package com.melot.talkee.driver.service;


import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.PublishLesson;
import com.melot.talkee.driver.domain.PublishLessonCount;

public class TalkPublishServiceTest {


	private static TalkPublishService talkPublishService;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 Main.startClient("classpath*:conf/client-spring-config.xml");
		 talkPublishService = MelotBeanFactory.getBean("talkPublishService", TalkPublishService.class);
	}

	@Test
	public void testPublishLesson() {
		Map<Long, Long> lessonTime = new HashMap<Long, Long>();
		
		long addTime = 5*60*1000;
		long currentTime = System.currentTimeMillis()+addTime;
		System.out.println(currentTime);
		
		long endTime = 0l;
		for (int i = 0; i < 6; i++) {
			endTime = currentTime+addTime*i;
			lessonTime.put(currentTime, endTime);
			currentTime  = endTime;
		}
		
		System.out.println(new Gson().toJson(lessonTime));
		String tagCode = talkPublishService.publishLesson(75, lessonTime, 1, 1,null);
		
		System.out.println(tagCode);
	}

	@Test
	public void testGetDailyPublishLessonList() {
		Date lessonDate = new Date(1498492800000l);

		List<PublishLesson> list =	talkPublishService.getDailyPublishLessonList(331, lessonDate, null, 1);
		
		System.out.println(new Gson().toJson(list));
	}

	@Test
	public void testGetPublishLessonList() {
		
		Date beginTime = new Date(1490976000000l);
		Date endTime = new Date(1493481600000l);

		List<PublishLessonCount> list =	talkPublishService.getPublishLessonList(19, beginTime, endTime, null, 1);
		
		System.out.println(new Gson().toJson(list));
	}


	@Test
	public void testModifyPublishLesson() {
		
		Map<Long, Long> lessonTime = new HashMap<Long, Long>();
		
		
		List<PublishLesson> list = talkPublishService.modifyPublishLesson(106, new Date(1496851200000l), null, 1, 1,null);
		
		System.out.println(new Gson().toJson(list));
		
	}
	
}
