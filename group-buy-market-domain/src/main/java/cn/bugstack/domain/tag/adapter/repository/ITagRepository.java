package cn.bugstack.domain.tag.adapter.repository;

import cn.bugstack.domain.tag.model.entity.CrowdTagsJobEntity;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 人群标签仓储接口
 * @create 2024-12-28 11:26
 */
public interface ITagRepository {

    CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId);

    /**
     * 将用户ID添加到指定的人群标签中 ，实现用户标签关联的双重存储机制。
     *
     * @param tagId
     * @param userId
     */
    void addCrowdTagsUserId(String tagId, String userId);

    void updateCrowdTagsStatistics(String tagId, int count);

}
