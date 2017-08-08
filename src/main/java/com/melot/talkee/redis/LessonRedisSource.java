package com.melot.talkee.redis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.melot.common.melot_jedis.JedisWrapper;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.Lesson;
import com.melot.talkee.driver.domain.LessonLevel;
import com.melot.talkee.driver.domain.PublishLesson;

public class LessonRedisSource {
	
	private static final String SOURCE_BEAN_NAME = "lessonRedisSource";

    private static final String LESSON_ORDER_ADVICE ="email_advice_%s" ;

	private static final String In_CLASS_ADVICE="in_class_period_%s";


	private static final String RECORD_CHANNEL_LIST="record_channel_list";

	private static final String STOP_RECORD_CHANNEL_LIST="stop_recorder_channel_list";


    private static Logger logger = Logger.getLogger(LessonRedisSource.class);
	
	/**
	 * 过期时间
	 */
	private static final int EXPIRE_TIME = 5*60;
	
	/**
	 * 获取redis资源
	 * @return
	 */
	private static JedisWrapper getInstance() {
    	return MelotBeanFactory.getBean(SOURCE_BEAN_NAME, JedisWrapper.class);
	}
	
	/**
	 * 老师发布信息缓存(talk_publish_lesson_periodId)
	 */
	private static final String PUBLISH_LESSON_KEY = "talk_publish_lesson_%s";
	
	/**
	 * 每天待上课老师
	 */
	private static final String DAILY_TEACHER_KEY = "talk_daily_teacher_%s";
	
	/**
	 * 老师每天待上课程
	 */
	private static final String TEACHER_DAILY_LESSON_KEY = "talk_teacher_daily_lesson_%s";
	
	/**
	 * 老师正在进行课程状态
	 */
	private static final String TEACHER_CHECK_LESSON_KEY = "talk_teacher_check_lesson_%s_%s";

	/**
	 * 课程配置
	 */
	private static final String CONF_LESSON_KEY = "talk_conf_lesson_%s";

	/**
	 * 课程等级配置
	 */
	private static final String LESSON_LEVEL_KEY = "talk_lesson_level_%s";

    /**
     * 当前课时课程
     */
	private static final String CURRENT_PERIOD_KEY = "talk_current_%s";
    /**
     * 当前课时 课程版本、课程视频段
     */
	private static final String CURRENT_PERIOD_LESSON_KEY = "talk_current_%s_%s";
	
	private static final String CURRENT_PERIOD_LESSON_VERSION_KEY = "lessonVersion";

    private static final String CURRENT_PERIOD_LESSON_SEGMENT_KEY = "segment";
	
    /**
     * 学生预约标记
     */
    private static final String TALK_ORDER_TAG_KEY = "talk_order_%s_%s";

    /**
     * 当前课时对应课程信息
     * @param lesson
     * @return
     */
    public static void addCurrentPeriodCache(Integer periodId,Integer lessonId,String lessonVersion){
        JedisWrapper jedis = null;
        if (periodId != null && lessonId != null && StringUtils.isNotBlank(lessonVersion)) {
            try {
                jedis = getInstance();
                String key = String.format(CURRENT_PERIOD_KEY,periodId);
                jedis.STRINGS.setEx(key, 3*60*60, String.valueOf(lessonId));
                key = String.format(CURRENT_PERIOD_LESSON_KEY,periodId,lessonId);
                
//                
//                Map<String, String> map = new HashMap<String, String>();
//                map.put(CURRENT_PERIOD_LESSON_VERSION_KEY, lessonVersion);
//                map.put(CURRENT_PERIOD_LESSON_SEGMENT_KEY, 1);
//                jedis.HASH.hset(key, CURRENT_PERIOD_LESSON_VERSION_KEY, lessonVersion);
            } catch (Exception e) {
                logger.error("LessonRedisSource.addCurrentPeriodLessonCache (periodId:" + periodId + ", lessonId:" + lessonId + ",lessonVersion:"+lessonVersion+") execute exception.", e);
            }
        }
    }
	

