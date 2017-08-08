package com.melot.talkee.driver.domain;

import java.io.Serializable;

public class UserResource  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer relId;

    private Integer userId;

    private Integer resType;

    private Integer resId;

    private Integer resValue;

    public Integer getRelId() {
        return relId;
    }

    public void setRelId(Integer relId) {
        this.relId = relId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getResType() {
        return resType;
    }

    public void setResType(Integer resType) {
        this.resType = resType;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public Integer getResValue() {
        return resValue;
    }

    public void setResValue(Integer resValue) {
        this.resValue = resValue;
    }
}