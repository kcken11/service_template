package com.melot.talkee.driver.domain;

import java.util.Date;

public class TeacherPerformance {
    private Integer histId;

    private Integer needPeriods;

    private Integer realPeriods;

    private Integer vaildPeriods;

    private Integer lessonType;

    private Integer teacherId;

    private Date dtime;

    private Integer periodId;
    
    private Integer extraPeriods;

    /**
	 * @return the extraPeriods
	 */
	public Integer getExtraPeriods() {
		return extraPeriods;
	}

	/**
	 * @param extraPeriods the extraPeriods to set
	 */
	public void setExtraPeriods(Integer extraPeriods) {
		this.extraPeriods = extraPeriods;
	}

	public Integer getHistId() {
        return histId;
    }

    public void setHistId(Integer histId) {
        this.histId = histId;
    }

    public Integer getNeedPeriods() {
        return needPeriods;
    }

    public void setNeedPeriods(Integer needPeriods) {
        this.needPeriods = needPeriods;
    }

    public Integer getRealPeriods() {
        return realPeriods;
    }

    public void setRealPeriods(Integer realPeriods) {
        this.realPeriods = realPeriods;
    }

    public Integer getVaildPeriods() {
        return vaildPeriods;
    }

    public void setVaildPeriods(Integer vaildPeriods) {
        this.vaildPeriods = vaildPeriods;
    }

    public Integer getLessonType() {
        return lessonType;
    }

    public void setLessonType(Integer lessonType) {
        this.lessonType = lessonType;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Date getDtime() {
        return dtime;
    }

    public void setDtime(Date dtime) {
        this.dtime = dtime;
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }
}