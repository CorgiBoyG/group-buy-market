package cn.bugstack.api.dto;


import lombok.Data;

import java.util.List;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.api.dto
 * @Description: 回调请求对象
 * @Author: Daniel G
 * @Create: 2025-07-18 22:23:21
 */
@Data
public class NotifyRequestDTO {

    /**
     * 组队ID
     */
    private String teamId;
    /**
     * 外部单号
     */
    private List<String> outTradeNoList;

}
