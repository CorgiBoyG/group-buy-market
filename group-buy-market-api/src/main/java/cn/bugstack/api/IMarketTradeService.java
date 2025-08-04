package cn.bugstack.api;


import cn.bugstack.api.dto.*;
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
     * 营销锁单
     *
     * @param lockMarketPayOrderRequestDTO 锁单商品信息
     * @return 锁单结果信息
     */
    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO);

    /**
     * 营销结算
     *
     * @param requestDTO 结算商品信息
     * @return 结算结果信息
     */
    Response<SettlementMarketPayOrderResponseDTO> settlementMarketPayOrder(SettlementMarketPayOrderRequestDTO requestDTO);

    /**
     * 营销拼团退单
     *
     * @param requestDTO 退单请求信息
     * @return 退单结果信息
     */
    Response<RefundMarketPayOrderResponseDTO> refundMarketPayOrder(RefundMarketPayOrderRequestDTO requestDTO);

}
