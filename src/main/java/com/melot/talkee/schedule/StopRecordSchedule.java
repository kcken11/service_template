package com.melot.talkee.schedule;

import com.melot.talkee.dao.StudentLessonMapper;
import com.melot.talkee.driver.domain.StudentLesson;
import com.melot.talkee.driver.service.TalkRecordService;
import com.melot.talkee.redis.LessonRedisSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by mn on 2017/5/17.
 */
public class StopRecordSchedule {

    private static final Logger logger = Logger.getLogger(StopRecordSchedule.class);

    private static final String LOCK_KEY_PREFIX = "stop_record_lock_%s";

    @Autowired
    private TalkRecordService talkRecordService;
    @Autowired
    private StudentLessonMapper studentLessonMapper;

    /****
     * 处理邮件通知
     */
    public void stopRecords() {
        logger.info("start stop record ");
        long currenttime = System.currentTimeMillis();
        String lockKey=null;
        try {
        Set<String> channels = LessonRedisSource.getStopRecordList(currenttime - 5000, currenttime + 5000);

        for (String channel : channels) {
            //加锁成功
            logger.info("start stop record  channel---->"+channel);
               lockKey = String.format(LOCK_KEY_PREFIX, channel);

                if (LessonRedisSource.lockKey(lockKey) == 1) {
                    logger.info("deal.key.channel:" + channel);
                    StudentLesson lesson=studentLessonMapper.getOrderLessonByPeriodId(Integer.valueOf(channel));

                    if(lesson!=null){
                       talkRecordService.stopRecord(channel,lesson);
                    }
                }
            logger.info("start stop record  channel---->"+channel);
        }
        } catch (Exception e) {
            logger.error("deal.key.periodId error " + e.getLocalizedMessage());
        } finally {
            if(StringUtils.isNotBlank(lockKey)){
            LessonRedisSource.unlockKey(lockKey);
            }
        }

    }

}
