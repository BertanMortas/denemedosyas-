package com.hrms.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.mailQueue}")
    String mailQueue;
    @Value("${rabbitmq.forgotPasswordQueue}")
    private String forgotPasswordQueue;

    @Value("${rabbitmq.user-mailQueue}")
    private String userMailQueue;
    @Bean
    Queue mailQueue(){return new Queue(mailQueue);}

    @Bean
    Queue forgotPasswordQueue(){
        return new Queue(forgotPasswordQueue);
    }

    @Bean
    Queue userMailQueue(){
        return new Queue(userMailQueue);
    }
}
