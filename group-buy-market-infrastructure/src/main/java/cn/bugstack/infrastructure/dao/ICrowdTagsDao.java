package cn.bugstack.infrastructure.dao;

import cn.bugstack.infrastructure.dao.po.CrowdTags;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 人群标签
 * @create 2024-12-28 11:49
 */
@Mapper
public interface ICrowdTagsDao {

    /**
     * 根据tagId 更新statistics
     *
     * @param crowdTagsReq:CrowdTags，封装了tagId、和需要被添加或减少的statistics人群标签统计量
     */
    void updateCrowdTagsStatistics(CrowdTags crowdTagsReq);

}
