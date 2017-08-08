package com.melot.talkee.dao;

import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.AdminCity;
import com.melot.talkee.mybatis.MybatisMapper;

public interface AdminCityMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer id);

    int insert(AdminCity record);

    int insertSelective(AdminCity record);

    AdminCity selectByPrimaryKey(Integer id);
    
    List<AdminCity> selectByParentId(Integer parentId);

    int updateByPrimaryKeySelective(AdminCity record);

    int updateByPrimaryKey(AdminCity record);
    
    AdminCity selectByNameAndType(Map<String, Object> param);
}