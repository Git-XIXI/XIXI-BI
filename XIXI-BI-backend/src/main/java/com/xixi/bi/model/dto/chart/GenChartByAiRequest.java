package com.xixi.bi.model.dto.chart;

import lombok.Data;

import java.io.Serializable;


/**
 * 创建请求
 *
 *
 */
@Data
public class GenChartByAiRequest implements Serializable {

    /**
     *  名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图标类型
     */
    private String chartType;



    private static final long serialVersionUID = 1L;
}