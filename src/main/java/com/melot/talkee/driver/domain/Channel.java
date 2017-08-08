package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class Channel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer channelId;

    private String channelName;

    private Integer platform;

    private String cooperationForm;

    private Integer state;

    private Date startTime;

    private Date endTime;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getCooperationForm() {
        return cooperationForm;
    }

    public void setCooperationForm(String cooperationForm) {
        this.cooperationForm = cooperationForm == null ? null : cooperationForm.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}