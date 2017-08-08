package com.melot.talkee.driver.domain;

import java.io.Serializable;

/**
 * Title:
 * <p>
 * Description:
 * </p>
 *
 * @author 范玉全<a href="mailto:yuquan.fan@melot.cn">
 * @version V1.0
 * @since 10:09 2017/7/25
 */
public class Requirement implements Serializable {
    private Integer requireId;
    /**
     * 中文要求
     */
    private String requirement;
    /**
     * 英文要求
     */
    private String enRequirement;
    /**
     * 是否互斥 0：不互斥 1：互斥
     */
    private Integer type;

    public Integer getRequireId() {
        return requireId;
    }

    public void setRequireId(Integer requireId) {
        this.requireId = requireId;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getEnRequirement() {
        return enRequirement;
    }

    public void setEnRequirement(String enRequirement) {
        this.enRequirement = enRequirement;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
