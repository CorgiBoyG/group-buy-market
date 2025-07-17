package cn.bugstack.types.design.framework.link.model2.chain;

import cn.bugstack.types.design.framework.link.model2.handler.ILogicHandler;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 业务链路
 * @create 2025-01-18 10:27
 */
public class BusinessLinkedList<T, D, R> extends LinkedList<ILogicHandler<T, D, R>> implements ILogicHandler<T, D, R> {

    // 处理器只关注业务逻辑（ILogicHandler）
    // 链管理由 LinkedList 负责
    // 执行逻辑由 BusinessLinkedList 统一处理

    /*自己是个数据结构，本身也是一个处理器，支持链式嵌套*/

    public BusinessLinkedList(String name) {
        super(name);
    }

    /**
     * - 顺序遍历 ：从 first 节点开始，逐个访问链表中的处理器
     * - 短路执行 ：当某个处理器返回非 null 结果时，立即返回，不再执行后续处理器
     * - 完整遍历 ：如果所有处理器都返回 null，最终返回 null
     */
    @Override
    public R apply(T requestParameter, D dynamicContext) throws Exception {
        Node<ILogicHandler<T, D, R>> current = this.first;
        do {
            ILogicHandler<T, D, R> item = current.item;
            R apply = item.apply(requestParameter, dynamicContext);
            if (null != apply) return apply; //如果有业务 直接返回

            current = current.next;
        } while (null != current);

        return null;
    }

}
