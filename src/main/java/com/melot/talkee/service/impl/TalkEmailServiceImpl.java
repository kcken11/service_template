package com.melot.talkee.service.impl;

import com.google.common.base.Charsets;
import com.kktalkee.crm.module.domain.*;
import com.kktalkee.crm.module.service.TlkAdminService;
import com.melot.kktv.util.DateUtil;
import com.melot.talkee.dao.AdminInfoMapper;
import com.melot.talkee.dao.EmailConfigMapper;
import com.melot.talkee.dao.StudentLessonMapper;
import com.melot.talkee.dao.StudentPeriodsMapper;
import com.melot.talkee.driver.domain.*;
import com.melot.talkee.driver.domain.AdminInfo;
import com.melot.talkee.driver.domain.StudentInfo;
import com.melot.talkee.driver.domain.TeacherInfo;
import com.melot.talkee.driver.service.*;
import com.melot.talkee.source.EmailSource;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mn on 2017/5/15.
 */
@Service
public class TalkEmailServiceImpl implements TalkEmailService {

    private static final Logger logger = Logger.getLogger(TalkEmailServiceImpl.class);

    @Autowired
    private EmailConfigMapper emailConfigMapper;
    @Autowired
    private TalkLessonService talkLessonService;

    @Autowired
    private TalkOrderService talkOrderService;

    @Autowired
    private TalkPublishService talkPublishService;
    @Autowired
    private TalkUserService talkUserService;
    @Autowired
    private StudentLessonMapper studentLessonMapper;
    @Autowired
    private AdminInfoMapper adminInfoMapper;
    private EmailSource emailSource;

    public void setEmailSource(EmailSource emailSource) {
        this.emailSource = emailSource;
    }


