package com.melot.talkee.driver.service;

import com.melot.talkee.driver.domain.StudentLesson;

import java.util.Date;

/**
 * Created by mn on 2017/7/18.
 */
public interface TalkRecordService {

    public  String startRecord(String channel,Date endTime);

    public  String stopRecord(String channel,StudentLesson lesson);


    public String isCovertDone(int periodId);
}
