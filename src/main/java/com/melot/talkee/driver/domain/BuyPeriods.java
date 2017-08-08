package com.melot.talkee.driver.domain;

import java.util.Date;

public class BuyPeriods {
    private Integer histId;

    private String orderid;

    private Integer userId;

    private Integer periods;

    private Integer publicLesson;

    private Integer extraPeriods;

    private Integer extraPublicLesson;

    private Integer status;

    private String ipaddr;

    private Integer paymentMode;

    private Integer amount;

    private Integer packageId;

    private Date buyTime;

    private Date affirmTime;

    private String callbakOrderid;

    /**
     * 扩展字段
     * **/
    /**
     *到期时间
     */
    private  Date validDate;

    private int freeTimes;


    /**
     * 免责次数
     * @return
     */
    public int getFreeTimes() {
        return freeTimes;
    }

    public void setFreeTimes(int freeTimes) {
        this.freeTimes = freeTimes;
    }

    public Integer getHistId() {
        return histId;
    }

    public void setHistId(Integer histId) {
        this.histId = histId;
    }

    /**
     * 订单编号
     * @return
     */
    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    /**
     * 学生id
     * @return
     */
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 订单购买普通课时数
     * @return
     */
    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    /**
     * 公开课时数
     * @return
     */
    public Integer getPublicLesson() {
        return publicLesson;
    }

    public void setPublicLesson(Integer publicLesson) {
        this.publicLesson = publicLesson;
    }

    public Integer getExtraPeriods() {
        return extraPeriods;
    }

    public void setExtraPeriods(Integer extraPeriods) {
        this.extraPeriods = extraPeriods;
    }

    public Integer getExtraPublicLesson() {
        return extraPublicLesson;
    }

    public void setExtraPublicLesson(Integer extraPublicLesson) {
        this.extraPublicLesson = extraPublicLesson;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr == null ? null : ipaddr.trim();
    }

    public Integer getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public Date getAffirmTime() {
        return affirmTime;
    }

    public void setAffirmTime(Date affirmTime) {
        this.affirmTime = affirmTime;
    }

    public String getCallbakOrderid() {
        return callbakOrderid;
    }

    public void setCallbakOrderid(String callbakOrderid) {
        this.callbakOrderid = callbakOrderid;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }
}