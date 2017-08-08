package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class TeacherInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer teacherId;

    private String teacherName;

    private Integer currStat;


    private String teacherLevel;

    private String canteacherLevels;

    private Integer tag;

    private String email;

    private Date birthday;

    private Date addTime;

    private Integer gender;

    private Integer age;

    private Integer cityId;

    private Integer portraitId;

    private String introduce;

    private String portrait;

    private Date lastLoginTime;

    private String lastLoginIp;

    private String eaDescribe;

    private Integer eaId;

    /**
     * EA id
     *
     * @return
     */
    public Integer getEaId() {
        return eaId;
    }

    public void setEaId(Integer eaId) {
        this.eaId = eaId;
    }

    public String getEaDescribe() {
        return eaDescribe;
    }

    /**
     * 备注
     *
     * @param eaDescribe
     */
    public void setEaDescribe(String eaDescribe) {
        this.eaDescribe = eaDescribe;
    }

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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName == null ? null : teacherName.trim();
    }

    public Integer getCurrStat() {
        return currStat;
    }

    public void setCurrStat(Integer currStat) {
        this.currStat = currStat;
    }

    public String getTeacherLevel() {
        return teacherLevel;
    }

    public void setTeacherLevel(String teacherLevel) {
        this.teacherLevel = teacherLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(Integer portraitId) {
        this.portraitId = portraitId;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    /**
     * 可授课等级
     * @return
     */
    public String getCanteacherLevels() {
        return canteacherLevels;
    }

    public void setCanteacherLevels(String canteacherLevels) {
        this.canteacherLevels = canteacherLevels;
    }

    public Integer getTag () {
        return tag;
    }

    public void setTag (Integer tag) {
        this.tag = tag;
    }
}