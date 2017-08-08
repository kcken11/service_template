package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class StudentPeriods  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer resId;

    private Integer userId;

    private String lastOrderId;

    private Date beginTime;

    private Date endTime;

    private Integer curPeriods;

    private Integer allPeriods;

    private Integer overPeriods;

    private Date dtime;
    
    private Integer periodsType;
    
    private Integer vaildPreviwPeriods;

    /**
     * 累计课时时长（累计说英语）
     */
    private Integer accumulativeTotal;
    
    /**
	 * @return the vaildPreviwPeriods
	 */
	public Integer getVaildPreviwPeriods() {
		return vaildPreviwPeriods;
	}

	/**
	 * @param vaildPreviwPeriods the vaildPreviwPeriods to set
	 */
	public void setVaildPreviwPeriods(Integer vaildPreviwPeriods) {
		this.vaildPreviwPeriods = vaildPreviwPeriods;
	}

	/**
	 * @return the periodsType
	 */
	public Integer getPeriodsType() {
		return periodsType;
	}

	/**
	 * @param periodsType the periodsType to set
	 */
	public void setPeriodsType(Integer periodsType) {
		this.periodsType = periodsType;
	}

	public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLastOrderId() {
        return lastOrderId;
    }

    public void setLastOrderId(String lastOrderId) {
        this.lastOrderId = lastOrderId == null ? null : lastOrderId.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getCurPeriods() {
        return curPeriods;
    }

    public void setCurPeriods(Integer curPeriods) {
        this.curPeriods = curPeriods;
    }

    public Integer getAllPeriods() {
        return allPeriods;
    }

    public void setAllPeriods(Integer allPeriods) {
        this.allPeriods = allPeriods;
    }

    public Integer getOverPeriods() {
        return overPeriods;
    }

    public void setOverPeriods(Integer overPeriods) {
        this.overPeriods = overPeriods;
    }

    public Date getDtime() {
        return dtime;
    }

    public void setDtime(Date dtime) {
        this.dtime = dtime;
    }

    public Integer getAccumulativeTotal() {
        return accumulativeTotal;
    }

    public void setAccumulativeTotal(Integer accumulativeTotal) {
        this.accumulativeTotal = accumulativeTotal;
    }
}