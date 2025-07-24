package cn.bugstack.domain.trade.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.valobj
 * @Description: 回调配置值对象
 * @Author: Daniel G
 * @Create: 2025-07-24 15:02:38
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyConfigVO {

    /**
     * 回调方式；MQ、HTTP
     */
    private NotifyTypeEnumVO notifyType;
    /**
     * 回调消息
     */
    private String notifyMQ;
    /**
     * 回调地址
     */
    private String notifyUrl;

}
