package com.melot.talkee.dao;

import java.util.List;

import com.melot.talkee.driver.domain.DetailCommentQuestion;
import com.melot.talkee.mybatis.MybatisMapper;

public interface DetailCommentQuestionMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer questionId);

    int insert(DetailCommentQuestion record);

    int insertSelective(DetailCommentQuestion record);

    DetailCommentQuestion selectByPrimaryKey(Integer questionId);

    int updateByPrimaryKeySelective(DetailCommentQuestion record);

    int updateByPrimaryKey(DetailCommentQuestion record);
    
    List<DetailCommentQuestion> selectBySelective(DetailCommentQuestion record);
}