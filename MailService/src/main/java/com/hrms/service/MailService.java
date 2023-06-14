package com.hrms.service;

import com.hrms.dto.request.ForgotPasswordRequestDto;
import com.hrms.manager.IAuthManager;
import com.hrms.rabbitmq.model.AddEmployeeMailModel;
import com.hrms.rabbitmq.model.ForgotPasswordMailModel;
import com.hrms.rabbitmq.model.RegisterMailModel;
import com.hrms.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final IAuthManager authManager;

    public void createMail(RegisterMailModel registerMailModel) {
        String token = jwtTokenProvider.createTokenForActivateLink(registerMailModel.getAuthId(), registerMailModel.getActivationCode())
                .orElseThrow(() -> {
                    throw new RuntimeException("Token üretilemedi");
                });
        System.out.println("Hello2" + token);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Hesap aktivasyon linki");
        simpleMailMessage.setFrom("${spring.mail.username}");
        simpleMailMessage.setTo(registerMailModel.getEmail());
        simpleMailMessage.setText("Sayın " + registerMailModel.getName() + ", hesabınızı aktif etmek için aşağıdaki linke tıklayınız. \n"
                + "http://localhost:8060/api/v1/auth/activate-status/" + token); //TODO Url'in uzantısı tamamlanmadı. UNUTMA!!
        System.out.println("Hello3");
        javaMailSender.send(simpleMailMessage);
        System.out.println("Hello4");
    }
    
    public Boolean sendMailForgetPassword(ForgotPasswordMailModel forgotPasswordMailModel) {
        try{
            Optional<String> optionalToken = jwtTokenProvider.createToken(forgotPasswordMailModel.getAuthId());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("${spring.mail.username}");
            mailMessage.setTo(forgotPasswordMailModel.getEmail());
            mailMessage.setSubject("PASSWORD RESET EMAIL");
            mailMessage.setText("Please click this link and set your new password.\n\n\n"+"http://localhost:8060/api/v1/auth/change-forgot-password");
            javaMailSender.send(mailMessage);
            ForgotPasswordRequestDto dto = ForgotPasswordRequestDto.builder()
                    .token(optionalToken.get())
                    .build();
            authManager.forgotPassword(dto);
        }catch (Exception e){
            e.getMessage();
        }
        return true;
    }

    public Boolean sendAddEmployeeInfo(AddEmployeeMailModel addEmployeeMailModel) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("${spring.mail.username}");
            mailMessage.setTo(addEmployeeMailModel.getEmail());
            mailMessage.setSubject("PASSWORD FOR HMRS");
            mailMessage.setText("Hey " + addEmployeeMailModel.getName() + " " + addEmployeeMailModel.getSurname() + ";" +
                    "\nWelcome our family. Here is your password for HRMS.\n\n"+"Password: " + addEmployeeMailModel.getPassword());
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            e.getMessage();
        }
        return true;
    }
    // selam
    // selam2
}
