package cn.bugstack.types.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.types.enums
 * @Description: 回调任务状态枚举
 * @Author: Daniel G
 * @Create: 2025-07-18 22:17:16
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum NotifyTaskHTTPEnumVO {

    SUCCESS("success", "成功"),
    ERROR("error", "失败"),
    NULL(null, "空执行"),
    ;

    private String code;
    private String info;

}
