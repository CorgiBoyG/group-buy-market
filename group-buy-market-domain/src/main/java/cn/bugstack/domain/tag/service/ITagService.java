package cn.bugstack.domain.tag.service;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 人群标签服务接口
 * @create 2024-12-28 11:26
 */
public interface ITagService {

    /**
     * 执行人群标签批次任务
     *
     * @param tagId   人群ID
     * @param batchId 批次ID
     */
    void execTagBatchJob(String tagId, String batchId);

}
