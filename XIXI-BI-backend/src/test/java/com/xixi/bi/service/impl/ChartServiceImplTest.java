package com.xixi.bi.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChartServiceImplTest {
    @Resource
    ChartServiceImpl chartService;

    @Test
    void saveChartDataByUserId() {
        chartService.saveChartDataByUserId("1","日期,用户数\n" +
                "1号,10\n" +
                "2号,4\n" +
                "3号,6\n" ,"909444627");
    }

    @Test
    void getChartCsvData() {
        String chart = chartService.getChartCsvData("chart_test");
        System.out.println(chart);;
    }
}