package cn.bugstack.domain.activity.service.trial.node;


import cn.bugstack.domain.activity.model.entity.MarketProductEntity;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.model.valobj.SCSkuActivityVO;
import cn.bugstack.domain.activity.model.valobj.SkuVO;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.trial.node
 * @Description: 营销优惠节点 用CompletableFuture
 * @Author: Daniel G
 * @Create: 2025-07-24 18:29:38
 */
@Slf4j
@Service
public class MarketNode2CompletableFuture extends MarketNode {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void multiThread(MarketProductEntity requestParameter,
                               DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        // 异步查询活动配置
        CompletableFuture<GroupBuyActivityDiscountVO> groupBuyActivityDiscountVOCompletableFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        // 判断是否存在可用的活动ID
                        Long availableActivityId = requestParameter.getActivityId();
                        if (null == requestParameter.getActivityId()) {
                            // 根据商品source、channel、goodsId 查询渠道商品活动配置关联配置
                            SCSkuActivityVO scSkuActivityVO =
                                    repository.querySCSkuActivityBySCGoodsId(requestParameter.getSource(),
                                            requestParameter.getChannel(), requestParameter.getGoodsId());
                            if (null == scSkuActivityVO) return null;
                            availableActivityId = scSkuActivityVO.getActivityId();
                        }
                        // 查询拼团活动的折扣优惠配置
                        return repository.queryGroupBuyActivityDiscountVO(availableActivityId);
                    } catch (Exception e) {
                        log.error("异步查询活动配置异常", e);
                        return null;
                    }
                }, threadPoolExecutor);

        // 异步查询商品信息 - 在实际生产中，商品有同步库或者调用接口查询。这里暂时使用DB方式查询。
        CompletableFuture<SkuVO> skuVOCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return repository.querySkuByGoodsId(requestParameter.getGoodsId());
            } catch (Exception e) {
                log.error("异步查询商品信息异常", e);
                return null;
            }
        }, threadPoolExecutor);

        // 等待所有异步任务完成并写入上下文
        CompletableFuture.allOf(groupBuyActivityDiscountVOCompletableFuture, skuVOCompletableFuture)
                .thenRun(() -> {
                    dynamicContext.setGroupBuyActivityDiscountVO(groupBuyActivityDiscountVOCompletableFuture.join());
                    dynamicContext.setSkuVO(skuVOCompletableFuture.join());
                }).join();

        log.info("拼团商品查询试算服务-MarketNode userId:{} 异步线程加载数据「GroupBuyActivityDiscountVO、SkuVO」完成",
                requestParameter.getUserId());
    }
}
