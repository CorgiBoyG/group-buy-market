package cn.bugstack.infrastructure.dao.po;

import cn.bugstack.infrastructure.dao.po.base.Page;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户拼单明细
 * @create 2025-01-11 08:42
 */
//设置 callSuper = true 意味着：
//1. 包含父类字段 ：生成的 equals() 和 hashCode() 方法会调用父类的相应方法
//2. 完整性保证 ：确保比较对象时不仅比较当前类的字段，还会比较父类 Page 中的字段
//3. 避免逻辑错误 ：如果不设置 callSuper = true ，两个对象即使父类字段不同，也可能被认为是相等的
@EqualsAndHashCode(callSuper = true) //用于自动生成 equals() 和 hashCode() 方法。
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyOrderList extends Page {

    /**
     * 自增ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 拼单组队ID
     */
    private String teamId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 活动开始时间
     */
    private Date startTime;
    /**
     * 活动结束时间
     */
    private Date endTime;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 渠道
     */
    private String source;
    /**
     * 来源
     */
    private String channel;
    /**
     * 原始价格
     */
    private BigDecimal originalPrice;
    /**
     * 折扣金额
     */
    private BigDecimal deductionPrice;
    /**
     * 最后的支付金额
     */
    private BigDecimal payPrice;
    /**
     * 状态；0初始锁定、1消费完成
     */
    private Integer status;
    /**
     * 外部交易单号-确保外部调用唯一幂等
     */
    private String outTradeNo;
    /**
     * 外部交易时间
     */
    private Date outTradeTime;
    /**
     * 唯一业务ID
     */
    private String bizId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
