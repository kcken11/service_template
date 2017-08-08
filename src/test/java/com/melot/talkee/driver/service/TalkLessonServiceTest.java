package com.melot.talkee.driver.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.fail;

public class TalkLessonServiceTest {

    private static TalkLessonService talkLessonService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Main.startClient("classpath*:conf/client-spring-config.xml");
        talkLessonService = MelotBeanFactory.getBean("talkLessonService", TalkLessonService.class);
    }

    @Test
    public void testCreateLesson() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetLessonList() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetParentLevel() {
        List<LessonLevel> list = talkLessonService.getParentLevel();
        System.out.print(JSONObject.toJSONString(list));
    }


    @Test
    public void testCreateTeacherComment() {
        TeacherCommentToStudent comment = new TeacherCommentToStudent();
        comment.setHistId(11);
        comment.setTeacherId(258);
        comment.setPeriodId(4403);
        comment.setStudentId(629);

        comment.setOther("xxxxxxxxxxxxx");
        comment.setSuggestion("cccXXXX");
        comment.setSummary("xxxxxxxx");

        comment.setParticipation(4);
        comment.setPronunciation(4);
        comment.setComprehension(4);
        comment.setFluency(5);
        comment.setCreativity(5);

        int code = talkLessonService.createTeacherComment(comment);

        System.out.print(code);

    }

    @Test
    public void testCreateStudentComment() {
        ClassroomComment comment = new ClassroomComment();
        comment.setTeacherId(253);
        comment.setPeriodId(4403);
        comment.setUserId(629);

        comment.setSoundArticulation(4);
        comment.setVideoSharpness(4);
        comment.setInteraction(4);
        comment.setAtmosphere(4);

        comment.setRequireIds("1,2,3");

        int code = talkLessonService.createStudentComment(comment);

        System.out.print(code);
    }


    @Test
    public void testGetRequirementList(){
        List<Requirement> list = talkLessonService.getRequirementList();
        System.out.print(JSONObject.toJSONString(list));
    }

    @Test
    public void testUpdateStudentCancelType(){
        int code = talkLessonService.updateStdentCancelType(1830,0);
        System.out.print(JSONObject.toJSONString(code));
    }



    @Test
    public void testGetStudentCommentInfo(){
        ClassroomComment comment = talkLessonService.getStudentCommentInfo(361,5293,144);
        System.out.print(JSONObject.toJSONString(comment));
    }

    @Test
    public void testGetTeacherCommentInfo(){
        TeacherCommentToStudent comment = talkLessonService.getTeacherCommentInfo(22,2543,58);
        System.out.print(JSONObject.toJSONString(comment));
    }


    @Test
    public void testGetLessonInfo() {
        Pager<Lesson> lessonPager = talkLessonService.getLessonInfoList(7, 1, 10, "desc");
        System.out.print(JSONObject.toJSONString(lessonPager));
    }

    @Test
    public void testGetLessonById() {
        talkLessonService.getLessonById(17);
    }

    @Test
    public void testGetLesson() {
        talkLessonService.getCourseware(1);
    }


    @Test
    public void testGetCurrentLesson() {

        System.out.println(new Gson().toJson(talkLessonService.getCurrentLessonByUserId(25)));

    }

    @Test
    public void testGetLessonLevelById() {
        fail("Not yet implemented");
    }

    @Test
    public void testSelectByParentLevel() {

        System.out.println(new Gson().toJson(talkLessonService.selectByParentLevel(0)));
        ;
    }

    @Test
    public void testModifyLesson() {
        fail("Not yet implemented");
    }

}
