package com.melot.talkee.driver.service;

import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.EmailAdviceTypeEnum;
import com.melot.talkee.driver.domain.EmailConfig;
import com.melot.talkee.driver.domain.SendConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by mn on 2017/5/15.
 */
public class TalkEmailServiceTest {

     private static TalkEmailService talkEmailService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Main.startClient("classpath*:conf/client-spring-config.xml");
        talkEmailService = MelotBeanFactory.getBean("talkEmailService", TalkEmailService.class);
    }
  @Test
    public  void testSendMail(){
      SendConfig config=new SendConfig();
      config.setSubject("中文乱码2");
      config.setContent("测试disconf配置2");
      config.setEmailTo(Arrays.asList("wei.yan@melot.cn"));
        try {
            talkEmailService.sendMail(config);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

  @Test
    public void testSendTemplateMail(){

        talkEmailService.sendTemplateMail(EmailAdviceTypeEnum.IN_CLASS,1110,19,19);



    }

}
