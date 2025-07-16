package cn.bugstack.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 人群标签
 * @create 2024-12-28 11:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrowdTags {

    /** 自增ID */
    private Long id;
    /** 人群ID */
    private String tagId;
    /** 人群名称 */
    private String tagName;
    /** 人群描述 */
    private String tagDesc;
    /** 人群标签统计量 */
    private Integer statistics;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