    /**
     * 当前课时对应课程信息
     * @param lesson
     * @return
     */
//    public static void addCurrentPeriodCache(Integer periodId,String fieid,String fieidValue){
//        JedisWrapper jedis = null;
//        if (periodId != null && StringUtils.isNotBlank(fieid) && StringUtils.isNotBlank(fieidValue)) {
//            try {
//                jedis = getInstance();
//                String key = String.format(CURRENT_PERIOD_KEY,periodId);
//                jedis.STRINGS.setEx(key, 3*60*60, String.valueOf(lessonId));
//                key = String.format(CURRENT_PERIOD_LESSON_KEY,periodId,lessonId);
//                
//                
//                Map<String, String> map = new HashMap<String, String>();
//                map.put(CURRENT_PERIOD_LESSON_VERSION_KEY, lessonVersion);
//                map.put(CURRENT_PERIOD_LESSON_SEGMENT_KEY, 1);
//                jedis.HASH.hset(key, CURRENT_PERIOD_LESSON_VERSION_KEY, lessonVersion);
//            } catch (Exception e) {
//                logger.error("LessonRedisSource.addCurrentPeriodLessonCache (periodId:" + periodId + ", lessonId:" + lessonId + ",lessonVersion:"+lessonVersion+") execute exception.", e);
//            }
//        }
//    }
    
    /**
     * 当前课时对应课程信息
     * @param lesson
     * @return
     */
    public static Map<String, String> getCurrentPeriodCache(Integer periodId){
        JedisWrapper jedis = null;
        if (periodId != null) {
            try {
                jedis = getInstance();
                String key = String.format(CURRENT_PERIOD_KEY,periodId);
                String  lessonId = jedis.STRINGS.get(key);
                if (StringUtils.isNotBlank(lessonId)) {
                    key = String.format(CURRENT_PERIOD_LESSON_KEY,periodId,lessonId);
                    return jedis.HASH.hgetAll(key);
                }
            } catch (Exception e) {
                logger.error("LessonRedisSource.getCurrentPeriodCache (periodId:" + periodId + ") execute exception.", e);
            }
        }
        return null;
    }
    
	
	/**
	 * 课程配置
	 * @param lesson
	 * @return
	 */
    public static String addConfLessonCache(Lesson lesson){
    	JedisWrapper jedis = null;
		if (lesson != null && lesson.getLessonId() != null) {
			try {
				jedis = getInstance();
				String key = String.format(CONF_LESSON_KEY,lesson.getLessonId());
	        	String value = new Gson().toJson(lesson,Lesson.class);
				jedis.STRINGS.setEx(key, EXPIRE_TIME,value);
				return value;
			} catch (Exception e) {
				logger.error("LessonRedisSource.addConfLessonCache (" + lesson.getLessonId() + ", " + new Gson().toJson(lesson) + ") execute exception.", e);
			}
		}
		return null;
    }

	/**
	 * 课程配置
	 * @param lessonId
	 * @return Lesson
	 */
    public static Lesson getConfLessonCache(Integer lessonId){
    	JedisWrapper jedis = null;
		if (lessonId != null) {
			try {
				jedis = getInstance();
				String key = String.format(CONF_LESSON_KEY,lessonId);
				String lessonInfo = jedis.STRINGS.get(key);
				if (StringUtils.isNotBlank(lessonInfo)) {
					return new Gson().fromJson(lessonInfo, Lesson.class);
				}
				return null;
			} catch (Exception e) {
				logger.error("LessonRedisSource.getConfLessonCache (" + lessonId +") execute exception.", e);
			}
		}
		return null;
    }


	/**
	 * 课程等级
	 * @param lessonLevel
	 * @return
	 */
    public static String addLessonLevelCache(LessonLevel lessonLevel){
    	JedisWrapper jedis = null;
		if (lessonLevel != null && lessonLevel.getLevelId() != null) {
			try {
				jedis = getInstance();
				String key = String.format(LESSON_LEVEL_KEY,lessonLevel.getParentLevel());
				Integer fieid = lessonLevel.getLevelId();
	        	String value = new Gson().toJson(lessonLevel,LessonLevel.class);
				jedis.HASH.hset(key, String.valueOf(fieid), value);
				jedis.KEYS.expire(key, EXPIRE_TIME);
				return value;
			} catch (Exception e) {
				logger.error("LessonRedisSource.addLessonLevelCache (" + lessonLevel.getParentLevel() + ", " + lessonLevel.getLevelId() + "," + new Gson().toJson(lessonLevel) + ") execute exception.", e);
			}
		}
		return null;
    }

