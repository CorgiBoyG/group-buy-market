package cn.bugstack.types.common;


/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.types.common
 * @Description: 属性值调整值对象
 * @Author: Daniel G
 * @Create: 2025-07-25 16:48:47
 */
public class AttributeVO {

    /**
     * 键 - 属性 fileName
     */
    private String attribute;

    /**
     * 值
     */
    private String value;

    public AttributeVO() {
    }

    public AttributeVO(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
