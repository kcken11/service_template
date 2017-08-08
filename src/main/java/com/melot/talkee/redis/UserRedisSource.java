package com.melot.talkee.redis;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.melot.common.melot_jedis.JedisWrapper;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.StudentInfo;
import com.melot.talkee.driver.domain.TeacherInfo;
import com.melot.talkee.driver.domain.UserInfo;

public class UserRedisSource {
	
	private static final String SOURCE_BEAN_NAME = "userRedisSource";
	
	private static Logger logger = Logger.getLogger(UserRedisSource.class);
	
	
	/**
	 * 获取redis资源
	 * @return
	 */
	private static JedisWrapper getInstance() {
    	return MelotBeanFactory.getBean(SOURCE_BEAN_NAME, JedisWrapper.class);
	}
	
	/**
	 * 用户token(utoken_userId)
	 */
	private static final String USER_TOKEN_KEY = "talk_utoken_%s_%s";
	
	
	/**
	 * 用户注册信息
	 */
	private static final String USER_INFO_KEY = "talk_userInfo_%s";
	
	/**
	 * 学生信息
	 */
	private static final String STUDENT_INFO_KEY = "talk_studentInfo_%s";
	
	
	/**
	 * 老师信息
	 */
	private static final String TEACHER_INFO_KEY = "talk_teacherInfo_%s";
	
	
	/**
	 * 用户头像
	 */
	private static final String USER_PORTRAIT_KEY = "talk_user_portrait_%s";
	
	/**
	 * 过期时间
	 */
	private static final int EXPIRE_TIME = 5*60;
	
	/**
	 * 存储注册信息
	 * @param key
	 * @param userInfo
	 * @return UserInfo JSON格式字符串
	 */
	public static String setUserInfoCache(int userId, UserInfo userInfo) {
		JedisWrapper jedis = null;
		if (userInfo == null) {
			return null;
		}
		try {
			jedis = getInstance();
			String key = String.format(USER_INFO_KEY, userId);
			String value = new Gson().toJson(userInfo,UserInfo.class);
			jedis.STRINGS.setEx(key, EXPIRE_TIME,value);
			return value;
		} catch (Exception e) {
			logger.error("UserRedisSource.setUserInfoCache (" + userId + ", " + new Gson().toJson(userInfo) + ") execute exception.", e);
		}
		return null;
	}
	
	/**
	 * 存储用户头像
	 * @param userId
	 * @param portrait
	 * @return portrait
	 */
	public static String setUserPortraitCache(int userId, String portrait) {
		JedisWrapper jedis = null;
		try {
			jedis = getInstance();
			String key = String.format(USER_PORTRAIT_KEY, userId);
			jedis.STRINGS.setEx(key, EXPIRE_TIME,portrait);
			return portrait;
		} catch (Exception e) {
			logger.error("UserRedisSource.setUserPortraitCache (" + userId + ", " + portrait + ") execute exception.", e);
		}
		return null;
	}
	
	/**
	 * 删除存储用户头像
	 * @param userId
	 */
	public static void delUserPortraitCache(int userId) {
		JedisWrapper jedis = null;
		try {
			jedis = getInstance();
			String key = String.format(USER_PORTRAIT_KEY, userId);
			jedis.KEYS.del(key);
		} catch (Exception e) {
			logger.error("UserRedisSource.delUserPortraitCache (" + userId + ") execute exception.", e);
		}
	}
	
	/**
	 * 存储学生信息
	 * @param studentId
	 * @param StudentInfo
	 * @return StudentInfo JSON格式字符串
	 */
	public static String setStudentInfoCache(int studentId, StudentInfo studentInfo) {
		JedisWrapper jedis = null;
		if (studentInfo == null) {
			return null;
		}
		try {
			jedis = getInstance();
			String key = String.format(STUDENT_INFO_KEY, studentId);
			String value = new Gson().toJson(studentInfo,StudentInfo.class);
			jedis.STRINGS.setEx(key, EXPIRE_TIME,value);
			return value;
		} catch (Exception e) {
			logger.error("UserRedisSource.setStudentInfoCache (" + studentId + ", " + new Gson().toJson(studentInfo,StudentInfo.class) + ") execute exception.", e);
		}
		return null;
	}
	
	
	/**
	 * 存储老师信息
	 * @param teacherId
	 * @param teacherInfo
	 * @return TeacherInfo JSON格式字符串
	 */
	public static String setTeacherInfoCache(int teacherId, TeacherInfo teacherInfo) {
		JedisWrapper jedis = null;
		if (teacherInfo == null) {
			return null;
		}
		try {
			jedis = getInstance();
			String key = String.format(TEACHER_INFO_KEY, teacherId);
			String value = new Gson().toJson(teacherInfo,TeacherInfo.class);
			jedis.STRINGS.setEx(key, EXPIRE_TIME,value);
			return value;
		} catch (Exception e) {
			logger.error("UserRedisSource.setTeacherInfoCache (" + teacherId + ", " +new Gson().toJson(teacherInfo,TeacherInfo.class) + ") execute exception.", e);
		}
		return null;
	}
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @return UserInfo对象
	 */
	public static UserInfo getUserInfoCache(int userId) {
		JedisWrapper jedis = null;
		UserInfo userInfo = null;
		try {
			jedis = getInstance();
			String key = String.format(USER_INFO_KEY, userId);
			String value = jedis.STRINGS.get(key);
			if (StringUtils.isNotBlank(value)) {
				userInfo = new Gson().fromJson(value, UserInfo.class);
			}
			return userInfo;
		} catch (Exception e) {
			logger.error("UserRedisSource.getUserInfoCache (" + userId + ")execute exception.", e);
		}
		return null;
	}
	
	
	/**
	 * 获取用户头像
	 * @param key
	 * @return portrait
	 */
	public static String getUserPortraitCache(int userId) {
		JedisWrapper jedis = null;
		try {
			jedis = getInstance();
			String key = String.format(USER_PORTRAIT_KEY, userId);
			return jedis.STRINGS.get(key);
		} catch (Exception e) {
			logger.error("UserRedisSource.getUserPortraitCache (" + userId + ") execute exception.", e);
		}
		return null;
	}
	
