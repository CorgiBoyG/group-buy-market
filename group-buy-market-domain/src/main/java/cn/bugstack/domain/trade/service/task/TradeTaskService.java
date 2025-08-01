package cn.bugstack.domain.trade.service.task;


import cn.bugstack.domain.trade.adapter.port.ITradePort;
import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;
import cn.bugstack.domain.trade.service.ITradeTaskService;
import cn.bugstack.types.enums.NotifyTaskHTTPEnumVO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.task
 * @Description: 交易通知任务（MQ/HTTP）服务
 * @Author: Daniel G
 * @Create: 2025-07-27 16:07:19
 */
@Slf4j
@Service
public class TradeTaskService implements ITradeTaskService {

    @Resource
    private ITradeRepository repository;

    @Resource
    private ITradePort port;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public Map<String, Integer> execNotifyJob(String teamId) throws Exception {
        log.info("拼团交易-执行结算通知回调，指定 teamId:{}", teamId);

        // 根据teamId 指定查询未执行任务，返回一个元素的list
        List<NotifyTaskEntity> notifyTaskEntityList = repository.queryUnExecutedNotifyTaskList(teamId);

        return execNotifyJob(notifyTaskEntityList);
    }

    @Override
    public Map<String, Integer> execNotifyJob(NotifyTaskEntity notifyTaskEntity) throws Exception {
        log.info("拼团交易-执行结算通知回调，指定 teamId:{} notifyTaskEntity:{}", notifyTaskEntity.getTeamId(),
                JSON.toJSONString(notifyTaskEntity));
        return execNotifyJob(Collections.singletonList(notifyTaskEntity));
    }

    @Override
    public Map<String, Integer> execNotifyJob() throws Exception {
        log.info("拼团交易-执行结算通知任务");

        // 查询未执行任务
        List<NotifyTaskEntity> notifyTaskEntityList = repository.queryUnExecutedNotifyTaskList();

        return execNotifyJob(notifyTaskEntityList);
    }


    private Map<String, Integer> execNotifyJob(List<NotifyTaskEntity> notifyTaskEntityList) throws Exception {
        int successCount = 0, errorCount = 0, retryCount = 0;
        for (NotifyTaskEntity notifyTaskEntity : notifyTaskEntityList) {
            // 回调处理 success 成功，error 失败
            String response = port.groupBuyNotify(notifyTaskEntity);

            // 更新状态判断&变更数据库表回调任务状态
            if (NotifyTaskHTTPEnumVO.SUCCESS.getCode().equals(response)) { //成功
                int updateCount = repository.updateNotifyTaskStatusSuccess(notifyTaskEntity);
                if (1 == updateCount) {
                    successCount += 1;
                }
            } else if (NotifyTaskHTTPEnumVO.ERROR.getCode().equals(response)) { //失败
                if (notifyTaskEntity.getNotifyCount() > 4) {
                    int updateCount = repository.updateNotifyTaskStatusError(notifyTaskEntity);
                    if (1 == updateCount) {
                        errorCount += 1;
                    }
                } else { //重试
                    int updateCount = repository.updateNotifyTaskStatusRetry(notifyTaskEntity);
                    if (1 == updateCount) {
                        retryCount += 1;
                    }
                }
            }
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("waitCount", notifyTaskEntityList.size());
        resultMap.put("successCount", successCount);
        resultMap.put("errorCount", errorCount);
        resultMap.put("retryCount", retryCount);

        return resultMap;
    }
}
