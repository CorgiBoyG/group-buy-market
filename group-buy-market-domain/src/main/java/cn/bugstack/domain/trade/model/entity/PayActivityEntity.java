package cn.bugstack.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model
 * @Description: 拼团，支付活动实体对象
 * @Author: Daniel G
 * @Create: 2025-07-17 16:54:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayActivityEntity {

    /**
     * 拼单组队ID
     */
    private String teamId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 拼团活动的开始时间
     */
    private Date startTime;
    /**
     * 拼团活动的结束时间
     */
    private Date endTime;
    /**
     * 如果开始，这个拼团活动的有效时长（分钟）
     */
    private Integer validTime;
    /**
     * 目标数量
     */
    private Integer targetCount;
}