	/**
	 * 获取学生信息
	 * @param studentId
	 * @return StudentInfo对象
	 */
	public static StudentInfo getStudentInfoCache(int studentId) {
		JedisWrapper jedis = null;
		StudentInfo studentInfo = null;
		try {
			jedis = getInstance();
			String key = String.format(STUDENT_INFO_KEY, studentId);
			String value = jedis.STRINGS.get(key);
			if (StringUtils.isNotBlank(value)) {
				studentInfo = new Gson().fromJson(value, StudentInfo.class);
			}
			return studentInfo;
		} catch (Exception e) {
			logger.error("UserRedisSource.getStudentInfoCache (" + studentId + ")execute exception.", e);
		}
		return null;
	}
	
	/**
	 * 获取老师信息
	 * @param teacherId
	 * @return TeacherInfo对象
	 */
	public static TeacherInfo getTeacherInfoCache(int teacherId) {
		JedisWrapper jedis = null;
		TeacherInfo teacherInfo = null;
		try {
			jedis = getInstance();
			String key = String.format(TEACHER_INFO_KEY, teacherId);
			String value = jedis.STRINGS.get(key);
			if (StringUtils.isNotBlank(value)) {
				teacherInfo = new Gson().fromJson(value, TeacherInfo.class);
			}
			return teacherInfo;
		} catch (Exception e) {
			logger.error("UserRedisSource.getTeacherInfoCache (" + teacherId + ")execute exception.", e);
		}
		return null;
	}
	
	

	/**
	 * 删除注册缓存信息
	 * @param userId
	 * @return 删除长度
	 */
	public static long delUserInfoCache(int userId) {
		JedisWrapper jedis = null;
		try {
			jedis = getInstance();
			String key = String.format(USER_INFO_KEY, userId);
			return jedis.KEYS.del(key);
		} catch (Exception e) {
			logger.error("UserRedisSource.delUserInfoCache (" + userId + ") execute exception.", e);
		}
		return 0;
	}
	
	/**
	 * 删除学生缓存信息
	 * @param studentId
	 * @return 删除长度
	 */
	public static long delStudentInfoCache(int studentId) {
		JedisWrapper jedis = null;
		try {
			jedis = getInstance();
			String key = String.format(STUDENT_INFO_KEY, studentId);
			return jedis.KEYS.del(key);
		} catch (Exception e) {
			logger.error("UserRedisSource.delStudentInfoCache (" + studentId + ") execute exception.", e);
		}
		return 0;
	}
	
	
	/**
	 * 删除老师缓存信息
	 * @param teacherId
	 * @return 删除长度
	 */
	public static long delTeacherInfoCache(int teacherId) {
		JedisWrapper jedis = null;
		try {
			jedis = getInstance();
			String key = String.format(TEACHER_INFO_KEY, teacherId);
			return jedis.KEYS.del(key);
		} catch (Exception e) {
			logger.error("UserRedisSource.delTeacherInfoCache (" + teacherId + ") execute exception.", e);
		}
		return 0;
	}
	
	
	
	public static String setUserToken(int userId, String token,int platform,int expireTime) {
		JedisWrapper jedis = null;
		try {
			if (expireTime == 0) {
				expireTime = 7*24*60*60;
			}
			jedis = getInstance();
			String key = String.format(USER_TOKEN_KEY, userId,platform);
			jedis.STRINGS.setEx(key, expireTime, token);
			return token;
		} catch (Exception e) {
			logger.error("UserRedisSource.setUserToken(" + userId + ", " + token + ", " + expireTime + ") execute exception.", e);
		}
		return null;
	}
	
	public static void delUserToken(int userId,int platform) {
		JedisWrapper jedis = null;
		try {
			jedis = getInstance();
			String key = String.format(USER_TOKEN_KEY, userId,platform);
			jedis.KEYS.del(key);
		} catch (Exception e) {
			logger.error("UserRedisSource.delUserToken(" + userId + ") execute exception.", e);
		}
	}
}