    @Override
    public void sendMail(SendConfig config) {

        Properties props = new Properties();
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.setProperty("mail.smtp.host", emailSource.getSmtphost());
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", emailSource.getSmtpport());
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.socketFactory.port", emailSource.getSmtpport());
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp"); //设置访问服务器的协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(emailSource.getSender(), new String(emailSource.getNickname().getBytes("ISO-8859-1")), "UTF-8"));
            msg.setSubject(config.getSubject());
            msg.setText(config.getContent());

            for (String address : config.getEmailTo()) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            }
            Transport transport = session.getTransport();
            transport.connect(emailSource.getSender(), emailSource.getPassword());
            transport.sendMessage(msg, msg.getAllRecipients());

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void sendTemplateMail(int adviceType, int periodId, int studentId, int extendId) {

      /*  try {

            if (periodId != 0) {

                StudentLesson lesson = studentLessonMapper.getOrderLessonByPeriodId(periodId);
                if (lesson == null) {
                    return;
                }


                if (adviceType == EmailAdviceTypeEnum.ORDER_SUCCESS_SINGLE) {
                    // 预约取消 -学生取消
                } else if (adviceType == EmailAdviceTypeEnum.STUDENT_CANCEL_ORDER) {
                    sendStudentAskForLevelEmail(periodId, studentId);
                }
                //老师取消预约,短信通知学生,邮件通知CC,CR
                else if (adviceType == EmailAdviceTypeEnum.TEACHER_CANCEL_ORDER) {
                    sendTeacherAskForLevelEmail(periodId, studentId);
                } else if (adviceType == EmailAdviceTypeEnum.STUDENT_CANCEL_ORDER_IN_FIVE_MIN) {
                    sendStudentAskForLevelInFiveMinutesEmail(periodId, studentId);
                } else if (adviceType == EmailAdviceTypeEnum.TEACHER_CANCEL_ORDER_IN_FIVE_MIN) {
                    sendTeacherAskForLevelInFiveMinutesEmail(periodId, studentId);
                } else if (adviceType == EmailAdviceTypeEnum.IN_CLASS) {
                    sendInclassEmail(periodId, studentId);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public String getLessonTypeName(int lessonType) {
        switch (lessonType) {
            case 1:
                return "普通课";
            case 2:
                return "公开课";
            case 3:
                return "试听课";
            case 4:
                return "调试课";
        }
        return "课";
    }

    /**
     * 切换老师本地时间
     *
     * @param date
     * @return
     */
    private static String convertToEnglish(Date date) throws ParseException {
        TimeZone us = TimeZone.getTimeZone("America/New_York");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'on' MMM dd'th'", Locale.US);
        sdf.setTimeZone(us);
        if (date != null) {
            return sdf.format(new Date());
        }
        return "";
    }


    public static void main(String[] args) throws ParseException {
        System.out.println(convertToEnglish(new Date()));
    }

    public void sendInclassEmail(Integer periodId, int studentId) {
        SendConfig sendConfig = new SendConfig();
        StudentLesson lesson = studentLessonMapper.getOrderLessonByPeriodId(periodId);
        StudentInfo student = talkUserService.getStudentInfoByUserId(studentId);
        AdminInfo adminInfo = null;
        EmailConfig emailConfig = new EmailConfig();
        String lessonTime = new SimpleDateFormat("HH:mm").format(lesson.getBeginTime());
        String nickName = student.getCnNickname();
        String contact = student.getPhoneNum() == null ? student.getEmail() : student.getPhoneNum();
        String content = "";
        //常课
        if (lesson.getConsType() == 1) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("studentId", studentId);
            params.put("publishType", TalkCommonEnum.PERIOD_TYPE_GENNEL);
            params.put("stateList", Arrays.asList(2));
            List<OrderLesson> lessons = studentLessonMapper.getOrderLesson(params);
            //没有已上完的常课,发送给CR
            if (lessons == null || lessons.size() == 0) {
                adminInfo = adminInfoMapper.selectAdminInfoByAdminId(student.getCrId());
                emailConfig.setAdviceType(2);
                emailConfig.setAdviceUserType(2);
                emailConfig.setAdviceStatus(1);
                emailConfig = emailConfigMapper.selectByParam(emailConfig);
                sendConfig.setSubject(emailConfig.getSubject());
                sendConfig.setEmailTo(Arrays.asList(adminInfo.getLoginName()));
                //发送内容
                content = String.format(emailConfig.getAdviceTemplate(), nickName, contact, lessonTime);
                sendConfig.setContent(content);

            }
        }//调试课
        else if (lesson.getConsType() == 3) {
            adminInfo = adminInfoMapper.selectAdminInfoByAdminId(student.getCcId());
            emailConfig.setAdviceType(2);
            emailConfig.setAdviceUserType(1);
            emailConfig.setAdviceStatus(1);
            emailConfig = emailConfigMapper.selectByParam(emailConfig);
            sendConfig.setSubject(emailConfig.getSubject());
            sendConfig.setEmailTo(Arrays.asList(adminInfo.getLoginName()));
            content = String.format(emailConfig.getAdviceTemplate(), lessonTime, nickName, contact);
            sendConfig.setContent(content);
        }
        //试听课
        else if (lesson.getConsType() == 4) {
            adminInfo = adminInfoMapper.selectAdminInfoByAdminId(student.getCcId());
            emailConfig.setAdviceType(2);
            emailConfig.setAdviceUserType(3);
            emailConfig.setAdviceStatus(1);
            emailConfig = emailConfigMapper.selectByParam(emailConfig);
            sendConfig.setSubject(emailConfig.getSubject());
            sendConfig.setEmailTo(Arrays.asList(adminInfo.getLoginName()));
            content = String.format(emailConfig.getAdviceTemplate(), lessonTime, nickName, contact);
            sendConfig.setContent(content);
        }
        sendMail(sendConfig);

    }


