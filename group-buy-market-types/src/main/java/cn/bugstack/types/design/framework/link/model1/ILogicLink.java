package cn.bugstack.types.design.framework.link.model1;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 略规则责任链接口
 * @create 2025-01-18 09:09
 */
public interface ILogicLink<T, D, R> extends ILogicChainArmory<T, D, R> {
    // 继承装配接口，添加业务处理方法

    // 由于继承了ILogicChainArmory，相当于链装配和业务执行在同一套接口中 就是一种单实例链了 不能被填充多个，导致无法创建多套不同的链
    // 得每创建一个都是一个新的对象的模式，这样才能填充出来多套链，在大营销项目中中会有体现

    // 业务受理，由具体非抽象的实现类实现
    R apply(T requestParameter, D dynamicContext) throws Exception;

}
