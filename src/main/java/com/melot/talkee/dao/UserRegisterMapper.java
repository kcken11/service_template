package com.melot.talkee.dao;

import java.util.List;

import com.melot.talkee.driver.domain.UserInfo;
import com.melot.talkee.mybatis.MybatisMapper;

public interface UserRegisterMapper extends MybatisMapper{

	/**
	 * 根据相关注册信息查询
	 * @param userRegister
	 * @return
	 */
	List<UserInfo> getUserRegister(UserInfo userRegister);
	
	/**
	 * 根据用户ID获取注册信息
	 * @param userId
	 * @return
	 */
	UserInfo getUserRegisterById(Integer userId);
	
	/**
	 * 根据登录名获取用户注册信息
	 * @param loginName
	 * @return
	 */
	UserInfo getUserRegisterByLoginName(String loginName);
	
	
	/**
	 * 根据email获取用户注册信息
	 * @param email
	 * @return
	 */
	UserInfo getUserRegisterByEmail(String email);
	
	/**
	 * 根据phoneNum获取用户注册信息
	 * @param phoneNum
	 * @return
	 */
	UserInfo getUserRegisterByPhoneNum(String phoneNum);
	
	
	/**
	 * 获取userId
	 * @return
	 */
	Integer getUserRegisterId();
	
	/**
	 * 添加注册信息
	 * @param userRegister
	 */
	int insertUserRegister(UserInfo userRegister);
	
	/**
	 * 根据主键修改注册信息
	 * @param userRegister
	 */
	int updateUserRegisterById(UserInfo userRegister);
	
	/**
	 * 根据注册信息删除表数据
	 * @param userRegister
	 */
	int deleteUserRegister(UserInfo userRegister);

	int updateByPrimaryKeySelective(UserInfo userInfo);
}
