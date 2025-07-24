package cn.bugstack.domain.activity.service.trial.node;


import cn.bugstack.domain.activity.model.entity.MarketProductEntity;
import cn.bugstack.domain.activity.model.entity.TrialBalanceEntity;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.model.valobj.SkuVO;
import cn.bugstack.domain.activity.service.discount.IDiscountCalculateService;
import cn.bugstack.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.domain.activity.service.trial.thread.QueryGroupBuyActivityDiscountVOThreadTask;
import cn.bugstack.domain.activity.service.trial.thread.QuerySkuVOFromDBThreadTask;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.trial.node
 * @Description: 营销优惠节点
 * @Author: Daniel G
 * @Create: 2025-07-15 22:18:43
 */
@Slf4j
//@Service
public class MarketNode extends AbstractGroupBuyMarketSupport<MarketProductEntity,
        DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private TagNode tagNode;

    @Resource
    private ErrorNode errorNode;

    /**
     * <a href="https://bugstack.cn/md/road-map/spring-dependency-injection.html">Spring 注入详细说明</a>
     */
    @Resource
    private Map<String, IDiscountCalculateService> discountCalculateServiceMap;

    /**
     * 在 MarketNode2CompletableFuture 继承的子类实现一个 CompletableFuture 多线程方式。
     * <p>
     * 1. CompletableFuture：适用于大多数现代 Java 应用，尤其在需要灵活任务编排时。
     * 2.  FutureTask：任务极度简单，适合简单场景。
     * <p>
     * | 对比维度    | FutureTask             | CompletableFuture                |
     * | :--------------- | :-------------------------- | :------------------------------------- |
     * | 任务编排能力 | 弱（需手动管理多个 Future） | 强（内置 `thenApply`、`allOf` 等方法） |
     * | 代码简洁性  | 冗余（显式调用 `get()`）    | 简洁（链式调用，逻辑内聚）             |
     * | 异常处理   | 繁琐（需捕获多个异常）      | 优雅（支持 `exceptionally` 统一处理）  |
     * | 线程阻塞     | 可能多次阻塞主线程          | 非阻塞或单次阻塞（如 `join()`）        |
     * | 适用场景     | 简单任务、低版本 Java 环境  | 复杂异步流程、Java 8+ 环境             |
     * <p>
     * 使用；MarketNode 的 @Service 注释掉，MarketNode2CompletableFuture 的 @Service 打开，就可以使用了。
     */
    @Override
    protected void multiThread(MarketProductEntity requestParameter,
                               DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        /*
         * 这两个任务被并行执行
         * 任务创建 ：封装查询逻辑到 Callable 对象中
         * 任务包装 ：用 FutureTask 包装,FutureTask 是Java并发包中的核心类,实现了 Future 和 Runnable 接口,可以包装 Callable 任务,提供异步执行和结果获取功能
         * 异步执行 ：提交到线程池,execute() 方法是异步的，不会阻塞当前线程, 线程池会分配一个工作线程来执行这个任务
         * 结果获取 ：后续可通过 futureTask.get() 控制何时获取结果
         */
        /*异步查询活动配置*/
        QueryGroupBuyActivityDiscountVOThreadTask queryGroupBuyActivityDiscountVOThreadTask =
                new QueryGroupBuyActivityDiscountVOThreadTask(requestParameter.getActivityId(),
                        requestParameter.getSource(), requestParameter.getChannel(), requestParameter.getGoodsId(),
                        repository);
        FutureTask<GroupBuyActivityDiscountVO> groupBuyActivityDiscountVOFutureTask =
                new FutureTask<>(queryGroupBuyActivityDiscountVOThreadTask);
        threadPoolExecutor.execute(groupBuyActivityDiscountVOFutureTask);

        /* 异步查询商品信息 - 在实际生产中，商品有同步库或者调用接口查询。这里暂时使用DB方式查询。*/
        QuerySkuVOFromDBThreadTask querySkuVOFromDBThreadTask =
                new QuerySkuVOFromDBThreadTask(requestParameter.getGoodsId(), repository);
        FutureTask<SkuVO> skuVOFutureTask = new FutureTask<>(querySkuVOFromDBThreadTask);
        threadPoolExecutor.execute(skuVOFutureTask);

        /* 写入上下文 - 对于一些复杂场景，获取数据的操作，有时候会在下N个节点获取，这样前置查询数据，可以提高接口响应效率*/
        dynamicContext.setGroupBuyActivityDiscountVO(groupBuyActivityDiscountVOFutureTask.get(timeout,
                TimeUnit.MINUTES));
        dynamicContext.setSkuVO(skuVOFutureTask.get(timeout, TimeUnit.MINUTES));

        log.info("拼团商品查询试算服务-MarketNode userId:{} 异步线程加载数据「GroupBuyActivityDiscountVO、SkuVO」完成",
                requestParameter.getUserId());
    }

    @Override
    public TrialBalanceEntity doApply(MarketProductEntity requestParameter,
                                      DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("拼团商品查询试算服务-MarketNode userId:{} requestParameter:{}", requestParameter.getUserId(),
                JSON.toJSONString(requestParameter));

        // 获取上下文数据
        GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = dynamicContext.getGroupBuyActivityDiscountVO();
        if (null == groupBuyActivityDiscountVO) {
            return router(requestParameter, dynamicContext);
        }
        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount =
                groupBuyActivityDiscountVO.getGroupBuyDiscount();

        SkuVO skuVO = dynamicContext.getSkuVO();
        if (null == groupBuyDiscount || null == skuVO) {
            return router(requestParameter, dynamicContext);
        }

        // 拼团优惠试算 选择某种优惠策略
        IDiscountCalculateService discountCalculateService =
                discountCalculateServiceMap.get(groupBuyDiscount.getMarketPlan());
        if (null == discountCalculateService) {
            log.info("不存在{}类型的折扣计算服务，支持类型为:{}", groupBuyDiscount.getMarketPlan(),
                    JSON.toJSONString(discountCalculateServiceMap.keySet()));
            throw new AppException(ResponseCode.E0001.getCode(), ResponseCode.E0001.getInfo());
        }

        // 计算优惠
        BigDecimal payPrice = discountCalculateService.calculate(requestParameter.getUserId(),
                skuVO.getOriginalPrice(), groupBuyDiscount); //这里计算的是优惠后最后的支付金额
        BigDecimal deductionPrice = skuVO.getOriginalPrice().subtract(payPrice);
        dynamicContext.setDeductionPrice(deductionPrice);
        dynamicContext.setPayPrice(payPrice);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 不存在配置的拼团活动，走异常节点
        if (null == dynamicContext.getGroupBuyActivityDiscountVO() ||
                null == dynamicContext.getGroupBuyActivityDiscountVO().getGroupBuyDiscount() ||
                null == dynamicContext.getSkuVO() ||
                null == dynamicContext.getDeductionPrice() ||
                null == dynamicContext.getPayPrice()) {
            return errorNode;
        }
        return tagNode;
    }
}
