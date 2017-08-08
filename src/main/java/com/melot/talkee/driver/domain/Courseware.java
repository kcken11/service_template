package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class Courseware  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer coswId;

    private Integer lessonId;

    private String coswUrl;

    private Integer admId;

    private Date dtime;

    private Integer grank;

    public Integer getCoswId() {
        return coswId;
    }

    public void setCoswId(Integer coswId) {
        this.coswId = coswId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getCoswUrl() {
        return coswUrl;
    }

    public void setCoswUrl(String coswUrl) {
        this.coswUrl = coswUrl == null ? null : coswUrl.trim();
    }

    public Integer getAdmId() {
        return admId;
    }

    public void setAdmId(Integer admId) {
        this.admId = admId;
    }

    public Date getDtime() {
        return dtime;
    }

    public void setDtime(Date dtime) {
        this.dtime = dtime;
    }

    public Integer getGrank() {
        return grank;
    }

    public void setGrank(Integer grank) {
        this.grank = grank;
    }
}