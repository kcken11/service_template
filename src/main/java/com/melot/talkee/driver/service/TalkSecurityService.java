package com.melot.talkee.driver.service;

import com.melot.talkee.driver.domain.EcloudSource;

/**
 * title:OSS 临时令牌服务
 *
 */
public interface TalkSecurityService {

	EcloudSource getOssToken(Integer userId);
}
