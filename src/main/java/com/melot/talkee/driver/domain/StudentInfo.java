package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class StudentInfo  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer userId;
    
    private String cnNickname;

    private String enNickname;

    private Integer gender;

    private Integer ccId;

    private Integer crId;

    private Integer userType;

    private Integer userLevel;
    
    private Integer subLevel;
  
    private Date birthday;

    private Date updateTime;
    
    private Integer tag;
    
    private String portrait;
    
    private Integer cityId;
    
    private String phoneNum;
    
    private String email;

    private Date lastLoginTime;

    private String lastLoginIp;

    private String adminDesc;
    
    private Integer classType;

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
	 * @return the subLevel
	 */
	public Integer getSubLevel() {
		return subLevel;
	}

	/**
	 * @param subLevel the subLevel to set
	 */
	public void setSubLevel(Integer subLevel) {
		this.subLevel = subLevel;
	}

	/**
	 * @return the phoneNum
	 */
	public String getPhoneNum() {
		return phoneNum;
	}

	/**
	 * @param phoneNum the phoneNum to set
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the cityId
	 */
	public Integer getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the portrait
	 */
	public String getPortrait() {
		return portrait;
	}

	/**
	 * @param portrait the portrait to set
	 */
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	/**
	 * @return the tag
	 */
	public Integer getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCnNickname() {
        return cnNickname;
    }

    public void setCnNickname(String cnNickname) {
        this.cnNickname = cnNickname == null ? null : cnNickname.trim();
    }

    public String getEnNickname() {
        return enNickname;
    }

    public void setEnNickname(String enNickname) {
        this.enNickname = enNickname == null ? null : enNickname.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getCcId() {
        return ccId;
    }

    public void setCcId(Integer ccId) {
        this.ccId = ccId;
    }

    public Integer getCrId() {
        return crId;
    }

    public void setCrId(Integer crId) {
        this.crId = crId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 备注
     * @return
     */
    public String getAdminDesc() {
        return adminDesc;
    }

    public void setAdminDesc(String adminDesc) {
        this.adminDesc = adminDesc;
    }

    public Integer getClassType() {
        return classType;
    }

    public void setClassType(Integer classType) {
        this.classType = classType;
    }
}