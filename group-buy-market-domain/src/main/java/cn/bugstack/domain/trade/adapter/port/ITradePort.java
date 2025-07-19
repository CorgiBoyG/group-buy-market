package cn.bugstack.domain.trade.adapter.port;


import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.adapter.port
 * @Description: 交易接口服务接口
 * @Author: Daniel G
 * @Create: 2025-07-18 22:43:06
 */
public interface ITradePort {

    String groupBuyNotify(NotifyTaskEntity NotifyTaskEntity) throws Exception;

}
