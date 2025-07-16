package cn.bugstack.domain.activity.service.trial.thread;


import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.util.concurrent.Callable;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.trial.thread
 * @Description: 查询营销配置任务
 * @Author: Daniel G
 * @Create: 2025-07-16 10:10:57
 */

/**
 * 该类实现了Java并发包中的 Callable 接口
 * Callable 是一个泛型接口，用于定义可以返回结果的异步任务
 * 泛型参数指定了任务执行后返回的数据类型
 * 与 Runnable 不同， Callable 可以返回结果并抛出异常
 */
public class QueryGroupBuyActivityDiscountVOThreadTask implements Callable<GroupBuyActivityDiscountVO> {

    /**
     * 来源
     */
    private final String source;

    /**
     * 渠道
     */
    private final String channel;

    /**
     * 活动仓储
     */
    private final IActivityRepository activityRepository;

    public QueryGroupBuyActivityDiscountVOThreadTask(String source, String channel,
                                                     IActivityRepository activityRepository) {
        this.source = source;
        this.channel = channel;
        this.activityRepository = activityRepository;
    }

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        return activityRepository.queryGroupBuyActivityDiscountVO(source, channel);
    }

}
