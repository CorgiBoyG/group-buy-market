package cn.bugstack.infrastructure.dao;

import cn.bugstack.infrastructure.dao.po.CrowdTagsDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 人群标签明细
 * @create 2024-12-28 11:49
 */
@Mapper
public interface ICrowdTagsDetailDao {

    /**
     * 根据tagId 和userId 插入CrowdTagsDetail数据
     *
     * @param crowdTagsDetailReq:CrowdTagsDetail 封装了tagId 和userId
     */
    void addCrowdTagsUserId(CrowdTagsDetail crowdTagsDetailReq);

}
