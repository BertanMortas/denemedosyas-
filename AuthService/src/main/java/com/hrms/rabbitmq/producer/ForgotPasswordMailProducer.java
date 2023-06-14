package com.hrms.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hrms.rabbitmq.model.ForgotPasswordMailModel;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class ForgotPasswordMailProducer {

    @Value("${rabbitmq.exchange}")
    String exchange;
    @Value("${rabbitmq.forgotPasswordBindingKey}")
    String forgotPasswordBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendForgotPasswordMail(ForgotPasswordMailModel forgotPasswordMailModel){
        rabbitTemplate.convertAndSend(exchange, forgotPasswordBindingKey, forgotPasswordMailModel);
    }

}
