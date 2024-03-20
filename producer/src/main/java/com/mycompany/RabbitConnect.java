package com.mycompany;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


public class RabbitConnect {

    private final static String QUEUE_NAME = "my_queue";

    public static String getQueueName() {
        return QUEUE_NAME;
    }

    public static Connection createConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        factory.setHost("rabbitmq-container"); 
        return factory.newConnection();
    }

    public static Channel createChannel(Connection connection) throws Exception {
        return connection.createChannel();
    }

    public static void declareQueue(Channel channel) throws Exception {
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    }
    
}
