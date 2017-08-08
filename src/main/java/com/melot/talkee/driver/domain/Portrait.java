package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class Portrait  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer portraitId;

    private String portraitUrl;

    private Date uploadTime;

    private Integer status;

    private Integer checkAid;
    
    private Integer userId;

    /**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(Integer portraitId) {
        this.portraitId = portraitId;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl == null ? null : portraitUrl.trim();
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCheckAid() {
        return checkAid;
    }

    public void setCheckAid(Integer checkAid) {
        this.checkAid = checkAid;
    }
}