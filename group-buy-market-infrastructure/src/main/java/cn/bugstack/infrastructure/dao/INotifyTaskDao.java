package cn.bugstack.infrastructure.dao;

import cn.bugstack.infrastructure.dao.po.NotifyTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 回调任务
 * @create 2025-01-26 18:23
 */
@Mapper
public interface INotifyTaskDao {

    /**
     * 插入回调任务记录
     *
     * @param notifyTask 回调任务对象
     */
    void insert(NotifyTask notifyTask);

}
