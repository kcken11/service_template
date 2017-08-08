package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.TeacherCommentToStudent;
import com.melot.talkee.mybatis.MybatisMapper;

import java.util.Map;

/**
 * Title:老师对学生的课堂评价
 * <p>
 * Description:
 * </p>
 *
 * @author 范玉全<a href="mailto:yuquan.fan@melot.cn">
 * @version V1.0
 * @since 9:23 2017/7/24
 */
public interface TeacherCommentToStudentMapper extends MybatisMapper {

    TeacherCommentToStudent selectByPrimaryKey(Integer histId);

    int insert(TeacherCommentToStudent record);

    int insertSelective(TeacherCommentToStudent record);

    TeacherCommentToStudent selectBySelective(Map<String, Object> param);

    int updateByPrimaryKeySelective(TeacherCommentToStudent record);

    int updateByPrimaryKey(TeacherCommentToStudent record);

}
