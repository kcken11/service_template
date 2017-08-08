package com.melot.talkee.driver.domain;

import java.io.Serializable;

public class LessonLevel  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer levelId;

    private String levelName;

    private Integer parentLevel;

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName == null ? null : levelName.trim();
    }

    public Integer getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(Integer parentLevel) {
        this.parentLevel = parentLevel;
    }
}