package cn.bugstack.types.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.types.enums
 * @Description: 拼团订单枚举
 * @Author: Daniel G
 * @Create: 2025-07-18 14:45:06
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum GroupBuyOrderEnumVO {

    PROGRESS(0, "拼单中"),
    COMPLETE(1, "完成"),
    FAIL(2, "失败"),
    COMPLETE_FAIL(3, "完成-含退单"),
    ;

    private Integer code;
    private String info;

    public static GroupBuyOrderEnumVO valueOf(Integer code) {
        switch (code) {
            case 0:
                return PROGRESS;
            case 1:
                return COMPLETE;
            case 2:
                return FAIL;
            case 3:
                return COMPLETE_FAIL;
        }
        throw new RuntimeException("err code not exist!");
    }

}

