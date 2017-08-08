package com.melot.talkee.driver.service;


import com.melot.talkee.driver.domain.*;

import java.util.List;
import java.util.Map;

/**
 * Title: LessonService
 * Description: 课程服务接口
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-04-05 15:25:53
 */
public interface TalkLessonService {


	/**
	 * 创建课程配置信息
	 * @param lessonType
	 * @param lessonName
	 * @param lessonLevel
	 * @param lessonTitle
	 * @param lessonUrl
	 * @param lessonRank
	 * @param content
	 * @param lessonDuration
	 * @param subLevel
	 * @return 课程信息
	 */
	Lesson createLesson(Integer lessonType,String lessonName,Integer lessonLevel,String lessonTitle,String lessonUrl,Integer lessonRank,String content,Integer lessonDuration,Integer subLevel);

	/**
	 * 获取程配置信息列表
	 * @param lessonLevel
	 * @param lessonType
	 * @param status
	 * @param lessonName
	 * @param title
	 * @return 课程信息列表
	 */
	List<Lesson> getLessonList(Integer lessonLevel,Integer lessonType,Integer status,String lessonName,String title);


	/**
	 * 通过课程ID获取课程信息
	 * @param lessonId
	 * @return 课程信息
	 */
	Lesson getLessonById(Integer lessonId);


	/**
	 * 获取当前用户课程信息(最后已上课或最近要上课)
	 * @param userId
	 * @return 当前课程信息
	 */
	Lesson getCurrentLessonByUserId(Integer userId);

	/**
	 * 通过课程等级获取等级信息
	 * @param levelId
	 * @return 课程等级
	 */
	LessonLevel getLessonLevelById(Integer levelId,Integer parentLevel);

	/**
	 * 通过父等级获取所有子等级
	 * @param parentLevel
	 * @return 子等级列表
	 */
	List<LessonLevel> selectByParentLevel(Integer parentLevel);

	/**
	 * 修改程配置信息（主键必须存在）
	 * @param lesson
	 * @return Lesson 修改后信息
	 */
	Lesson modifyLesson(Lesson lesson);


	/**
	 * 通过课程ID获取课件信息
	 * @param lessonId
	 * @return 课件文件列表
	 */
	List<Courseware> getCourseware(Integer lessonId);


	/**
	 * 课件上传
	 * @param lessonId
	 * @param files
	 * @param uploadUser
	 * @return 课件文件列表
	 */
	List<Courseware> uploadCourseware(Integer lessonId, Map<String, String> files,Integer uploadUser);


	/**
	 * 获取详细评价问题
	 * @param questionId
	 * @return 详细评论问题
	 */
	DetailCommentQuestion getDetailCommentQuestionById(Integer questionId);

	/**
	 * 添加详细评价问题
	 * @param detailCommentQuestion
	 * @return 详细评论问题
	 */
	DetailCommentQuestion addDetailCommentQuestion(DetailCommentQuestion detailCommentQuestion);


	/**
	 * 根据问题类型获取对应问题列表
	 * @param questionType
	 * @return 详细评论问题列表
	 */
	List<DetailCommentQuestion> getDetailCommentQuestion(String questionType,Integer state);

    //下载课程
	/**
	 * 获取课程大等级（查conf_lesson_level，parent_level=0）
	 * @return
	 */
	List<LessonLevel> getParentLevel();

	/**
	 * 根据课程等级，查看课程名称，url (conf_lesson)
	 * @param lessonLevel
	 * @return
	 */
	Pager<Lesson> getLessonInfoList(Integer lessonLevel, Integer start, Integer offset,String order);

    //评论
    /**
     * 老师添加评语
     *
     * @return
     */
    int createTeacherComment(TeacherCommentToStudent comment);

    /**
     * 根据主键查看评论信息
     * @param histId
     * @return
     */
	TeacherCommentToStudent getCommentByKey(Integer histId);

    /**
     * 查看老师评语
     *
     * @param studentId 学生id
     * @param periodId  课程id
     * @return
     */
    TeacherCommentToStudent getTeacherCommentInfo(Integer teacherId, Integer periodId,Integer studentId);

    /**
     * 学生添加课堂反馈
     *
     * @return
     */
    int createStudentComment(ClassroomComment comment);

    /**
     * 老师查看学生课堂反馈
     *
     * @param teacherId 老师id
     * @param studentId 学生id
     * @param periodId  课程id
     * @return
     */
    ClassroomComment getStudentCommentInfo(Integer teacherId, Integer periodId, Integer studentId);

	/**
	 * 获取学生小要求配置列表
	 * @return
	 */
	List<Requirement> getRequirementList();

    /**
     * 更新学生请假，老师确认的状态
     * @param histId
     * @param type
     * @return
     */
	int updateStdentCancelType(Integer histId,Integer type);

}
