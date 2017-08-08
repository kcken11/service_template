package com.melot.talkee.driver.service;

import com.melot.talkee.driver.domain.SendConfig;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * Created by mn on 2017/5/15.
 */
public interface TalkEmailService {
  /**
   *
   * @param config 发送配置项
   * @throws MessagingException
   * @throws UnsupportedEncodingException
   */
  public void sendMail(SendConfig config) throws MessagingException, UnsupportedEncodingException;

  /**
   *
   * @param emailAdviceType 通知类型(1.预约成功,2上课通知,3预约取消,4注册)
   * @param periodId 课程id
   * @param studentId 学生id
   * @param extend  扩展参数
   *         上课通知
   */
  public void sendTemplateMail(int emailAdviceType,int periodId,int studentId,int extend);



}
