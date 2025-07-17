package cn.bugstack.domain.trade.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.valobj
 * @Description: 拼团进度值对象
 * @Author: Daniel G
 * @Create: 2025-07-17 17:07:58
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyProgressVO {

    /**
     * 目标数量
     */
    private Integer targetCount;
    /**
     * 完成数量
     */
    private Integer completeCount;
    /**
     * 锁单数量
     */
    private Integer lockCount;
}
