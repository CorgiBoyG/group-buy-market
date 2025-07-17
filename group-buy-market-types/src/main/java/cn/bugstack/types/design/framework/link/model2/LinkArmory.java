package cn.bugstack.types.design.framework.link.model2;

import cn.bugstack.types.design.framework.link.model2.chain.BusinessLinkedList;
import cn.bugstack.types.design.framework.link.model2.handler.ILogicHandler;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 链路装配
 * @create 2025-01-18 10:02
 */
public class LinkArmory<T, D, R> {
    /**
     * 是 Model2 责任链模式中的 链路装配器 ，负责简化责任链的创建和配置过程。
     * 装配流程：
     * <p>
     * 1. 创建链表 ：内部创建一个 BusinessLinkedList 实例
     * 2. 批量添加 ：遍历传入的处理器数组，依次添加到链表中
     * 3. 顺序保证 ：处理器按照传入顺序添加到链表中
     * 4. 链表返回 ：通过 getLogicLink() 方法获取装配好的责任链
     */


    private final BusinessLinkedList<T, D, R> logicLink;

    // 链路装配
    // @SafeVarargs 注解消除编译器警告
    @SafeVarargs
    public LinkArmory(String linkName, ILogicHandler<T, D, R>... logicHandlers) {
        logicLink = new BusinessLinkedList<>(linkName);
        for (ILogicHandler<T, D, R> logicHandler : logicHandlers) {
            logicLink.add(logicHandler);
        }
    }

    public BusinessLinkedList<T, D, R> getLogicLink() {
        return logicLink;
    }

}
