package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.ClassroomComment;
import com.melot.talkee.driver.domain.Requirement;
import com.melot.talkee.mybatis.MybatisMapper;

import java.util.List;
import java.util.Map;

/**
 * Title: 学生对老师的课堂评价
 * <p>
 * Description:
 * </p>
 *
 * @author 范玉全<a href="mailto:yuquan.fan@melot.cn">
 * @version V1.0
 * @since 9:26 2017/7/24
 */
public interface ClassroomCommentMapper extends MybatisMapper {

    int insert(ClassroomComment record);

    int insertSelective(ClassroomComment record);

    ClassroomComment selectBySelective(Map<String, Object> param);

    int updateByPrimaryKeySelective(ClassroomComment record);

    int updateByPrimaryKey(ClassroomComment record);

}
