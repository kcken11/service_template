package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mn on 2017/7/25.
 */
public class LessonRecord implements Serializable {
   /*课堂ID*/
    private Integer periodId;
  /**课程ID**/
    private Integer lessonId;
    /**课程名称**/
    private String lessonName;
    /**学生视频记录**/
    private VideoRecording studentVideo;
   /**老师视频记录**/
    private VideoRecording teacherVideo;

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public VideoRecording getStudentVideo() {
        return studentVideo;
    }

    public void setStudentVideo(VideoRecording studentVideo) {
        this.studentVideo = studentVideo;
    }

    public VideoRecording getTeacherVideo() {
        return teacherVideo;
    }

    public void setTeacherVideo(VideoRecording teacherVideo) {
        this.teacherVideo = teacherVideo;
    }
}
