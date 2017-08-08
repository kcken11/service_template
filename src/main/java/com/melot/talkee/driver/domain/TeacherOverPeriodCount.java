package com.melot.talkee.driver.domain;

import java.io.Serializable;

/**
 * Created by mn on 2017/5/19.
 */
public class TeacherOverPeriodCount implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer needPeriods;

    private Integer realPeriods;

    private Integer vaildPeriods;
    
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

	/**
	 * @return the needPeriods
	 */
	public Integer getNeedPeriods() {
		return needPeriods;
	}

	/**
	 * @param needPeriods the needPeriods to set
	 */
	public void setNeedPeriods(Integer needPeriods) {
		this.needPeriods = needPeriods;
	}

	/**
	 * @return the realPeriods
	 */
	public Integer getRealPeriods() {
		return realPeriods;
	}

	/**
	 * @param realPeriods the realPeriods to set
	 */
	public void setRealPeriods(Integer realPeriods) {
		this.realPeriods = realPeriods;
	}

	/**
	 * @return the vaildPeriods
	 */
	public Integer getVaildPeriods() {
		return vaildPeriods;
	}

	/**
	 * @param vaildPeriods the vaildPeriods to set
	 */
	public void setVaildPeriods(Integer vaildPeriods) {
		this.vaildPeriods = vaildPeriods;
	}

    
}