	/**
	 * 课程等级
	 * @param parentLevel
	 * @param levelId
	 * @return Lesson
	 */
    public static LessonLevel getLessonLevelCache(Integer parentLevel,Integer levelId){
    	JedisWrapper jedis = null;
		if (parentLevel != null && levelId != null) {
			try {
				jedis = getInstance();
				String key = String.format(LESSON_LEVEL_KEY,parentLevel);
				String levelInfo = jedis.HASH.hget(key, String.valueOf(levelId));
				if (StringUtils.isNotBlank(levelInfo)) {
					return new Gson().fromJson(levelInfo, LessonLevel.class);
				}
				return null;
			} catch (Exception e) {
				logger.error("LessonRedisSource.getLessonLevelCache (" + parentLevel +"," + levelId +") execute exception.", e);
			}
		}
		return null;
    }



	/**
	 * 老师发布课程缓存
	 * @param periodId
	 * @param publishLesson
	 * @return
	 */
    public static String addPublishLessonCache(int periodId,PublishLesson publishLesson){
    	JedisWrapper jedis = null;
		if (publishLesson == null) {
			return null;
		}
		try {
			jedis = getInstance();
			String key = String.format(PUBLISH_LESSON_KEY,periodId);
        	String value = new Gson().toJson(publishLesson,PublishLesson.class);
			jedis.STRINGS.setEx(key, EXPIRE_TIME,value);
			return value;
		} catch (Exception e) {
			logger.error("LessonRedisSource.addPublishLessonCache (" + periodId + ", " + new Gson().toJson(publishLesson) + ") execute exception.", e);
		}
		return null;
    }
	
	
	
