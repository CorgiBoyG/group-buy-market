package cn.bugstack.infrastructure.dcc;


import cn.bugstack.types.annotations.DCCValue;
import org.springframework.stereotype.Service;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.dcc
 * @Description: 动态配置服务
 * @Author: Daniel G
 * @Create: 2025-07-17 12:31:25
 */

@Service
public class DCCService {

    /**
     * 降级开关 0关闭、1开启
     */
    @DCCValue("downgradeSwitch:0")
    private String downgradeSwitch; // 降级开关

    @DCCValue("cutRange:100")
    private String cutRange; // 切量范围

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

}
