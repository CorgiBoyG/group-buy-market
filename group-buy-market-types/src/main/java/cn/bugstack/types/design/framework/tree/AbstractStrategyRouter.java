package cn.bugstack.types.design.framework.tree;


import lombok.Getter;
import lombok.Setter;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.types.design.framework.tree
 * @Description: 策略路由抽象类
 * @Author: Daniel G
 * @Create: 2025-07-15 21:58:34
 */

public abstract class AbstractStrategyRouter<T, D, R> implements StrategyMapper<T, D, R>, StrategyHandler<T, D, R> {

    @Getter
    @Setter
    protected StrategyHandler<T, D, R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    public R router(T requestParameter, D dynamicContext) throws Exception {
        StrategyHandler<T, D, R> strategyHandler = get(requestParameter, dynamicContext);
        if (null != strategyHandler) return strategyHandler.apply(requestParameter, dynamicContext);
        return defaultStrategyHandler.apply(requestParameter, dynamicContext);
    }

}
