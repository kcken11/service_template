package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class TeacherLesson  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer teacherId;

    private Integer lessonLevel;

    private Date confTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getLessonLevel() {
        return lessonLevel;
    }

    public void setLessonLevel(Integer lessonLevel) {
        this.lessonLevel = lessonLevel;
    }

    public Date getConfTime() {
        return confTime;
    }

    public void setConfTime(Date confTime) {
        this.confTime = confTime;
    }
}