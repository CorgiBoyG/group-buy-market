package cn.bugstack.domain.activity.service.trial.thread;


import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.valobj.SkuVO;

import java.util.concurrent.Callable;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.trial.thread
 * @Description: 查询商品信息任务
 * @Author: Daniel G
 * @Create: 2025-07-16 10:34:05
 */

public class QuerySkuVOFromDBThreadTask implements Callable<SkuVO> {

    /**
     * 商品ID
     */
    private final String goodsId;

    private final IActivityRepository activityRepository;

    public QuerySkuVOFromDBThreadTask(String goodsId, IActivityRepository activityRepository) {
        this.goodsId = goodsId;
        this.activityRepository = activityRepository;
    }

    @Override
    public SkuVO call() throws Exception {
        return activityRepository.querySkuByGoodsId(goodsId);
    }
}
