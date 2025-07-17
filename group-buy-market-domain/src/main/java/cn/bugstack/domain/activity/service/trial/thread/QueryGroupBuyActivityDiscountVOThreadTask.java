package cn.bugstack.domain.activity.service.trial.thread;


import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.model.valobj.SCSkuActivityVO;

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
     * 活动ID
     */
    private final Long activityId;

    /**
     * 来源
     */
    private final String source;

    /**
     * 渠道
     */
    private final String channel;

    /**
     * 商品ID
     */
    private final String goodsId;

    /**
     * 活动仓储
     */
    private final IActivityRepository activityRepository;

    public QueryGroupBuyActivityDiscountVOThreadTask(Long activityId, String source, String channel, String goodsId,
                                                     IActivityRepository activityRepository) {
        this.activityId = activityId;
        this.source = source;
        this.channel = channel;
        this.goodsId = goodsId;
        this.activityRepository = activityRepository;
    }


    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        // 判断是否存在可用的活动ID
        Long availableActivityId = activityId;
        if (null == activityId) {
            // 根据商品source、channel、goodsId 查询渠道商品活动配置关联配置
            SCSkuActivityVO scSkuActivityVO = activityRepository.querySCSkuActivityBySCGoodsId(source, channel,
                    goodsId);
            if (null == scSkuActivityVO) return null;
            availableActivityId = scSkuActivityVO.getActivityId();
        }
        
        // 查询拼团活动的折扣优惠配置
        return activityRepository.queryGroupBuyActivityDiscountVO(availableActivityId);
    }

}
