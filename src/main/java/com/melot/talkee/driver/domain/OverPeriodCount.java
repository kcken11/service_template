package com.melot.talkee.driver.domain;

import java.io.Serializable;

/**
 * Title: OverPeriodCount
 * Description: 老师结束课时汇总
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-04-26 15:25:53 
 */
public class OverPeriodCount  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long currentWeekCount;
	
	private Long lastWeekCount;
	
	private Long currentMonthCount;
	
	private Long lastMonthCount;
	
	private Long allCount;

	/**
	 * @return the currentWeekCount
	 */
	public Long getCurrentWeekCount() {
		return currentWeekCount;
	}

	/**
	 * @param currentWeekCount the currentWeekCount to set
	 */
	public void setCurrentWeekCount(Long currentWeekCount) {
		this.currentWeekCount = currentWeekCount;
	}

	/**
	 * @return the lastWeekCount
	 */
	public Long getLastWeekCount() {
		return lastWeekCount;
	}

	/**
	 * @param lastWeekCount the lastWeekCount to set
	 */
	public void setLastWeekCount(Long lastWeekCount) {
		this.lastWeekCount = lastWeekCount;
	}

	/**
	 * @return the currentMonthCount
	 */
	public Long getCurrentMonthCount() {
		return currentMonthCount;
	}

	/**
	 * @param currentMonthCount the currentMonthCount to set
	 */
	public void setCurrentMonthCount(Long currentMonthCount) {
		this.currentMonthCount = currentMonthCount;
	}

	/**
	 * @return the lastMonthCount
	 */
	public Long getLastMonthCount() {
		return lastMonthCount;
	}

	/**
	 * @param lastMonthCount the lastMonthCount to set
	 */
	public void setLastMonthCount(Long lastMonthCount) {
		this.lastMonthCount = lastMonthCount;
	}

	/**
	 * @return the allCount
	 */
	public Long getAllCount() {
		return allCount;
	}

	/**
	 * @param allCount the allCount to set
	 */
	public void setAllCount(Long allCount) {
		this.allCount = allCount;
	}
	
}
