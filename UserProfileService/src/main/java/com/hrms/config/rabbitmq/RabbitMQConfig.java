package com.hrms.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.user-exchange}")
    private String userExchange;
    @Value("${rabbitmq.user-mailQueue}")
    private String userMailQueue;
    @Value("${rabbitmq.user-mailBindingKey}")
    private String userMailBindingKey;

    @Bean
    Queue mailQueue() {
        return new Queue(userMailQueue);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(userExchange);
    }

    @Bean
    public Binding bindingAddEmployeeMail(final Queue userMailQueue, final DirectExchange directExchange) {
        return BindingBuilder.bind(userMailQueue).to(directExchange).with(userMailBindingKey);
    }


}
