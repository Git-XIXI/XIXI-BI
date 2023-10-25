package com.xixi.bi.mapper;

import com.xixi.bi.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 86159
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2023-09-13 01:03:34
* @Entity com.xixi.bi.model.entity.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {
    void createChartDataByUserId(@Param("createSql") String createSql);
    void insertChartDataByUserId(@Param("insertSql") String insertSql);

    String getChartCsvData(@Param("tableName") String tableName);
}




