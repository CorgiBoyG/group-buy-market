package cn.bugstack.domain.activity.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.model.valobj
 * @Description: 折扣优惠类型
 * @Author: Daniel G
 * @Create: 2025-07-16 13:19:02
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DiscountTypeEnum {

    BASE(0, "基础优惠"),
    TAG(1, "人群标签"),
    ;

    private Integer code;
    private String info;

    public static DiscountTypeEnum get(Integer code) {
        switch (code) {
            case 0:
                return BASE;
            case 1:
                return TAG;
            default:
                throw new RuntimeException("err code!");
        }
    }

}
