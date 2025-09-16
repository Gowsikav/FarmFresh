package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.config.EmailConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSenderImpl implements EmailSender{

    @Autowired
    private EmailConfiguration configuration;

    public EmailSenderImpl()
    {
        System.out.println("EmailSenderImpl constructor");
    }

    @Override
    public boolean mailSend(String email) {
        System.out.println("mailSend method");
        try {
            String resetLink = "http://localhost:8081/farm-fresh/redirectToSetPassword?email=" + email;

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Password Reset Request");

            String messageBody = "Dear User,\n\n"
                    + "We received a request to reset your password. Please click the link below to reset it:\n\n"
                    + resetLink + "\n\n"
                    + "If you did not request this, please ignore this email.\n\n"
                    + "Regards,\nFarm Fresh Team";

            simpleMailMessage.setText(messageBody);

            configuration.mailSender().send(simpleMailMessage);
            log.info("Password reset mail sent to: {}" , email);
            return true;
        } catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
            return false;
        }
    }

}
