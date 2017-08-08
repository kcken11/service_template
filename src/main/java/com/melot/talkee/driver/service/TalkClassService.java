package com.melot.talkee.driver.service;

import java.util.List;

import com.melot.talkee.driver.domain.LessonRecord;
import com.melot.talkee.driver.domain.Whiteboard;

/**
 * Created by mn on 2017/5/9.
 */
public interface TalkClassService {

    /**
     * 进入课堂
     * @param userId 用户id
     * @param roleType 角色类型
     * @param periodId 课时id
     * @param platform 平台
     * @param deviceUid 设备id
     * @return TagCode
     */
    public String enterClass(Integer userId,Integer roleType, Integer periodId, Integer platform, String deviceUid);

    /**
     * 退出课堂
     * @param userId 用户id
     * @param periodId 课时id
     * @param platform 平台
     * @param deviceUid 设备id
     * @return TagCode
     */
    public String outClass(Integer userId,Integer roleType, Integer periodId, Integer platform, String deviceUid);

    /**
     * 记录白板数据
     * @param userId 用户id
     * @param periodId 课时id
     * @param segment 第几段
     * @param msgData 消息数据
     * @return TagCode
     */
    public String recordWhiteboard(Integer userId, Integer periodId, Integer segment, String msgData);

    /**
     * 获取白板数据列表
     * @param userId 用户id
     * @param periodId 课时id
     * @param segment 第几段
     * @return List<Whiteboard>
     */
    public List<Whiteboard> getWhiteboardList(Integer userId, Integer periodId, Integer segment);


    /**
     * 获取白板记录 单条（数据量大 分次调取）
     * @param periodId
     * @param segment
     * @param offset
     * @param limit
     * @return
     */
    public Whiteboard getWhiteBoard(Integer periodId,Integer segment,Integer offset,Integer limit);

    /**
     * 获取课程回放相关信息
     * @param periodId
     * @return
     */
    public LessonRecord getLessonRecord(Integer periodId);


    public Integer deleteOldRecord(Integer periodId);

}
