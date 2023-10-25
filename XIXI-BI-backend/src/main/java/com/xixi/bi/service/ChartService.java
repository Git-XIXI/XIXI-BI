package com.xixi.bi.service;

import com.xixi.bi.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;

/**
* @author 86159
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2023-09-13 01:03:34
*/
public interface ChartService extends IService<Chart> {
    /**
     * 单独保存图表数据
     * @param userId
     * @param CSVData
     * @param chartId
     */
    public void saveChartDataByUserId(String userId,String CSVData, String chartId);

    /**
     * 根据表名获取图表数据
     *
     * @param tableName
     * @return
     */
    public String getChartCsvData(String tableName);
}
