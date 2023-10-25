package com.xixi.bi.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel工具类
 */
@Slf4j
public class ExcelUtils {

    /**
     * Excel 转 CSV
     *
     * @param multipartFile
     * @return
     */
    public static String excelToCsv(MultipartFile multipartFile) {
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:testExcel.xlsx");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        // 读取数据
        List<Map<Integer, String>> list = null;
        try {
            //list = EasyExcel.read("D:\\桌面\\小汐 AIGC BI 系统\\XIXI-BI-backend\\src\\main\\resources\\testExcel.xlsx")
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (Exception e) {
            log.error("表格处理异常", e);
        }
        if(CollectionUtil.isEmpty(list)) {
            return "";
        }

        // 转化为 csv
        StringBuilder stringBuilder = new StringBuilder();
        // 读取表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap) list.get(0);
        List<String> headerList = headerMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(headerList,",")).append("\n");

        // 读取数据
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap) list.get(i);
            List<String> dataList = dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(dataList,",  ")).append("\n");
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(excelToCsv(null));
    }
}
