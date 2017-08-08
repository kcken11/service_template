package com.melot.talkee.driver.domain;

import java.util.Date;

public class Lesson extends RespMsg{

	private static final long serialVersionUID = 1L;

	private Integer lessonId;

    private Integer lessonType;

    private String lessonName;

    private Integer lessonLevel;

    private String lessonTitle;

    private String lessonUrl;

    private Date confTime;

    private Integer lessonRank;

    private Integer overCnt;

    private String content;

    private Integer lessonDuration;

    private Integer subLevel;

    private Integer status;
    
    private String originalLessonUrl;

    
    /**
     * @return the originalLessonUrl
     */
    public String getOriginalLessonUrl() {
        return originalLessonUrl;
    }

    
    /**
     * @param originalLessonUrl the originalLessonUrl to set
     */
    public void setOriginalLessonUrl(String originalLessonUrl) {
        this.originalLessonUrl = originalLessonUrl;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getLessonType() {
        return lessonType;
    }

    public void setLessonType(Integer lessonType) {
        this.lessonType = lessonType;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName == null ? null : lessonName.trim();
    }

    public Integer getLessonLevel() {
        return lessonLevel;
    }

    public void setLessonLevel(Integer lessonLevel) {
        this.lessonLevel = lessonLevel;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle == null ? null : lessonTitle.trim();
    }

    public String getLessonUrl() {
        return lessonUrl;
    }

    public void setLessonUrl(String lessonUrl) {
        this.lessonUrl = lessonUrl == null ? null : lessonUrl.trim();
    }

    public Date getConfTime() {
        return confTime;
    }

    public void setConfTime(Date confTime) {
        this.confTime = confTime;
    }

    public Integer getLessonRank() {
        return lessonRank;
    }

    public void setLessonRank(Integer lessonRank) {
        this.lessonRank = lessonRank;
    }

    public Integer getOverCnt() {
        return overCnt;
    }

    public void setOverCnt(Integer overCnt) {
        this.overCnt = overCnt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getLessonDuration() {
        return lessonDuration;
    }

    public void setLessonDuration(Integer lessonDuration) {
        this.lessonDuration = lessonDuration;
    }

    public Integer getSubLevel() {
        return subLevel;
    }

    public void setSubLevel(Integer subLevel) {
        this.subLevel = subLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}