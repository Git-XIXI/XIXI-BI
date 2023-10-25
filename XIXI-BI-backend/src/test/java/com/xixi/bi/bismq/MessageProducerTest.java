package com.xixi.bi.bismq;

import com.rabbitmq.client.Channel;
import com.xixi.bi.common.ErrorCode;
import com.xixi.bi.exception.BusinessException;
import com.xixi.bi.model.entity.Chart;
import com.xixi.bi.model.enums.TaskStatusEnum;
import com.xixi.bi.service.ChartService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class MessageProducerTest {
    @Resource
    public BiMessageProducer messageProducer;
    @Resource
    public ChartService chartService;

    @Test
    void sendMessage() {
        //messageProducer.sendMessage("hello,world","test_routingkey","test_exchange");
    }
//    @Test
//    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
//    public void receriveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
//        if(StringUtils.isNotBlank(message)){
//            System.out.println(message);
//        }
//
//        // 消息确认
//        try {
//            channel.basicAck(deliveryTag, false);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}