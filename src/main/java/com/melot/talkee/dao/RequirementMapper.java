package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.Requirement;
import com.melot.talkee.mybatis.MybatisMapper;

import java.util.List;
import java.util.Map;

/**
 * Title:
 * <p>
 * Description:
 * </p>
 *
 * @author 范玉全<a href="mailto:yuquan.fan@melot.cn">
 * @version V1.0
 * @since 10:33 2017/7/25
 */
public interface RequirementMapper extends MybatisMapper {
    List<Requirement> selectRequirement(Map<String, Object> param);

    List<Requirement> selectRequirementList();
}
