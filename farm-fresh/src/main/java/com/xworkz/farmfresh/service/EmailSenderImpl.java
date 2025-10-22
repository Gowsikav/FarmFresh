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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class EmailSenderImpl implements EmailSender{

    @Autowired
    private EmailConfiguration configuration;

    @Autowired
    private AdminRepository adminRepository;

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

    @Override
    public boolean mailForSupplierPayment(SupplierEntity supplier, PaymentDetailsEntity paymentDetails) {
        log.info("mailForSupplierPayment method in email sender");
        try {
            String subject = "Farm Fresh - Milk Supply Payment Confirmation";

            String messageBody = "Dear " + supplier.getFirstName()+" "+supplier.getLastName() + ",\n\n"
                    + "We are pleased to inform you that the payment for your milk supply has been successfully processed.\n\n"
                    + "Below are the payment details:\n"
                    + "---------------------------------------\n"
                    + "Payment Date: " + paymentDetails.getPaymentDate() + "\n"
                    + "Amount Paid: ₹" + paymentDetails.getTotalAmount() + "\n"
                    + "Supply Period: " + paymentDetails.getPeriodStart() + " to " + paymentDetails.getPeriodEnd() + "\n"
                    + "---------------------------------------\n\n"
                    + "This payment covers the total amount for the milk supplied during the above period.\n\n"
                    + "If you have any questions or concerns regarding this payment, please contact our accounts team at info@farmfresh.com.\n\n"
                    + "Thank you for your consistent and quality milk supply.\n\n"
                    + "Warm regards,\n"
                    + "Farm Fresh Team";

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(supplier.getEmail());
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(messageBody);

            configuration.mailSender().send(simpleMailMessage);
            log.info("Payment confirmation mail sent successfully to: {}", supplier.getEmail());
            return true;
        } catch (Exception e) {
            log.error("Error while sending payment confirmation email: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean mailForBankDetailsRequest(SupplierEntity supplier) {
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
            return true;

        } catch (Exception e) {
            log.error("Error while sending bank details reminder email to {}", supplier.getEmail(), e);
            return false;
        }
    }

    @Override
    public boolean mailForAdminPaymentSummary(List<PaymentDetailsDTO> payments) {
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
            return true;
        } catch (Exception e) {
            log.error("Error while sending admin payment summary email: {}", e.getMessage(), e);
        }
        return false;
    }
}
