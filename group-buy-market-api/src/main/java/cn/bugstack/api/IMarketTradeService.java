package cn.bugstack.api;


import cn.bugstack.api.dto.LockMarketPayOrderRequestDTO;
import cn.bugstack.api.dto.LockMarketPayOrderResponseDTO;
import cn.bugstack.api.response.Response;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.api
 * @Description: 营销交易服务接口
 * @Author: Daniel G
 * @Create: 2025-07-17 18:05:17
 */

public interface IMarketTradeService {

    /**
     * 锁定营销支付订单
     *
     * @param lockMarketPayOrderRequestDTO 锁定营销支付订单请求参数
     * @return 锁定营销支付订单响应结果
     */
    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO);

}
