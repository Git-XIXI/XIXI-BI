package com.xixi.bi.model.dto.chart;


import com.xixi.bi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;


/**
 * 查询请求
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     *  用户 id
     */
    private Long userId;

    /**
     *  名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;


    /**
     * 图表类型
     */
    private String chartType;


    private static final long serialVersionUID = 1L;
}