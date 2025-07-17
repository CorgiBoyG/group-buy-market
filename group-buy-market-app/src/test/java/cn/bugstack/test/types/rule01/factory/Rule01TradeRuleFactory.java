package cn.bugstack.test.types.rule01.factory;

import cn.bugstack.test.types.rule01.logic.RuleLogic101;
import cn.bugstack.test.types.rule01.logic.RuleLogic102;
import cn.bugstack.test.types.rule02.factory.Rule02TradeRuleFactory;
import cn.bugstack.types.design.framework.link.model1.ILogicLink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class Rule01TradeRuleFactory {
    /**
     * 想用单例链方式 做多例链 就不能让ruleLogic101是单例对象，其Scope不能为单例模式了，得是原型模式
     * 每获取的ruleLogic101对象都得是一个新的对象，才能去使用
     */

    // 负责链的装配和创建

    @Resource
    private RuleLogic101 ruleLogic101;
    @Resource
    private RuleLogic102 ruleLogic102;

    // 构建完整的责任链
    public ILogicLink<String, Rule02TradeRuleFactory.DynamicContext, String> openLogicLink() {
        ruleLogic101.appendNext(ruleLogic102); // 链式串联 不接收方法的返回值是完全合法的
        // 或者：ILogicLink<String, Rule02TradeRuleFactory.DynamicContext, String> lastNode = ruleLogic101.appendNext
        // (ruleLogic102);
        return ruleLogic101;
    }

    // 会改变链的结构
//    public ILogicLink<String, Rule02TradeRuleFactory.DynamicContext, String> openLogicLink2() {
//        ruleLogic101.appendNext(ruleLogic103);
//        return ruleLogic101;
//    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        private String age;
    }

}
