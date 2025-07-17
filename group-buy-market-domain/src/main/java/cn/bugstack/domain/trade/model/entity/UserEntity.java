package cn.bugstack.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model
 * @Description: 用户实体对象
 * @Author: Daniel G
 * @Create: 2025-07-17 16:54:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    /**
     * 用户ID
     */
    private String userId;
}
