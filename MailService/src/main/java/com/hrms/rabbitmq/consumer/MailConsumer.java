package com.hrms.rabbitmq.consumer;

import com.hrms.rabbitmq.model.AddEmployeeMailModel;
import com.hrms.rabbitmq.model.ForgotPasswordMailModel;
import com.hrms.rabbitmq.model.RegisterMailModel;
import com.hrms.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailConsumer {
    private final MailService mailService;

    @RabbitListener(queues = ("${rabbitmq.mailQueue}"))
    public void sendActivationLink(RegisterMailModel model) {mailService.createMail(model);}

    @RabbitListener(queues = ("${rabbitmq.forgotPasswordQueue}"))
    public void sendForgotPasswordMail(ForgotPasswordMailModel forgotPasswordMailModel){
        mailService.sendMailForgetPassword(forgotPasswordMailModel);
    }

    @RabbitListener(queues = ("${rabbitmq.user-mailQueue}"))
    public void sendAddEmployeeInfo(AddEmployeeMailModel addEmployeeMailModel){
        mailService.sendAddEmployeeInfo(addEmployeeMailModel);
    }
}
