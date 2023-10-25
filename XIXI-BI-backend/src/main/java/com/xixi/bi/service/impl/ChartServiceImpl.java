package com.xixi.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xixi.bi.common.ErrorCode;
import com.xixi.bi.exception.BusinessException;
import com.xixi.bi.model.entity.Chart;
import com.xixi.bi.mapper.ChartMapper;
import com.xixi.bi.service.ChartService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author 86159
 * @description 针对表【chart(图表信息表)】的数据库操作Service实现
 * @createDate 2023-09-13 01:03:34
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {
    @Resource
    ChartMapper chartMapper;

    /**
     * 使用excel的列名作为数据库表的列名
     * @param userId
     * @param CSVData
     * @param chartId
     */
    /*
    @Override
    public void saveChartDataByUserId(String userId, String CSVData, String chartId) {
        String[] lines = CSVData.split("\n");
        // 获取表头
        String[] header = lines[0].split(",");
        String tableName = "chart_" + chartId;
        StringBuilder createSql = new StringBuilder("create table if not exists " + tableName + "\n(\n");
        for (int i = 0; i < header.length; i++) {
            if (i != header.length - 1) {
                createSql.append(header[i] + " varchar(50),\n");
            } else {
                createSql.append(header[i] + " varchar(50)\n)");
            }
        }
        System.out.println(createSql);
        try {
            chartMapper.createChartDataByUserId(createSql.toString());
        } catch (Exception e) {
            log.error("创建图表数据表失败", e);
            throw new BusinessException(ErrorCode.CRATETABLE_ERROR);
        }

        // 获取数据
        StringBuilder insertSql = new StringBuilder("insert into " + tableName + "\nvalues\n");
        List<String> chartDataList = new ArrayList<>(Arrays.asList(lines));
        // 删除列名
        chartDataList.remove(0);
        for (int i = 0; i < chartDataList.size(); i++) {
            String[] lineData = chartDataList.get(i).split(",");
            insertSql.append("(");
            for (int j = 0; j < lineData.length; j++) {
                if (j != lineData.length - 1) {
                    insertSql.append("'" + lineData[j] + "',");
                } else {
                    insertSql.append("'" + lineData[j]  + "'");
                }
            }
            if (i != chartDataList.size() - 1) {
                insertSql.append("),\n");
            } else {
                insertSql.append(");");
            }
        }
        try {
            chartMapper.insertChartDataByUserId(insertSql.toString());
        } catch (Exception e) {
            log.error("创建图表数据表失败", e);
            throw new BusinessException(ErrorCode.CRATETABLE_ERROR);
        }
        System.out.println(insertSql);

    }
    */


    /**
     * 直接将csv数据存在一个字段中
     *
     * @param userId
     * @param CSVData
     * @param chartId
     */
    @Override
    public void saveChartDataByUserId(String userId, String CSVData, String chartId) {
        String[] lines = CSVData.split("\n");
        // 获取表头
        String[] header = lines[0].split(",");
        String tableName = "chart_" + chartId;
        String createSql = "create table if not exists " + tableName + "\n(\nchartdata mediumtext\n);";
        System.out.println(createSql);
        try {
            chartMapper.createChartDataByUserId(createSql);
        } catch (Exception e) {
            log.error("创建图表数据表失败", e);
            throw new BusinessException(ErrorCode.CRATETABLE_ERROR);
        }

        // 获取数据
        StringBuilder insertSql = new StringBuilder("insert into " + tableName + "\nvalues\n(\"");
        insertSql.append(CSVData + "\");");
        try {
            chartMapper.insertChartDataByUserId(insertSql.toString());
        } catch (Exception e) {
            log.error("创建图表数据表失败", e);
            throw new BusinessException(ErrorCode.CRATETABLE_ERROR);
        }
        System.out.println(insertSql);

    }

    @Override
    public String getChartCsvData(String tableName) {
        String chartCsvData = chartMapper.getChartCsvData(tableName);
        return chartCsvData;
    }
}