    /**
     * 老师请假
     *
     * @param periodId
     * @param studentId
     */
    public void sendTeacherAskForLevelEmail(int periodId, int studentId) {
        StudentLesson lesson = studentLessonMapper.getOrderLessonByPeriodId(periodId);
        StudentInfo student = talkUserService.getStudentInfoByUserId(studentId);
        TeacherInfo teacherInfo = talkUserService.getTeacherInfoByUserId(lesson.getTeacherId());
        String lessonTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lesson.getBeginTime());
        AdminInfo adminInfo = null;
        EmailConfig emailConfig = new EmailConfig();
        SendConfig sendConfig = new SendConfig();
        //todo get EA list
        if (lesson.getConsType() == 1) {
            adminInfo = talkUserService.getAdminInfoByAdminId(student.getCrId());
        } else {
            adminInfo = talkUserService.getAdminInfoByAdminId(student.getCcId());
        }
        emailConfig.setAdviceStatus(1);
        emailConfig.setAdviceUserType(2);
        emailConfig.setAdviceType(3);
        emailConfig = emailConfigMapper.selectByParam(emailConfig);
        sendConfig.setSubject(emailConfig.getSubject());
        sendConfig.setEmailTo(Arrays.asList(adminInfo.getLoginName()));
        String content = String.format(emailConfig.getAdviceTemplate(), teacherInfo.getTeacherName(), lessonTime, student.getCnNickname(), getLessonTypeName(lesson.getConsType()), teacherInfo.getTeacherName(), teacherInfo.getEmail());
        sendConfig.setContent(content);
        sendMail(sendConfig);
    }

    public void sendStudentAskForLevelEmail(int periodId, int studentId) throws ParseException {
        StudentLesson lesson = studentLessonMapper.getOrderLessonByPeriodId(periodId);
        StudentInfo student = talkUserService.getStudentInfoByUserId(studentId);
        TeacherInfo teacher = talkUserService.getTeacherInfoByUserId(lesson.getTeacherId());
        String lessonTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lesson.getBeginTime());
        String teacherLocalTime = convertToEnglish(lesson.getBeginTime());
        AdminInfo adminInfo = null;
        String content = null;
        EmailConfig emailConfig = new EmailConfig();
        SendConfig sendConfig = new SendConfig();
        //通知老师
        emailConfig.setAdviceStatus(1);
        emailConfig.setAdviceType(3);
        emailConfig.setAdviceUserType(1);
        emailConfig = emailConfigMapper.selectByParam(emailConfig);
        sendConfig.setSubject(emailConfig.getSubject());
        sendConfig.setEmailTo(Arrays.asList(teacher.getEmail()));
        content = String.format(emailConfig.getAdviceTemplate(), teacher.getTeacherName(), student.getEnNickname(), teacherLocalTime);
        sendConfig.setContent(content);
        sendMail(sendConfig);

        //通知CC,CR,EA
        //todo get EA list
        if (lesson.getConsType() == 1) {
            adminInfo = talkUserService.getAdminInfoByAdminId(student.getCrId());
        } else {
            adminInfo = talkUserService.getAdminInfoByAdminId(student.getCcId());
        }
        emailConfig.setAdviceStatus(1);
        emailConfig.setAdviceUserType(3);
        emailConfig.setAdviceType(3);
        emailConfig = emailConfigMapper.selectByParam(emailConfig);
        sendConfig.setSubject(emailConfig.getSubject());
        sendConfig.setEmailTo(Arrays.asList(adminInfo.getLoginName()));
        content = String.format(emailConfig.getAdviceTemplate(), student.getCnNickname(), lessonTime, teacher.getTeacherName(), getLessonTypeName(lesson.getConsType()), teacher.getTeacherName(), teacher.getEmail());
        sendConfig.setContent(content);
        sendMail(sendConfig);

    }

    public void sendStudentAskForLevelInFiveMinutesEmail(int periodId, int studentId) {
        EmailConfig emailConfig = new EmailConfig();
        SendConfig sendConfig = new SendConfig();
        StudentLesson lesson = studentLessonMapper.getOrderLessonByPeriodId(periodId);
        StudentInfo student = talkUserService.getStudentInfoByUserId(studentId);
        TeacherInfo teacher = talkUserService.getTeacherInfoByUserId(lesson.getTeacherId());
        String lessonTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lesson.getBeginTime());
        AdminInfo adminInfo = null;
        //通知CC,CR,EA
        //todo get EA list
        if (lesson.getConsType() == 1) {
            adminInfo = talkUserService.getAdminInfoByAdminId(student.getCrId());
        } else {
            adminInfo = talkUserService.getAdminInfoByAdminId(student.getCcId());
        }
        emailConfig.setAdviceStatus(1);
        emailConfig.setAdviceUserType(3);
        emailConfig.setAdviceType(3);
        emailConfig = emailConfigMapper.selectByParam(emailConfig);
        sendConfig.setSubject(emailConfig.getSubject());
        sendConfig.setEmailTo(Arrays.asList(adminInfo.getLoginName()));
        String content = String.format(emailConfig.getAdviceTemplate(), student.getCnNickname(), lessonTime, teacher.getTeacherName(), getLessonTypeName(lesson.getConsType()), teacher.getTeacherName(), teacher.getEmail());
        sendConfig.setContent(content);
        sendMail(sendConfig);

    }

    public void sendTeacherAskForLevelInFiveMinutesEmail(int periodId, int studentId) {
        StudentLesson lesson = studentLessonMapper.getOrderLessonByPeriodId(periodId);
        StudentInfo student = talkUserService.getStudentInfoByUserId(studentId);
        TeacherInfo teacherInfo = talkUserService.getTeacherInfoByUserId(lesson.getTeacherId());
        String lessonTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lesson.getBeginTime());
        AdminInfo adminInfo = null;
        EmailConfig emailConfig = new EmailConfig();
        SendConfig sendConfig = new SendConfig();
        //todo get EA list
        if (lesson.getConsType() == 1) {
            adminInfo = talkUserService.getAdminInfoByAdminId(student.getCrId());
        } else {
            adminInfo = talkUserService.getAdminInfoByAdminId(student.getCcId());
        }
        emailConfig.setAdviceStatus(1);
        emailConfig.setAdviceUserType(2);
        emailConfig.setAdviceType(3);
        emailConfig = emailConfigMapper.selectByParam(emailConfig);
        sendConfig.setSubject(emailConfig.getSubject());
        sendConfig.setEmailTo(Arrays.asList(adminInfo.getLoginName()));
        String content = String.format(emailConfig.getAdviceTemplate(), teacherInfo.getTeacherName(), lessonTime, student.getCnNickname(), getLessonTypeName(lesson.getConsType()), teacherInfo.getTeacherName(), teacherInfo.getEmail());
        sendConfig.setContent(content);
        sendMail(sendConfig);

    }

    /**
     * 课程撤销
     * @param periodId
     * @param studentId
     * @throws ParseException
     */
    public void sendLessonCancelEmail(int periodId, int studentId) throws ParseException {
        StudentLesson lesson = studentLessonMapper.getOrderLessonByPeriodId(periodId);
        StudentInfo student = talkUserService.getStudentInfoByUserId(studentId);
        TeacherInfo teacher = talkUserService.getTeacherInfoByUserId(lesson.getTeacherId());
        String teacherLocalTime = convertToEnglish(lesson.getBeginTime());
        String content = null;
        EmailConfig emailConfig = new EmailConfig();
        SendConfig sendConfig = new SendConfig();
        //通知老师
        emailConfig.setAdviceStatus(1);
        emailConfig.setAdviceType(4);
        emailConfig.setAdviceUserType(1);
        emailConfig = emailConfigMapper.selectByParam(emailConfig);
        sendConfig.setSubject(emailConfig.getSubject());
        sendConfig.setEmailTo(Arrays.asList(teacher.getEmail()));
        content = String.format(emailConfig.getAdviceTemplate(), teacher.getTeacherName(), student.getEnNickname(), teacherLocalTime);
        sendConfig.setContent(content);
        sendMail(sendConfig);
    }


}
