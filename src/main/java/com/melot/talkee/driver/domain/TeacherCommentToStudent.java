package com.melot.talkee.driver.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * Title:老师对学生的评价
 * <p>
 * Description:hist_teacher_detail_comment
 * </p>
 *
 * @author 范玉全<a href="mailto:yuquan.fan@melot.cn">
 * @version V1.0
 * @since 10:05 2017/7/20
 */
public class TeacherCommentToStudent implements Serializable {

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
     * 老师头像url
     */
    private String portraitUrl;
    /**
     * 学生id
     */
    private Integer studentId;
    /**
     * 学生姓名(英文)
     */
    private String studentName;
    /**
     * 学生头像url
     */
    private String stuPortraitUrl;
    /**
     * 课程id
     */
    private Integer periodId;

    //课堂点评评分（默认：0，）
    //1星：Poor  ,2星：Fair，3星：Satisfactory，4星：Good，5星：Excellent
    /**
     * 发音
     */
    private Integer pronunciation;
    /**
     * 参与度
     */
    private Integer participation;
    /**
     * 理解力
     */
    private Integer comprehension;
    /**
     * 流畅度
     */
    private Integer fluency;
    /**
     * 创造力
     */
    private Integer creativity;

    /**
     * 课堂总结
     */
    private String summary;
    /**
     * 老师建议
     */
    private String suggestion;
    /**
     * 其他
     */
    private String other;
    /**
     * 评论增加时间
     */
    private Date dTime;

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

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
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

    public Integer getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(Integer pronunciation) {
        this.pronunciation = pronunciation;
    }

    public Integer getParticipation() {
        return participation;
    }

    public void setParticipation(Integer participation) {
        this.participation = participation;
    }

    public Integer getComprehension() {
        return comprehension;
    }

    public void setComprehension(Integer comprehension) {
        this.comprehension = comprehension;
    }

    public Integer getFluency() {
        return fluency;
    }

    public void setFluency(Integer fluency) {
        this.fluency = fluency;
    }

    public Integer getCreativity() {
        return creativity;
    }

    public void setCreativity(Integer creativity) {
        this.creativity = creativity;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Date getdTime() {
        return dTime;
    }

    public void setdTime(Date dTime) {
        this.dTime = dTime;
    }

    public String getStuPortraitUrl() {
        return stuPortraitUrl;
    }

    public void setStuPortraitUrl(String stuPortraitUrl) {
        this.stuPortraitUrl = stuPortraitUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
