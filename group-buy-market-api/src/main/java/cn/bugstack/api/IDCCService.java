package cn.bugstack.api;


import cn.bugstack.api.response.Response;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.api
 * @Description: DCC 动态配置中心
 * @Author: Daniel G
 * @Create: 2025-07-17 13:06:00
 */

public interface IDCCService {

    /**
     * 动态配置更新
     *
     * @param key
     * @param value
     * @return
     */
    Response<Boolean> updateConfig(String key, String value);
}
