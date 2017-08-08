/*
package com.melot.talkee.service.impl;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.melot.chat.domain.CheckInfo;
import com.melot.chat.domain.PrivateLetter;
import com.melot.chat.service.ChatAnalyzerService;
import com.melot.chat.service.PrivateChatAnalyzerService;
import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.service.TalkSecurityService;

public class TalkSecurityServiceImplTest {


	private static TalkSecurityService talkSecurityService;

	private static PrivateChatAnalyzerService privateChatAnalyzerService;

	private static ChatAnalyzerService chatAnalyzerService;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 Main.startClient("classpath*:conf/client-spring-config.xml");
		 talkSecurityService = MelotBeanFactory.getBean("talkSecurityService", TalkSecurityService.class);

		 privateChatAnalyzerService = MelotBeanFactory.getBean("privateChatAnalyzerService", PrivateChatAnalyzerService.class);

		 chatAnalyzerService = MelotBeanFactory.getBean("chatAnalyzerService", ChatAnalyzerService.class);
	}

	@Test
	public void testGetOssToken() {

		talkSecurityService.getOssToken(25);
	}


	@Test
	public void testChatAnalyzer() {

		
		PrivateLetter privateLetter = privateChatAnalyzerService.checkPrivateLetter(7505798,7529652,"冰火");
		System.out.println(new Gson().toJson(privateLetter));

		privateChatAnalyzerService.addPrivateLetterPhrase(0, "社区");
		
		privateLetter = privateChatAnalyzerService.checkPrivateLetter(7505798,7529652,"社区");
		System.out.println(new Gson().toJson(privateLetter));
		
		String str = chatAnalyzerService.checkPhrase(7505798,7529652,"冰火");
		System.out.println(str);

		CheckInfo checkInfo = chatAnalyzerService.checkSensitiveWords(7505798, "冰火");
		System.out.println(new Gson().toJson(checkInfo));

	}
}
*/
