package com.melot.talkee.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.melot.talkee.dao.StudentLessonMapper;
import com.melot.talkee.driver.domain.StudentLesson;
import com.melot.talkee.driver.domain.TalkTagCodeEnum;
import com.melot.talkee.driver.service.TalkRecordService;
import com.melot.talkee.driver.utils.HttpClient;
import com.melot.talkee.redis.LessonRedisSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mn on 2017/7/18.
 */
public class TalkRecordServiceImpl implements TalkRecordService {

    private static Logger logger = Logger.getLogger(TalkRecordServiceImpl.class);

    @Autowired
    private String agoraServer;

    @Autowired
    private StudentLessonMapper studentLessonMapper;


    @Override
    public String startRecord(String channel, Date endTime) {
        //如果录制中 直接返回
        if (LessonRedisSource.isRecording(channel)) {
            return null;
        }
        String result = "";
        String url = agoraServer + "?msgTag=1&userId=0&channel=" + channel;
        long endtime = endTime.getTime() + 30 * 60 * 1000;
        try {
            result = HttpClient.doGet(url, null);
            JSONObject resultJson = JSONObject.parseObject(result);
            if (resultJson != null && resultJson.containsKey("success") && resultJson.getBoolean("success")) {
                String recordDate = resultJson.getString("date");
                LessonRedisSource.putIntoRecordList(channel, recordDate);
                LessonRedisSource.putIntoStopRecordList(channel, endtime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String stopRecord(String channel, StudentLesson lesson) {
        String result = "";
        String date = LessonRedisSource.getRecordDate(channel);
        String url = agoraServer + "?msgTag=2&date=" + date + "&channel=" + channel;
        try {
            result = HttpClient.doGet(url, null);
            JSONObject resultJson = JSONObject.parseObject(result);
            if (resultJson != null && resultJson.containsKey("success") && resultJson.getBoolean("success")) {
                String storeUrl = resultJson.getString("storeUrl");
                lesson.setTeacherUrl(storeUrl + "/" + lesson.getTeacherId() + ".mp4");
                lesson.setStudentUrl(storeUrl + "/" + lesson.getStudentId() + ".mp4");

                JSONArray details = resultJson.getJSONArray("recordInfo");
                for (Object detail : details) {
                    detail = (JSONObject) detail;
                    int userId = ((JSONObject) detail).getInteger("userId");
                    if (userId == lesson.getStudentId()) {
                        lesson.setStudentStartTime(((JSONObject) detail).getString("start_date"));
                        lesson.setStudentDuration((int) (((JSONObject) detail).getFloat("end_time") * 1000));
                    }
                    if (userId == lesson.getTeacherId()) {
                        lesson.setTeacherStartTime(((JSONObject) detail).getString("start_date"));
                        lesson.setTeacherDuration((int) (((JSONObject) detail).getFloat("end_time") * 1000));
                    }
                }

                studentLessonMapper.updateByPrimaryKeySelective(lesson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LessonRedisSource.removeFromRecordList(channel);
        LessonRedisSource.removeFromStopChannelList(channel);
        return result;
    }

    @Override
    public String isCovertDone(int periodId) {
        HashMap<String, Object> lesson = studentLessonMapper.selectLessonRecordByPeriodId(periodId);
        if (lesson != null) {
            if (lesson.get("teacherUrl") == null || lesson.get("studentUrl") == null) {
                return TalkTagCodeEnum.VIDEO_RECORDING;
            }
            String savePath = (String) lesson.get("teacherUrl");
            String[] items = savePath.split("/");
            savePath = "/" + items[items.length - 3] + "/" + items[items.length - 2];
            String url = agoraServer + "?msgTag=3&path=" + savePath;
            try {
                String result = HttpClient.doGet(url, null);
                JSONObject resultJson = JSONObject.parseObject(result);
                if (resultJson != null && resultJson.containsKey("success") && resultJson.getBoolean("success")) {
                    if (resultJson.getBoolean("exist")) {
                        return TalkTagCodeEnum.SUCCESS;
                    } else {
                        return TalkTagCodeEnum.VIDEO_CONVERTING;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
               logger.error(" request agora-server exception: "+e.getLocalizedMessage());
            }
        }
        return TalkTagCodeEnum.LESSON_NOT_EXIST;
    }


    public static void main(String[] args) {
        String savePath = "http://yanxxcloud.cn/20170724/5775_083148/409.mp4";
        String[] items = savePath.split("/");
        savePath = items[items.length - 3] + "/" + items[items.length - 2];
        System.out.println(savePath);
    }
}
