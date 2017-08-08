package com.melot.talkee.driver.domain;

import java.io.Serializable;

/**
 * Created by mn on 2017/7/25.
 */
public class VideoRecording implements Serializable {
    /*用户id*/
    private Integer userId;
    /**
     * 开始时间
     **/
    private Integer startTime;
    /**
     * 时长
     */
    private Integer duration;
    /**
     * 视频地址
     **/
    private String videoUrl;
    /**
     * 用户类型
     **/
    private Integer accountType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
