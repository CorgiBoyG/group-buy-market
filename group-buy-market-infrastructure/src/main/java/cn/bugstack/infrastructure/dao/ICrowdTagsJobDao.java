package cn.bugstack.infrastructure.dao;

import cn.bugstack.infrastructure.dao.po.CrowdTagsJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 人群标签任务
 * @create 2024-12-28 11:50
 */
@Mapper
public interface ICrowdTagsJobDao {

    /**
     * 根据tagId和batchId 查询 CrowdTagsJob
     *
     * @param crowdTagsJobReq:CrowdTagsJob 封装了tagId和batchId
     * @return
     */
    CrowdTagsJob queryCrowdTagsJob(CrowdTagsJob crowdTagsJobReq);

}
