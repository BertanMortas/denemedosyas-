package com.hrms.rabbitmq.producer;

import com.hrms.rabbitmq.model.AddEmployeeMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailProducer {
    @Value("${rabbitmq.user-exchange}")
    private String userExchange;
    @Value("${rabbitmq.user-mailBindingKey}")
    private String userMailBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendEmployeeInfo(AddEmployeeMailModel model) {
        rabbitTemplate.convertAndSend(userExchange,userMailBindingKey,model);
    }


}
