package com.melot.talkee.dao;

import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.StudentInfo;
import com.melot.talkee.mybatis.MybatisMapper;

public interface UserStudentMapper extends MybatisMapper {

    int deleteByPrimaryKey(Integer userId);

    int insert(StudentInfo record);

    int insertSelective(StudentInfo record);

    StudentInfo selectByPrimaryKey(Integer userId);

    /**
     * 验证手机号码是否重复、或非当前userId的手机号是否重复
     * @param param phoneNum,userId
     * @return
     */
    List<StudentInfo> selectByPhoneNum(Map<String, Object> param);

    int updateByPrimaryKeySelective(StudentInfo record);

    int updateByPrimaryKey(StudentInfo record);

}