package cn.bugstack.types.design.framework.tree;


/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.types.design.framework.tree
 * @Description: 受理策略处理
 * * T 入参类型
 * * D 上下文参数
 * * R 返参类型
 * @Author: Daniel G
 * @Create: 2025-07-15 21:54:50
 */

public interface StrategyHandler<T, D, R> {

    StrategyHandler DEFAULT = (T, D) -> null;

    R apply(T requestParameter, D dynamicContext) throws Exception;

}
