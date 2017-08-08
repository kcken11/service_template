package com.melot.talkee.service.impl;

import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.dao.*;
import com.melot.talkee.driver.domain.*;
import com.melot.talkee.driver.service.TalkLessonService;
import com.melot.talkee.driver.service.TalkUserService;
import com.melot.talkee.redis.LessonRedisSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TalkLessonServiceImpl implements TalkLessonService {

    @Autowired
    private StudentLessonMapper studentLessonMapper;

    @Autowired
    private  TurnStudentLessonMapper turnStudentLessonMapper;

    @Autowired
    private UserStudentMapper userStudentMapper;

    @Autowired
    private UserTeacherMapper userTeacherMapper;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private LessonLevelMapper lessonLevelMapper;

    @Autowired
    private CoursewareMapper coursewareMapper;

    @Autowired
    private RequirementMapper requirementMapper;

    @Autowired
    private DetailCommentQuestionMapper detailCommentQuestionMapper;

    @Autowired
    private TeacherDetailCommentMapper teacherDetailCommentMapper;

    @Autowired
    private ClassroomCommentMapper classroomCommentMapper;

    @Autowired
    private TeacherCommentToStudentMapper teacherCommentToStudentMapper;

    @Autowired
    private TalkUserService talkUserService;

    @Override
    public Lesson createLesson(Integer lessonType, String lessonName,
                               Integer lessonLevel, String lessonTitle, String lessonUrl,
                               Integer lessonRank, String content, Integer lessonDuration,
                               Integer subLevel) {
        Lesson lesson = new Lesson();
        lesson.setConfTime(new Date());
        lesson.setContent(content);
        lesson.setLessonDuration(lessonDuration);
        lesson.setLessonLevel(lessonLevel);
        lesson.setLessonName(lessonName);
        lesson.setLessonRank(lessonRank);
        lesson.setLessonTitle(lessonTitle);
        lesson.setLessonType(lessonType);
        lesson.setLessonUrl(lessonUrl);
        lesson.setSubLevel(subLevel);
        int code = lessonMapper.insertSelective(lesson);
        if (code > 0) {
            return lesson;
        }
        return null;
    }

    @Override
    public List<Lesson> getLessonList(Integer lessonLevel, Integer lessonType,
                                      Integer status, String lessonName, String title) {

        Lesson lesson = new Lesson();
        if (lessonLevel != null) {
            lesson.setLessonLevel(lessonLevel);
        }
        if (lessonType != null) {
            lesson.setLessonType(lessonType);
        }
        if (status != null) {
            lesson.setStatus(status);
        }
        return lessonMapper.selectSelective(lesson);
    }

    @Override
    public Lesson modifyLesson(Lesson lesson) {
        if (lesson != null && lesson.getLessonId() != null) {
            int code = lessonMapper.updateByPrimaryKeySelective(lesson);
            if (code > 0) {
                return lessonMapper.selectByPrimaryKey(lesson.getLessonId());
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#getLessonById(java.lang.Integer)
     */
    @Override
    public Lesson getLessonById(Integer lessonId) {
        Lesson lesson = null;
        if (lessonId != null) {
            lesson = LessonRedisSource.getConfLessonCache(lessonId);
            if (lesson == null) {
                lesson = lessonMapper.selectByPrimaryKey(lessonId);
                if (lesson != null) {
                    String lessonUrl = lesson.getLessonUrl();
                    if (StringUtils.isNotBlank(lessonUrl)) {
                        String yunUrl = "";
                        if (lessonUrl.startsWith("aliyun")) {
                            yunUrl = MelotBeanFactory.getBean("aliyun", String.class);
                        }
                        lessonUrl = yunUrl + "/" + lessonUrl;
                        lesson.setLessonUrl(lessonUrl);
                    }

                    String originalLessonUrl = lesson.getOriginalLessonUrl();
                    if (StringUtils.isNotBlank(originalLessonUrl)) {
                        String yunUrl = "";
                        if (originalLessonUrl.startsWith("aliyun")) {
                            yunUrl = MelotBeanFactory.getBean("aliyun", String.class);
                        }
                        originalLessonUrl = yunUrl + "/" + originalLessonUrl;
                        lesson.setOriginalLessonUrl(originalLessonUrl);
                    }
                    LessonRedisSource.addConfLessonCache(lesson);
                }
            }
        }
        return lesson;
    }


    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#getLessonLevelById(java.lang.Integer)
     */
    @Override
    public LessonLevel getLessonLevelById(Integer levelId, Integer parentLevel) {
        LessonLevel lessonLevel = null;
        if (levelId != null) {
            if (parentLevel == null) {
                parentLevel = 0;
            }
            lessonLevel = LessonRedisSource.getLessonLevelCache(parentLevel, levelId);
            if (lessonLevel == null) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("levelId", levelId);
                param.put("parentLevel", parentLevel);
                lessonLevel = lessonLevelMapper.selectByPrimaryKey(param);
                if (lessonLevel != null) {
                    LessonRedisSource.addLessonLevelCache(lessonLevel);
                }
            }
        }
        return lessonLevel;
    }

    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#getCurrentLessonByUserId(java.lang.Integer)
     */
    @Override
    public Lesson getCurrentLessonByUserId(Integer userId) {
        if (userId != null) {
            // 先获取最后已上课记录
            Integer lessonId = null;
            StudentInfo studentInfo = talkUserService.getStudentInfoByUserId(userId);
            if (studentInfo != null) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("studentId", userId);
                param.put("stateList", Arrays.asList(0, 1));
                OrderLesson nextStudentLesson = studentLessonMapper.getNextOrderLesson(param);
                if (nextStudentLesson != null) {
                    lessonId = nextStudentLesson.getLessonId();
                    return getLessonById(lessonId);
                } else {
                    int sublevel = studentInfo.getSubLevel();
                    int level = studentInfo.getUserLevel();

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("lessonLevel", level);
                    map.put("subLevel", sublevel);
                    Lesson lesson = lessonMapper.selectByLevel(map);
                    if (lesson != null) {
                        lessonId = lesson.getLessonId();
                        return getLessonById(lessonId);
                    }
                }
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#selectByParentLevel(java.lang.Integer)
     */
    @Override
    public List<LessonLevel> selectByParentLevel(Integer parentLevel) {
        if (parentLevel != null) {
            return lessonLevelMapper.selectByParentLevel(parentLevel);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#getCourseware(java.lang.Integer)
     */
    @Override
    public List<Courseware> getCourseware(Integer lessonId) {
        if (lessonId != null) {
            Courseware courseware = new Courseware();
            courseware.setLessonId(lessonId);
            List<Courseware> list = coursewareMapper.selectBySelective(courseware);
            if (list != null && list.size() > 0) {
                List<Courseware> tempList = new ArrayList<Courseware>();
                for (Courseware tCourseware : list) {
                    String coswUrl = tCourseware.getCoswUrl();
                    String yunUrl = "";
                    if (StringUtils.isNotBlank(coswUrl) && coswUrl.startsWith("aliyun")) {
                        yunUrl = MelotBeanFactory.getBean("aliyun", String.class);
                    }
                    coswUrl = yunUrl + "/" + coswUrl;
                    tCourseware.setCoswUrl(coswUrl);
                    tempList.add(tCourseware);
                }
                return tempList;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#uploadCourseware(java.lang.Integer, java.util.Map)
     */
    @Override
    public List<Courseware> uploadCourseware(Integer lessonId, Map<String, String> files, Integer uploadUser) {
        if (lessonId != null && (files != null && files.size() > 0)) {
            List<Courseware> fileList = new ArrayList<Courseware>();

            for (Map.Entry<String, String> entry : files.entrySet()) {
                String fileName = entry.getKey();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                Courseware courseware = new Courseware();
                if (StringUtils.isNumeric(name)) {
                    courseware.setGrank(Integer.valueOf(name));
                } else {

                }
                courseware.setAdmId(uploadUser);
                courseware.setCoswUrl(entry.getValue());
                courseware.setDtime(new Date());
                courseware.setLessonId(lessonId);
                if (coursewareMapper.insertSelective(courseware) > 0) {
                    fileList.add(courseware);
                }
            }
            return fileList;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#getDetailCommentQuestion(java.lang.String, java.lang.Integer)
     */
    @Override
    public List<DetailCommentQuestion> getDetailCommentQuestion(String questionType, Integer state) {
        DetailCommentQuestion detailCommentQuestion = new DetailCommentQuestion();
        detailCommentQuestion.setState(state);
        detailCommentQuestion.setQuestionType(questionType);
        return detailCommentQuestionMapper.selectBySelective(detailCommentQuestion);
    }


    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#getDetailCommentQuestionById(java.lang.Integer)
     */
    @Override
    public DetailCommentQuestion getDetailCommentQuestionById(Integer questionId) {
        if (questionId != null) {
            return detailCommentQuestionMapper.selectByPrimaryKey(questionId);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkLessonService#addDetailCommentQuestionById(com.melot.talkee.driver.domain.DetailCommentQuestion)
     */
    @Override
    public DetailCommentQuestion addDetailCommentQuestion(DetailCommentQuestion detailCommentQuestion) {
        if (detailCommentQuestion != null && StringUtils.isNotBlank(detailCommentQuestion.getQuestion()) && StringUtils.isNotBlank(detailCommentQuestion.getQuestionType())) {
            detailCommentQuestion.setDtime(new Date());
            detailCommentQuestion.setState(1);
            if (detailCommentQuestionMapper.insertSelective(detailCommentQuestion) > 0) {
                return detailCommentQuestion;
            }
        }
        return null;
    }

    @Override
    public List<LessonLevel> getParentLevel() {
        return lessonLevelMapper.getParentLevel();
    }

    @Override
    public Pager<Lesson> getLessonInfoList(Integer lessonLevel, Integer start, Integer offset, String order) {
        if (lessonLevel != null) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("lessonLevel", lessonLevel);

            Pager<Lesson> pager = new Pager<>();
            pager.setStart(start);
            pager.setPageSize(offset);
            pager.setParams(paramMap);
            pager.setOrder(order);

            List<Lesson> list = lessonMapper.findPager(pager);

            if (list != null && list.size() > 0) {
                pager.setPageItems(formatLessonList(list));
            }
            return pager;
        }

        return null;
    }

    /**
     * 通过mybatis关联查询，需要类型转换
     *
     * @param list
     * @return
     */
    private List<Lesson> formatLessonList(List<Lesson> list) {
        if (list != null && list.size() > 0) {
            List<Lesson> tempList = new ArrayList<Lesson>();
            for (Lesson lesson : list) {
                tempList.add(formatLesson(lesson));
            }
            return tempList;
        }
        return null;
    }

    private Lesson formatLesson(Lesson lesson) {
        if (lesson != null) {
            Lesson les = new Lesson();
            BeanUtils.copyProperties(lesson, les);
            return les;
        }
        return null;
    }

    @Override
    @Transactional
    public int createTeacherComment(TeacherCommentToStudent comment) {
        if (comment == null) {
            return 0;
        }
        if (comment.getPeriodId() != null && comment.getStudentId() != null && comment.getTeacherId() != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("teacherId", comment.getTeacherId());
            param.put("periodId", comment.getPeriodId());
            param.put("studentId", comment.getStudentId());
            TeacherCommentToStudent student = teacherCommentToStudentMapper.selectBySelective(param);

            if (student == null || comment.getHistId() == null || comment.getHistId() == 0) {
                TeacherCommentToStudent com = new TeacherCommentToStudent();
                com.setTeacherId(comment.getTeacherId());
                com.setPeriodId(comment.getPeriodId());
                com.setStudentId(comment.getStudentId());

                com.setParticipation(comment.getParticipation());
                com.setPronunciation(comment.getPronunciation());
                com.setComprehension(comment.getComprehension());
                com.setFluency(comment.getFluency());
                com.setCreativity(comment.getCreativity());

                if (comment.getOther() != null) {
                    com.setOther(comment.getOther());
                }
                if (comment.getSuggestion() != null) {
                    com.setSuggestion(comment.getSuggestion());
                }
                if (comment.getSummary() != null) {
                    com.setSummary(comment.getSummary());
                }
                com.setdTime(new Date());
                int code = teacherCommentToStudentMapper.insertSelective(com);
                if (code > 0) {
                    StudentLesson studentLesson = studentLessonMapper.getOrderLessonByPeriodId(comment.getPeriodId());
                    if (studentLesson != null) {
                        studentLesson.setIsTeacherComment(1);

                        studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                    }

                    TurnStudentLesson turnStudentLesson = turnStudentLessonMapper.selectByPeriodId(comment.getPeriodId());
                    if (turnStudentLesson != null) {
                        turnStudentLesson.setIsTeacherComment(1);

                        turnStudentLessonMapper.updateByPrimaryKeySelective(turnStudentLesson);
                    }
                }

                return code;
            } else if (student != null && comment.getHistId() != null && comment.getHistId() != 0) {
                TeacherCommentToStudent studentComment = teacherCommentToStudentMapper.selectByPrimaryKey(comment.getHistId());
                if (studentComment != null) {
                    studentComment.setComprehension(comment.getComprehension());
                    studentComment.setCreativity(comment.getCreativity());
                    studentComment.setFluency(comment.getFluency());
                    studentComment.setPronunciation(comment.getPronunciation());
                    studentComment.setParticipation(comment.getParticipation());
                    if (comment.getOther() != null) {
                        studentComment.setOther(comment.getOther());
                    }
                    if (comment.getSuggestion() != null) {
                        studentComment.setSuggestion(comment.getSuggestion());
                    }
                    if (comment.getSummary() != null) {
                        studentComment.setSummary(comment.getSummary());
                    }

                    int code = teacherCommentToStudentMapper.updateByPrimaryKeySelective(studentComment);

                    return code;
                }
            }

        }

        return 0;
    }

    @Override
    public TeacherCommentToStudent getCommentByKey(Integer histId) {
        if(histId == null || histId == 0){
            return null;
        }

        return teacherCommentToStudentMapper.selectByPrimaryKey(histId);
    }


    @Override
    public TeacherCommentToStudent getTeacherCommentInfo(Integer teacherId, Integer periodId, Integer studentId) {
        if (periodId != null && studentId != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("teacherId", teacherId);
            param.put("periodId", periodId);
            param.put("studentId", studentId);

            TeacherCommentToStudent comment = teacherCommentToStudentMapper.selectBySelective(param);

            StudentInfo studentInfo = userStudentMapper.selectByPrimaryKey(studentId);
            TeacherInfo teacherInfo = userTeacherMapper.selectByPrimaryKey(comment.getTeacherId());
            if (studentInfo != null && teacherInfo != null) {
                comment.setTeacherName(teacherInfo.getTeacherName());
                comment.setPortraitUrl(teacherInfo.getPortrait());
                comment.setStudentName(studentInfo.getEnNickname());
                comment.setStuPortraitUrl(studentInfo.getPortrait());
            }

            return comment;
        }

        return null;
    }

    @Override
    @Transactional
    public int createStudentComment(ClassroomComment comment) {
        if (comment == null) {
            return 0;
        }
        if (comment.getTeacherId() != null && comment.getPeriodId() != null && comment.getUserId() != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("teacherId", comment.getTeacherId());
            param.put("periodId", comment.getPeriodId());
            param.put("userId", comment.getUserId());

            ClassroomComment classroomComment = classroomCommentMapper.selectBySelective(param);
            ClassroomComment com = new ClassroomComment();
            com.setTeacherId(comment.getTeacherId());
            com.setPeriodId(comment.getPeriodId());
            com.setUserId(comment.getUserId());

            com.setSoundArticulation(comment.getSoundArticulation());
            com.setAtmosphere(comment.getAtmosphere());
            com.setVideoSharpness(comment.getVideoSharpness());
            com.setInteraction(comment.getInteraction());

            com.setRequireIds(comment.getRequireIds());

            com.setCommentTime(new Date());
            if (classroomComment == null) {
                int code = classroomCommentMapper.insertSelective(com);
                if(code > 0){
                    //更新hist_student_lesson学生是否发表评论状态（0：没有；1：有）
                    StudentLesson studentLesson = studentLessonMapper.getOrderLessonByPeriodId(comment.getPeriodId());
                    if(studentLesson != null){
                        studentLesson.setIsStudentComment(1);

                        studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                    }

                    TurnStudentLesson turnStudentLesson = turnStudentLessonMapper.selectByPeriodId(comment.getPeriodId());
                    if(turnStudentLesson != null){
                        turnStudentLesson.setIsStudentComment(1);

                        turnStudentLessonMapper.updateByPrimaryKeySelective(turnStudentLesson);
                    }

                }

                return code;
            } else {
                return 0;
            }
        }

        return 0;
    }


    @Override
    public ClassroomComment getStudentCommentInfo(Integer teacherId, Integer periodId, Integer studentId) {
        if (teacherId != null && periodId != null && studentId != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("teacherId", teacherId);
            param.put("periodId", periodId);
            param.put("userId", studentId);

            ClassroomComment comment = classroomCommentMapper.selectBySelective(param);
            if(comment == null){
                return null;
            }
            StudentInfo studentInfo = userStudentMapper.selectByPrimaryKey(studentId);
            TeacherInfo teacherInfo = userTeacherMapper.selectByPrimaryKey(teacherId);
            if (studentInfo != null && teacherInfo != null) {
                comment.setTeacherName(teacherInfo.getTeacherName());
                comment.setStudentName(studentInfo.getEnNickname());
            }

            param.clear();

            if(StringUtils.isBlank(comment.getRequireIds())){
                return comment;
            } else {
                String[] requireId = comment.getRequireIds().split(",");
                List<Integer> requireIds = new ArrayList<>();
                for (String string : requireId) {
                    requireIds.add(Integer.valueOf(string.trim()));
                }

                param.put("requireIds", requireIds);

                List<Requirement> list = requirementMapper.selectRequirement(param);

                comment.setRequirements(list);

                return comment;
            }
        }

        return null;
    }

    @Override
    public List<Requirement> getRequirementList() {
        return requirementMapper.selectRequirementList();
    }

    @Transactional
    @Override
    public int updateStdentCancelType(Integer histId, Integer type) {
        StudentLesson studentLesson = studentLessonMapper.selectByPrimaryKey(histId);
        if(studentLesson != null){
            studentLesson.setType(0);

            studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
        }
        return 0;
    }

}
