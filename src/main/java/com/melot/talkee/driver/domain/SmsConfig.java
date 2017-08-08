package com.melot.talkee.driver.domain;

public class SmsConfig {
    private Integer templateId;

    private Integer adviceType;

    private Integer adviceUserType;

    private String adviceTemplate;

    private Integer adviceStatus;
    
    private Integer dailyCount;
    
    private Integer activeTime;
    
    
    /**
	 * @return the activeTime
	 */
	public Integer getActiveTime() {
		return activeTime;
	}

	/**
	 * @param activeTime the activeTime to set
	 */
	public void setActiveTime(Integer activeTime) {
		this.activeTime = activeTime;
	}

	/**
	 * @return the dailyCount
	 */
	public Integer getDailyCount() {
		return dailyCount;
	}

	/**
	 * @param dailyCount the dailyCount to set
	 */
	public void setDailyCount(Integer dailyCount) {
		this.dailyCount = dailyCount;
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