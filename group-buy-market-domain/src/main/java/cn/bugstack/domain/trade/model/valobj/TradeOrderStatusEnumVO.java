package cn.bugstack.domain.trade.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.valobj
 * @Description: 交易订单状态枚举
 * @Author: Daniel G
 * @Create: 2025-07-17 17:00:14
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TradeOrderStatusEnumVO {

    CREATE(0, "初始锁定"),
    COMPLETE(1, "消费完成"),
    CLOSE(2, "用户退单"),
    ;

    private Integer code;
    private String info;

    public static TradeOrderStatusEnumVO valueOf(Integer code) {
        switch (code) {
            case 0:
                return CREATE;
            case 1:
                return COMPLETE;
            case 2:
                return CLOSE;
        }
        return CREATE;
    }

}
