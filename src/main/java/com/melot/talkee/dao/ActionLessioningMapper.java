package com.melot.talkee.dao;

import java.util.Map;

import com.melot.talkee.driver.domain.ActionLessioning;
import com.melot.talkee.mybatis.MybatisMapper;

public interface ActionLessioningMapper extends MybatisMapper {
    int deleteByPrimaryKey(Integer peroidId);

    int insert(ActionLessioning record);

    int insertSelective(ActionLessioning record);

    ActionLessioning selectByPrimaryKey(Integer peroidId);

    int updateByPrimaryKeySelective(ActionLessioning record);

    int updateByPrimaryKey(ActionLessioning record);

    ActionLessioning selectByPeriodAndUser(Map<String, Object> param);

    ActionLessioning selectFirstByPeriodUser(Map<String, Object> param);

    int updateTeacherInOutByPeriod(ActionLessioning record);

}