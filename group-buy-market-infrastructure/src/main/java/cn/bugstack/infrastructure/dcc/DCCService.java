package cn.bugstack.infrastructure.dcc;


import cn.bugstack.types.annotations.DCCValue;
import cn.bugstack.types.common.Constants;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.dcc
 * @Description: 动态配置服务
 * @Author: Daniel G
 * @Create: 2025-07-17 12:31:25
 */

@Service
public class DCCService {

    @DCCValue("downgradeSwitch:0")
    private String downgradeSwitch; // 降级开关 0关闭、1开启降级

    @DCCValue("cutRange:100")
    private String cutRange; // 切量范围

    @DCCValue("scBlacklist:s02c02")
    private String scBlacklist; //商品来源黑名单列表

    @DCCValue("cacheSwitch:0")
    private String cacheOpenSwitch; //0 开缓存 1关闭

    public boolean isDowngradeSwitch() {
        return "1".equals(downgradeSwitch);
    }

    public boolean isCutRange(String userId) {
        // 计算哈希码的绝对值，因为哈希值可能为负数
        int hashCode = Math.abs(userId.hashCode());

        // 获取最后两位
        int lastTwoDigits = hashCode % 100;

        // 判断是否在切量范围内
        if (lastTwoDigits <= Integer.parseInt(cutRange)) {
            return true;
        }

        return false;
    }

    /**
     * 判断黑名单拦截渠道，true拦截 false放行
     */
    public boolean isSCBlackIntercept(String source, String channel) {
        List<String> list = Arrays.asList(scBlacklist.split(Constants.SPLIT));
        return list.contains(source + channel);
    }

    /**
     * 缓存开启开关，true为开启，1为关闭
     */
    public boolean isCacheOpenSwitch() {
        return "0".equals(cacheOpenSwitch);
    }
}
