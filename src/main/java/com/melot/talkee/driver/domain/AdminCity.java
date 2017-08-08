package com.melot.talkee.driver.domain;

import java.io.Serializable;

public class AdminCity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer parentId;

    private String name;

    private String enName;

    private String code;
    
    private Integer cityType;
    
    /**
     * @return the cityType
     */
    public Integer getCityType() {
        return cityType;
    }

    
    /**
     * @param cityType the cityType to set
     */
    public void setCityType(Integer cityType) {
        this.cityType = cityType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
}