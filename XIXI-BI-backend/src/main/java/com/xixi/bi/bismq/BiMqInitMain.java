package com.xixi.bi.bismq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 用于创建测试程序用到的交换机和队列
 *
 */
public class BiMqInitMain {

    public static void main(String[] args) {

        try {
            // 创建链接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            // 建立链接、创建频道
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHAANGE_NAME = BiMqConstant.BI_EXCHANGE_NAME;
            channel.exchangeDeclare(EXCHAANGE_NAME, "direct");

            String QUEUE_NAME = BiMqConstant.BI_QUEUE_NAME;
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(BiMqConstant.BI_QUEUE_NAME, BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