	/**
	 * 获取老师发布课程信息
	 * @param periodId
	 * @return
	 */
	public static PublishLesson getPublishLessonCache(int periodId) {
		JedisWrapper jedis = null;
		PublishLesson publishLesson = null;
		try {
			jedis = getInstance();
			String key = String.format(PUBLISH_LESSON_KEY,periodId);
			String value = jedis.STRINGS.get(key);
			if (StringUtils.isNotBlank(value)) {
				publishLesson = new Gson().fromJson(value, PublishLesson.class);
			}
			return publishLesson;
		} catch (Exception e) {
			logger.error("LessonRedisSource.getPublishLessonCache (" + periodId + ")execute exception.", e);
		}
		return null;
	}
	
    
	/**
	 * 添加每天待上课老师
	 * @param teacherId
	 * @return
	 * @throws Exception
	 */
    public static Long addDailyTeacherSet(int teacherId) throws Exception{
    	String key = String.format(DAILY_TEACHER_KEY,new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()));
    	long size = getInstance().SETS.sadd(key,String.valueOf(teacherId));
    	if (size == 1) {
    		 getInstance().KEYS.expireAt(key, getSeconds(3));
		}
    	return size;
    }
    
	 /**
	  * 获取每天待上课老师
	  * @return
	  * @throws Exception
	  */
    public static Set<String> getDailyTeacherSet() throws Exception{
    	return  getInstance().SETS.smembers(String.format(DAILY_TEACHER_KEY,new java.text.SimpleDateFormat("yyyyMMdd").format(new Date())));
    }
	
	/**
	 * 添加老师每天待上课程
	 * @param teacherId
	 * @param periodId
	 * @param beginTime
	 * @return
	 * @throws Exception
	 */
    public static Long addTeacherDailyLessonSet(int teacherId,int periodId,Date beginTime) throws Exception{
    	String key = String.format(TEACHER_DAILY_LESSON_KEY,teacherId);
    	long size =  getInstance().SORTSETS.zadd(key,beginTime.getTime(),String.valueOf(periodId));
    	if (size == 1) {
    		getInstance().KEYS.expireAt(key, getSeconds(3));
		}
    	return size;
    }
    
	 /**
	  * 删除老师每天待上课程
	  * @param teacherId
	  * @param periodId
	  * @return
	  * @throws Exception
	  */
    public static Long removeTeacherDailyLessonSet(String teacherId,String periodId) throws Exception{
    	String key = String.format(TEACHER_DAILY_LESSON_KEY,teacherId);
    	return getInstance().SORTSETS.zrem(key,periodId);
    }
    
	 /**
	  * 获取老师每天待上课程
	  * @param teacherId
	  * @return
	  * @throws Exception
	  */
    public static Set<String> getTeacherDailyLessonSet(String teacherId) throws Exception{
    	String key = String.format(TEACHER_DAILY_LESSON_KEY,teacherId);
    	return getInstance().SORTSETS.zrange(key, 0, 0);
    }
    
	
	/**
	 * 获取检测老师课程状态
	 * @param teacherId
	 * @param periodId
	 * @return 1 未检测，大于1 已检测或检测中
	 * @throws Exception
	 */
    public static long getCheckLessonStatus(String teacherId,String periodId) throws Exception{
    	String key = String.format(TEACHER_CHECK_LESSON_KEY,teacherId,periodId);
    	long size = getInstance().STRINGS.incrBy(key, 1);
    	if (size == 1) {
    		getInstance().KEYS.expire(key, 300);
		}
    	return size;
    }
	
    
    /**
     * 获取学生预约标记
     * @param studentId
     * @param periodId
     * @return 1 未检测，大于1 已检测或检测中
     * @throws Exception
     */
    public static long getStudentOrderTag(Integer studentId,Integer periodId){
        long size = 0L;
        try {
            String key = String.format(TALK_ORDER_TAG_KEY,studentId,periodId);
            size = getInstance().STRINGS.incrBy(key, 1);
            if (size == 1L) {
                getInstance().KEYS.expire(key, 300);
            }
        } catch (Exception e) {
            size = 1L;
        }
        return size;
    }
    
	
    /**
     * 获取多少天后零点时间
     *
     * @return
     */
    public static long getSeconds(long day) {
        day = day * 24 * 60 * 60;
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(date);
            long today = dateFormat.parse(dateStr).getTime() / 1000;
            return today + day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 新增预约通知
     *
     * @param period 课程id
     * @param score  通知的时间
     * @return 影响的条数
     */
    public static long addEmailAdvice(String period, double score) {
        JedisWrapper jedis = null;
        if (period ==null) {
            return 0;
        }
        try {
            jedis = getInstance();
            String key = LESSON_ORDER_ADVICE;
            return jedis.SORTSETS.zadd(key, score, String.valueOf(period));
        } catch (Exception e) {
            logger.error("UserRedisSource.addEmailAdvice (" + period + ") execute exception.", e);
        }
        return 0;
    }

    /**
     *
     * @param min 最小时间戳
     * @param max 最大时间戳
     * @return
     */
    public static Set<String> getEmailAdvice(double min, double max) {
        JedisWrapper jedis = null;
        try {
            jedis = getInstance();
            String key = LESSON_ORDER_ADVICE;
            return jedis.SORTSETS.zrangeByScore(key, min, max);
        } catch (Exception e) {
            logger.error("UserRedisSource.getEmailAdvice  between (" + min + "," + max + ") execute exception.", e);
        }
        return null;
    }

    /**
     * 判断邮件发送通知 是否存在
     * @param period
     * @return
     */
    public static boolean existEmailAdvice(int period){
        JedisWrapper jedis = null;
        try {
            jedis = getInstance();
            String key = LESSON_ORDER_ADVICE;
            Double score=jedis.SORTSETS.zscore(key, String.valueOf(period));
            return   score==null?false:true;
        } catch (Exception e) {
            logger.error("UserRedisSource.existEmailAdvice   (" +period+ ") execute exception.", e);
        }

        return false;
    }

    /**
     * 判断邮件发送通知 是否存在
     * @param period
     * @return
     */
    public static long removeEmailAdvice(String period){
        JedisWrapper jedis = null;
        try {
            jedis = getInstance();
            String key = LESSON_ORDER_ADVICE;
            long l=jedis.SORTSETS.zrem(key, String.valueOf(period));
            return   l;
        } catch (Exception e) {
            logger.error("UserRedisSource.existEmailAdvice   (" +period+ ") execute exception.", e);
        }
        return 0;
    }


    /**
     * 预防集群同时处理,处理前加锁
     * @param key
     * @return
     */
    public static long lockKey(String key) {
        long l = 0;
        JedisWrapper jedis = null;
        try {
          jedis=getInstance();

          l=jedis.STRINGS.setnx(key,"1");

        } catch (Exception e) {
            logger.error("UserRedisSource.lockKey  (" + key + ") execute exception.", e);
        }

        return l;
    }
    /**
     * 预防集群同时处理,处理后删除锁
     * @param key
     * @return
     */
    public static long unlockKey(String key) {
        long l = 0;
        JedisWrapper jedis = null;
        try {
            jedis=getInstance();

            l=jedis.KEYS.del(key);

        } catch (Exception e) {
            logger.error("UserRedisSource.unlockKey  (" + key + ") execute exception.", e);

        }

        return l;
    }


	public static boolean existKey(String key) {
		boolean result=false;
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();
			result=jedis.KEYS.exists(key);

		} catch (Exception e) {
			logger.error("UserRedisSource.unlockKey  (" + key + ") execute exception.", e);

		}

		return result;
	}

	public static String setKeyExpire(String key,int seconds,String value) {
		String  result="";
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();
		//	result=jedis.KEYS.expire(key,60 * 60 );
         result= jedis.STRINGS.setEx(key,seconds,value);
		} catch (Exception e) {
			logger.error("UserRedisSource.unlockKey  (" + key + ") execute exception.", e);

		}

		return result;
	}

	public static boolean existInClassKey(Integer periodId) {
       return 	existKey(String.format(In_CLASS_ADVICE,periodId));
	}

	public static String expireInClassKey(Integer periodId,int seconds){
    	return  setKeyExpire(String.format(In_CLASS_ADVICE,periodId),seconds,"1");
	}

	/**
	 * 判断是否已在录制中
	 * @param channel
	 * @return
	 */
	public static boolean isRecording(String channel){
		String  result="";
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();

			result= jedis.HASH.hget(RECORD_CHANNEL_LIST,channel);

		} catch (Exception e) {
			logger.error("UserRedisSource.isRecording  (" + channel + ") execute exception.", e);
		}

		return StringUtils.isNotBlank(result);
	}

	public static long putIntoRecordList(String channel,String date){
		long  result=0;
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();

			result= jedis.HASH.hset(RECORD_CHANNEL_LIST,channel,date);

		} catch (Exception e) {
			logger.error("UserRedisSource.isRecording  (" + channel + ") execute exception.", e);
		}

		return result;
	}

	/**
	 * 获取录制日期
	 * @param channel
	 * @return
	 */
	public static String getRecordDate(String channel){
		String  result="";
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();

			result= jedis.HASH.hget(RECORD_CHANNEL_LIST,channel);

		} catch (Exception e) {
			logger.error("UserRedisSource.isRecording  (" + channel + ") execute exception.", e);
		}

		return result;
	}

	public static long removeFromRecordList(String channel){
		long  result=0;
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();

			result= jedis.HASH.hdel(RECORD_CHANNEL_LIST,channel);

		} catch (Exception e) {
			logger.error("UserRedisSource.isRecording  (" + channel + ") execute exception.", e);
		}

		return result;
	}

	public static long putIntoStopRecordList(String channel,long timestamp){
		long  result=0;
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();
             Double score= Double.valueOf(timestamp);
			result= jedis.SORTSETS.zadd(STOP_RECORD_CHANNEL_LIST,score,channel);

		} catch (Exception e) {
			logger.error("UserRedisSource.isRecording  (" + channel + ") execute exception.", e);
		}

		return result;
	}

	public static Set<String> getStopRecordList(long start,long end){
		Set<String>  result=null;
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();

			result= jedis.SORTSETS.zrangeByScore(STOP_RECORD_CHANNEL_LIST,Double.valueOf(start),Double.valueOf(end));

		} catch (Exception e) {
			logger.error("UserRedisSource.putIntoStopRecordList  (" + start + ") execute exception.", e);
		}

		return result;
	}
	public static long removeFromStopChannelList(String channel){
		long result=0L;
		JedisWrapper jedis = null;
		try {
			jedis=getInstance();

			result= jedis.SORTSETS.zrem(STOP_RECORD_CHANNEL_LIST,channel);

		} catch (Exception e) {
			logger.error("UserRedisSource.putIntoStopRecordList  (" + channel + ") execute exception.", e);
		}

		return result;
	}
}
