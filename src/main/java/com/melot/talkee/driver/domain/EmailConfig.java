package com.melot.talkee.driver.domain;

public class EmailConfig {
    private Integer templateId;

    private Integer adviceType;

    private Integer adviceUserType;

    private String adviceTemplate;

    private Integer adviceStatus;

    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getAdviceType() {
        return adviceType;
    }

    public void setAdviceType(Integer adviceType) {
        this.adviceType = adviceType;
    }

    public Integer getAdviceUserType() {
        return adviceUserType;
    }

    public void setAdviceUserType(Integer adviceUserType) {
        this.adviceUserType = adviceUserType;
    }

    public String getAdviceTemplate() {
        return adviceTemplate;
    }

    public void setAdviceTemplate(String adviceTemplate) {
        this.adviceTemplate = adviceTemplate == null ? null : adviceTemplate.trim();
    }

    public Integer getAdviceStatus() {
        return adviceStatus;
    }

    public void setAdviceStatus(Integer adviceStatus) {
        this.adviceStatus = adviceStatus;
    }
}