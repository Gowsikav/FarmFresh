package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.config.EmailConfiguration;
import com.xworkz.farmfresh.dto.PaymentDetailsDTO;
import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.entity.PaymentDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierBankDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierEntity;
import com.xworkz.farmfresh.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class EmailSenderImpl implements EmailSender{

    @Autowired
    private EmailConfiguration configuration;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SpringTemplateEngine emailTemplateEngine;

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
    @Async
    public void mailForSupplierRegisterSuccess(String email, String supplierName, String qrCodePath) {
        log.info("mailForSupplierRegisterSuccess method");

        try {
            String subject = "Welcome to Farm Fresh - Registration Successful";

            String messageBody = "<p>Dear <b>" + supplierName + "</b>,</p>"
                    + "<p>We are happy to inform you that your registration as a Milk Supplier with <b>Farm Fresh</b> has been successfully completed.</p>"
                    + "<p>You are now officially part of our trusted network of suppliers. "
                    + "Our team is committed to supporting you in delivering high-quality milk efficiently.</p>"
                    + "<p><b>Your QR Code:</b> This QR code will be used for quick identification during milk collection.</p>"
                    + "<p><img src='cid:qrCodeImage' alt='QR Code' style='width:150px;height:150px;'/></p>"
                    + "<p>If you have any questions, reach out to us at <a href='mailto:info@farmfresh.com'>info@farmfresh.com</a>.</p>"
                    + "<p>Warm regards,<br/>Farm Fresh Team</p>";

            MimeMessage mimeMessage = configuration.mailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(messageBody, true);

            FileSystemResource qrImage = new FileSystemResource(new File(qrCodePath));
            helper.addInline("qrCodeImage", qrImage);

            configuration.mailSender().send(mimeMessage);
            log.info("Registration email with QR sent to: {}", email);

        } catch (Exception e) {
            log.error("Error while sending registration success email: {}", e.getMessage(), e);
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
    @Async
    public void mailForSupplierBankDetails(String email, SupplierBankDetailsEntity bankDetails) {
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
        } catch (Exception e) {
            log.error("Error while sending bank details email: {}", e.getMessage());
        }
    }

    @Override
    @Async
    public void mailForSupplierPayment(SupplierEntity supplier, PaymentDetailsEntity paymentDetails) {
        log.info("mailForSupplierPayment method in email sender");

        try {
            String subject = "Farm Fresh - Milk Supply Payment Confirmation";

            Context context = new Context();
            context.setVariable("firstName", supplier.getFirstName());
            context.setVariable("lastName", supplier.getLastName());
            context.setVariable("paymentDate", paymentDetails.getPaymentDate());
            context.setVariable("amount", paymentDetails.getTotalAmount());
            context.setVariable("periodStart", paymentDetails.getPeriodStart());
            context.setVariable("periodEnd", paymentDetails.getPeriodEnd());

            String htmlContent = emailTemplateEngine.process("supplier-payment-confirmation", context);

            MimeMessage message = configuration.mailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(supplier.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            configuration.mailSender().send(message);

            log.info("HTML payment confirmation email sent successfully");

        } catch (Exception e) {
            log.error("Error sending payment email: {}", e.getMessage());
        }
    }

    @Override
    @Async
    public void mailForBankDetailsRequest(SupplierEntity supplier) {
        log.info("mailForBankDetailsRequest method in EmailSender");

        try {
            String subject = "Farm Fresh - Action Required: Update Your Bank Details";

            String messageBody = "Dear " + supplier.getFirstName()+" "+supplier.getLastName() + ",\n\n"
                    + "We hope you're doing well!\n\n"
                    + "Our records show that your bank details are not yet provided in your Farm Fresh account.\n"
                    + "Please update your bank details as soon as possible to ensure smooth and timely payments.\n\n"
                    + "⚠️ Note: Payments will not be processed until your bank details are submitted and verified.\n\n"
                    + "To update your bank details, please log in to your Farm Fresh Supplier Dashboard.\n\n"
                    + "Thank you for your cooperation.\n\n"
                    + "Best regards,\n"
                    + "Farm Fresh Team";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(supplier.getEmail());
            message.setSubject(subject);
            message.setText(messageBody);

            configuration.mailSender().send(message);

            log.info("Bank details reminder email sent successfully to {}", supplier.getEmail());

        } catch (Exception e) {
            log.error("Error while sending bank details reminder email to {}", supplier.getEmail(), e);
        }
    }

    @Override
    @Async
    public void mailForAdminPaymentSummary(List<PaymentDetailsDTO> payments) {
        log.info("mailForAdminPaymentSummary method in EmailSender");

        try {
            LocalDate today=LocalDate.now();
            StringBuilder html = new StringBuilder();
            html.append("<h2>📅 Payment Summary for ").append(today).append("</h2>");
            html.append("<table border='1' cellspacing='0' cellpadding='6' style='border-collapse:collapse;width:100%;'>")
                    .append("<tr style='background:#f2f2f2;'>")
                    .append("<th>Supplier</th>")
                    .append("<th>Amount Paid (₹)</th>")
                    .append("<th>Status</th>")
                    .append("<th>Period</th>")
                    .append("</tr>");

            for (PaymentDetailsDTO p : payments) {
                String color = "Paid".equalsIgnoreCase(p.getPaymentStatus()) ? "green" : "red";
                html.append("<tr>")
                        .append("<td>").append(p.getSupplier().getFirstName()).append(" ").append(p.getSupplier().getLastName()).append("</td>")
                        .append("<td>").append(p.getTotalAmount()).append("</td>")
                        .append("<td style='color:").append(color).append(";'>").append(p.getPaymentStatus()).append("</td>")
                        .append("<td>").append(p.getPeriodStart()).append(" to ").append(p.getPeriodEnd()).append("</td>")
                        .append("</tr>");
            }
            html.append("</table>");

            List<AdminEntity> admins = adminRepository.findAll();

            for (AdminEntity admin : admins) {
                if (admin.getEmail() != null && !admin.getEmail().isEmpty()) {
                    MimeMessage message = configuration.mailSender().createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(admin.getEmail());
                    helper.setSubject("Evening Payment Summary - " + today);
                    helper.setText(html.toString(), true);
                    configuration.mailSender().send(message);
                    log.info("Payment summary email sent to admin: {}", admin.getEmail());
                }
            }
        } catch (Exception e) {
            log.error("Error while sending admin payment summary email: {}", e.getMessage(), e);
        }
    }
}
