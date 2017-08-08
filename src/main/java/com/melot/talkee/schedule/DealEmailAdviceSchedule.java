package com.melot.talkee.schedule;

import com.melot.talkee.driver.domain.EmailAdviceTypeEnum;
import com.melot.talkee.driver.domain.Lesson;
import com.melot.talkee.driver.service.TalkEmailService;
import com.melot.talkee.redis.LessonRedisSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by mn on 2017/5/17.
 */
public class DealEmailAdviceSchedule {

    private static final Logger logger = Logger.getLogger(DealEmailAdviceSchedule.class);

    private static final String LOCK_KEY_PREFIX = "email_advice_lock_%s";

    @Autowired
    private TalkEmailService talkEmailService;

    /****
     * 处理邮件通知
     */
    public void dealAdvice() {
        long currenttime = System.currentTimeMillis();
        String lockKey=null;
        try {
        Set<String> members = LessonRedisSource.getEmailAdvice(currenttime - 5000, currenttime + 5000);
        for (String memeber : members) {
            //加锁成功
               lockKey = String.format(LOCK_KEY_PREFIX, memeber);

                if (LessonRedisSource.lockKey(lockKey) == 1) {
                    logger.info("deal.key.periodId:" + memeber);
                    String[] ks = memeber.split("-");
                    String[] params = ks[0].split(",");

                    if (params.length == 1) {
                        talkEmailService.sendTemplateMail(EmailAdviceTypeEnum.ORDER_SUCCESS_SINGLE, Integer.parseInt(params[0]), Integer.parseInt(ks[1]), 0);
                    } else {
                        talkEmailService.sendTemplateMail(EmailAdviceTypeEnum.ORDER_SUCCESS_BATCH, Integer.parseInt(params[1]), Integer.parseInt(ks[1]), Integer.parseInt(params[0]));
                    }
                    LessonRedisSource.removeEmailAdvice(memeber);
                }

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
