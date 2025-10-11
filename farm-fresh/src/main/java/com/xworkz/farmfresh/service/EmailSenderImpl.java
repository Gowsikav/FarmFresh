package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.config.EmailConfiguration;
import com.xworkz.farmfresh.entity.SupplierBankDetailsEntity;
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
        log.info("EmailSenderImpl constructor");
    }

    @Override
    public boolean mailSend(String email) {
        log.info("mailSend method");
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

    @Override
    public boolean mailForSupplierRegisterSuccess(String email, String supplierName) {
        log.info("mailForSupplierRegisterSuccess method");
        try {
            String subject = "Welcome to Farm Fresh - Registration Successful";

            String messageBody = "Dear " + supplierName + ",\n\n"
                    + "We are thrilled to inform you that your registration as a Milk Supplier with Farm Fresh has been successfully completed.\n\n"
                    + "You are now officially part of our trusted network of suppliers. "
                    + "Our team is committed to supporting you in delivering high-quality milk to our customers efficiently.\n\n"
                    + "If you have any questions or need assistance, please feel free to reach out to us at info@farmfresh.com or call our support line.\n\n"
                    + "Once again, welcome aboard! We look forward to a fruitful and long-lasting partnership.\n\n"
                    + "Warm regards,\n"
                    + "Farm Fresh Team";

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(messageBody);

            configuration.mailSender().send(simpleMailMessage);
            log.info("Registration success mail sent to: {}", email);
            return true;
        } catch (Exception e) {
            log.error("Error while sending registration success email: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean mailForSupplierLoginOtp(String email, String otp) {
        log.info("mailForSupplierLoginOtp method in email sender");
        try {
            String subject = "Farm Fresh - Your OTP for Login";

            String messageBody = "Dear Supplier,\n\n"
                    + "We have received a request to log in to your Farm Fresh account.\n\n"
                    + "Your One-Time Password (OTP) is: " + otp + "\n\n"
                    + "Please use this OTP to complete your login. This code is valid for the next 5 minutes.\n\n"
                    + "If you did not request this OTP, please ignore this email or contact our support team immediately at info@farmfresh.com.\n\n"
                    + "Thank you for being a valued member of the Farm Fresh supplier community.\n\n"
                    + "Warm regards,\n"
                    + "Farm Fresh Team";

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(messageBody);

            configuration.mailSender().send(simpleMailMessage);
            log.info("OTP mail sent successfully to: {} and OTP: {}", email,otp);
            return true;
        } catch (Exception e) {
            log.error("Error while sending OTP email: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean mailForSupplierBankDetails(String email, SupplierBankDetailsEntity bankDetails) {
        log.info("mailForSupplierBankDetails method in email sender");
        try {
            String subject = "Farm Fresh - Bank Details Updated Successfully";

            String messageBody = "Dear Supplier,\n\n"
                    + "Your bank details have been successfully added/updated in your Farm Fresh account.\n\n"
                    + "Below are the details we have on record:\n"
                    + "---------------------------------------\n"
                    + "Bank Name: " + bankDetails.getBankName() + "\n"
                    + "Account Number: " + bankDetails.getAccountNumber() + "\n"
                    + "IFSC Code: " + bankDetails.getIFSCCode() + "\n"
                    + "Branch Name: " + bankDetails.getBankBranch() + "\n"
                    + "---------------------------------------\n\n"
                    + "If you did not make this change, please contact our support team immediately at info@farmfresh.com.\n\n"
                    + "Thank you for keeping your account information up to date.\n\n"
                    + "Warm regards,\n"
                    + "Farm Fresh Team";

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(messageBody);

            configuration.mailSender().send(simpleMailMessage);
            log.info("Bank details update mail sent successfully to: {}", email);
            return true;
        } catch (Exception e) {
            log.error("Error while sending bank details email: {}", e.getMessage());
            return false;
        }
    }

}
