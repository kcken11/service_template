package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class TeacherDetailComment implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer histId;

    private Integer questionId;

    private String teacherAnswer;

    private Integer detailId;

    private Date dtime;

    public Integer getHistId() {
        return histId;
    }

    public void setHistId(Integer histId) {
        this.histId = histId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getTeacherAnswer() {
        return teacherAnswer;
    }

    public void setTeacherAnswer(String teacherAnswer) {
        this.teacherAnswer = teacherAnswer == null ? null : teacherAnswer.trim();
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Date getDtime() {
        return dtime;
    }

    public void setDtime(Date dtime) {
        this.dtime = dtime;
    }
}