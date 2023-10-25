package com.xixi.bi.bismq;

import com.rabbitmq.client.Channel;
import com.xixi.bi.common.ErrorCode;
import com.xixi.bi.exception.BusinessException;
import com.xixi.bi.manager.AiManager;
import com.xixi.bi.model.entity.Chart;
import com.xixi.bi.model.enums.TaskStatusEnum;
import com.xixi.bi.service.ChartService;
import com.xixi.bi.service.impl.ChartServiceImpl;
import com.xixi.bi.utils.ExcelUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

@Component
@Slf4j
public class BiMessageConsumer {
    @Resource
    private ChartService chartService;
    @Resource
    private AiManager aiManager;

    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receriveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图表为空");
        }
        // 先修改图标任务状态为“执行中”，等待执行成功后，修改为“已完成",并保存执行结果，执行失败后，状态修改为”失败“
        Chart updateChart = new Chart();
        updateChart.setId(chartId);
        updateChart.setStatus(TaskStatusEnum.RUNNING.getValue());
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chartId, "更新图表状态为执行中失败");
            return;
        }
        //压缩后的数据
        String csvData = chartService.getChartCsvData("chart_" + chartId);
        // 调用 AI
        String result = aiManager.doChat(bulidUserInput(csvData,chart));
        String[] splits = result.split("!!!");
        if (splits.length < 3) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chartId, "AI 生成错误");
            return;
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        Chart updateChartRsult = new Chart();
        updateChartRsult.setId(chartId);
        updateChartRsult.setGenChart(genChart);
        updateChartRsult.setGenResult(genResult);
        updateChartRsult.setStatus(TaskStatusEnum.SUCCESS.getValue());
        boolean updateResult = chartService.updateById(updateChartRsult);
        if (!updateResult) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chartId, "更新图表为成功状态和更新AI生成数据失败");
        }
        // 消息确认
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 统一处理异常
     *
     * @param chartId
     * @param execMessage
     */
    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus(TaskStatusEnum.FAILED.getValue());
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败" + chartId + "," + execMessage);
        }
    }

    private String bulidUserInput(String csvData, Chart chart) {
        //合理的将数据进行可视化
        final String prompt = "你是一个数据分析师和前端开发专家，接下里我会按照一下固定格式给你提供内容：\n" +
                "分析需求：\n" +
                "{数据分析的需求或者目标} \n" +
                "原始数据：\n" +
                "{csv格式的原始数据，用，作为分隔符} \n" +
                "请根据这两部分内容，按照以下指定生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
                "!!! \n" +
                "{合理对数据进行分析，并生成前端 Echart V5 的 option 配置对象JSON代码，且代码要包含title字段，title字段中只包含text字段，不要生成任何多余的内容，比注如注释} \n" +
                "!!! \n" +
                "明确地数据分析结论、越详细越好，不要生成多余地注释。\n";
        //用户数据
        StringBuilder userInput = new StringBuilder();
        userInput.append(prompt).append("\n");
        userInput.append("分析需求：").append("\n");
        //分析目标
        String userGoal = chart.getGoal();
        String chartType = chart.getChartType();
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += ",请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }
}
