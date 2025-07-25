package cn.bugstack.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.config
 * @Description: TODO
 * @Author: Daniel G
 * @Create: 2025-07-25 16:36:54
 */
@ConfigurationProperties(prefix = "xfg.wrench.config", ignoreInvalidFields = true)
public class DynamicConfigCenterAutoProperties {

    /**
     * 系统名称
     */
    private String system;

    /**
     * dcc在redis中存储的key的前缀
     */
    public String getKey(String attributeName) {
        return this.system + "_dcc_" + attributeName;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }


}
