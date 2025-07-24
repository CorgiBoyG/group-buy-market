package cn.bugstack.test.domain.trade;


import cn.bugstack.domain.trade.model.entity.TradePaySettlementEntity;
import cn.bugstack.domain.trade.model.entity.TradePaySuccessEntity;
import cn.bugstack.domain.trade.service.ITradeSettlementOrderService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.test.domain.trade
 * @Description: 拼团交易结算服务测试
 * @Author: Daniel G
 * @Create: 2025-07-18 15:33:07
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITradeSettlementOrderServiceTest {

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;

    @Test
    public void test_settlementMarketPayOrder() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1); //添加后，相当于也是一个application了

        TradePaySuccessEntity tradePaySuccessEntity = new TradePaySuccessEntity();
        tradePaySuccessEntity.setSource("s01");
        tradePaySuccessEntity.setChannel("c01");
        tradePaySuccessEntity.setUserId("xfg01");
        tradePaySuccessEntity.setOutTradeNo("546178225045");
        tradePaySuccessEntity.setOutTradeTime(new Date());
        TradePaySettlementEntity tradePaySettlementEntity =
                tradeSettlementOrderService.settlementMarketPayOrder(tradePaySuccessEntity);
        log.info("请求参数:{}", JSON.toJSONString(tradePaySuccessEntity));
        log.info("测试结果:{}", JSON.toJSONString(tradePaySettlementEntity));

        // 等待，消息消费。测试后，可主动关闭。相当于没添加这个，发送完了，这里就直接结束了
        countDownLatch.await();
    }
}
