package cn.bugstack.api;


import cn.bugstack.api.dto.LockMarketPayOrderRequestDTO;
import cn.bugstack.api.dto.LockMarketPayOrderResponseDTO;
import cn.bugstack.api.dto.SettlementMarketPayOrderRequestDTO;
import cn.bugstack.api.dto.SettlementMarketPayOrderResponseDTO;
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

}
