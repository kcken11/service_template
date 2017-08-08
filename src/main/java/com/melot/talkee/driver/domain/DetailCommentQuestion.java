package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class DetailCommentQuestion implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer questionId;

    private String question;

    private Integer state;

    private Date dtime;

    private String questionType;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getDtime() {
        return dtime;
    }

    public void setDtime(Date dtime) {
        this.dtime = dtime;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType == null ? null : questionType.trim();
    }
}