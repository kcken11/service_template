package com.melot.talkee.driver.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Title:学生对老师的课堂反馈
 * <p>
 * Description: hist_student_comment_teacher
 * </p>
 *
 * @author 范玉全<a href="mailto:yuquan.fan@melot.cn">
 * @version V1.0
 * @since 10:20 2017/7/20
 */
public class ClassroomComment implements Serializable {

    private Integer histId;
    /**
     * 老师id
     */
    private Integer teacherId;
    /**
     * 老师姓名
     */
    private String teacherName;
    /**
     * 学生id
     */
    private Integer userId;
    /**
     * 学生姓名
     */
    private String studentName;
    /**
     * 课程id
     */
    private Integer periodId;

    //课堂评价
    //默认：0； 1星：一般  ；2星：有待提高；3星：不错；4星：满意；5星：无可挑剔

    /**
     * 视频清晰度
     */
    private Integer videoSharpness;
    /**
     * 声音清晰度
     */
    private Integer soundArticulation;
    /**
     * 课堂气氛
     */
    private Integer atmosphere;
    /**
     * 课程内容
     */
    private Integer interaction;
    /**
     * 给老师提点小要求
     */
    private List<Requirement> requirements;

    private String requireIds;

    /**
     * 评论增加时间
     */
    private Date commentTime;

    public Integer getHistId() {
        return histId;
    }

    public void setHistId(Integer histId) {
        this.histId = histId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public Integer getVideoSharpness() {
        return videoSharpness;
    }

    public void setVideoSharpness(Integer videoSharpness) {
        this.videoSharpness = videoSharpness;
    }

    public Integer getSoundArticulation() {
        return soundArticulation;
    }

    public void setSoundArticulation(Integer soundArticulation) {
        this.soundArticulation = soundArticulation;
    }

    public Integer getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Integer atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Integer getInteraction() {
        return interaction;
    }

    public void setInteraction(Integer interaction) {
        this.interaction = interaction;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public String getRequireIds() {
        return requireIds;
    }

    public void setRequireIds(String requireIds) {
        this.requireIds = requireIds;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
