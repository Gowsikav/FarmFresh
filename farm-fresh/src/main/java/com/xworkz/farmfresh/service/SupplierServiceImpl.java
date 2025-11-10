package com.xworkz.farmfresh.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.SupplierBankDetailsDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.entity.*;
import com.xworkz.farmfresh.repository.CollectMilkRepository;
import com.xworkz.farmfresh.repository.NotificationRepository;
import com.xworkz.farmfresh.repository.PaymentDetailsRepository;
import com.xworkz.farmfresh.repository.SupplierRepository;
import com.xworkz.farmfresh.util.OTPUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService{

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CollectMilkRepository collectMilkRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private QrGeneratorService qrGeneratorService;

    public SupplierServiceImpl()
    {
        log.info("SupplierServiceImpl constructor");
    }

    @Override
    public boolean addSupplier(SupplierDTO supplierDTO,String adminEmail) {
        log.info("addSupplier method in SupplierServiceImpl");
        SupplierEntity supplierEntity=new SupplierEntity();
        BeanUtils.copyProperties(supplierDTO,supplierEntity);

        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(adminEmail);
        SupplierAuditEntity supplierAuditEntity=supplierEntity.getSupplierAuditEntity();
        if(supplierAuditEntity==null)
        {
            supplierAuditEntity=new SupplierAuditEntity();
            supplierAuditEntity.setName(supplierEntity.getFirstName()+" "+supplierEntity.getLastName());
            supplierAuditEntity.setCreatedBy(adminDTO.getAdminName());
            supplierAuditEntity.setCreatedAt(LocalDateTime.now());
            supplierAuditEntity.setSupplierEntity(supplierEntity);
        }
        supplierEntity.setSupplierAuditEntity(supplierAuditEntity);
        supplierEntity.setIsActive(true);
        if(supplierRepository.addSupplier(supplierEntity))
        {
            log.info("supplier details saved");
            SupplierEntity supplier=supplierRepository.getSupplierByEmail(supplierEntity.getEmail());
            String qrcode=qrGeneratorService.generateSupplierQR(supplier.getSupplierId(),supplier.getEmail(),supplier.getPhoneNumber());
            if(emailSender.mailForSupplierRegisterSuccess(supplierEntity.getEmail(),supplierEntity.getFirstName()+supplierEntity.getLastName(),qrcode))
            {
                log.info("Mail send to supplier");
                return true;
            }
            log.error("Mail not send");
        }
        log.error("details not saved");
        return false;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers(int pageNumber,int pageSize) {
        log.info("getAllSuppliers method in supplier service");
        List<SupplierEntity> supplierEntities=supplierRepository.getAllSuppliers(pageNumber,pageSize);
        List<SupplierDTO> supplierDTOS=new ArrayList<>();
        supplierEntities.forEach(supplierEntity -> {
            SupplierDTO supplierDTO=new SupplierDTO();
            BeanUtils.copyProperties(supplierEntity,supplierDTO);
            if(supplierEntity.getSupplierBankDetails()!=null)
            {
                SupplierBankDetailsDTO supplierBankDetailsDTO=new SupplierBankDetailsDTO();
                BeanUtils.copyProperties(supplierEntity.getSupplierBankDetails(),supplierBankDetailsDTO);
                supplierDTO.setSupplierBankDetails(supplierBankDetailsDTO);
            }
            supplierDTOS.add(supplierDTO);
        });
        return supplierDTOS;
    }

    @Override
    public boolean checkEmail(String email) {
        log.info("checkEmail method in Supplier service");
        return supplierRepository.checkEmail(email);
    }

    @Override
    public boolean checkPhonNumber(String phoneNumber) {
        log.info("checkPhonNumber method in Supplier service");
        return supplierRepository.checkPhoneNumber(phoneNumber);
    }

    @Override
    public boolean editSupplierDetails(SupplierDTO supplierDTO, String adminEmail) {
        log.info("editSupplierDetails method in supplier service");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(adminEmail);

        SupplierEntity supplierEntity=supplierRepository.getSupplierByEmail(supplierDTO.getEmail());

        SupplierEntity sendEntity=new SupplierEntity();
        BeanUtils.copyProperties(supplierDTO,sendEntity);

        sendEntity.setSupplierAuditEntity(supplierEntity.getSupplierAuditEntity());
        SupplierAuditEntity supplierAuditEntity=sendEntity.getSupplierAuditEntity();
        if(supplierAuditEntity==null)
        {
            log.error("getSupplier not found");
            return false;
        }
        supplierAuditEntity.setUpdatedBy(adminDTO.getAdminName());
        supplierAuditEntity.setUpdatedAt(LocalDateTime.now());
        return supplierRepository.updateSupplierDetails(sendEntity, false);
    }

    @Override
    public boolean deleteSupplierDetails(String email, String adminEmail) {
        log.info("deleteSupplierDetails method in supplier service");
        SupplierEntity supplierEntity = supplierRepository.getSupplierByEmail(email);
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(adminEmail);

        SupplierAuditEntity supplierAuditEntity=supplierEntity.getSupplierAuditEntity();
        if(supplierAuditEntity==null)
        {
            log.error("getSupplier not found");
            return false;
        }
        supplierAuditEntity.setDeletedBy(adminDTO.getAdminName());
        supplierAuditEntity.setDeletedAt(LocalDateTime.now());
        supplierEntity.setSupplierAuditEntity(supplierAuditEntity);
        return supplierRepository.updateSupplierDetails(supplierEntity, true);
    }

    @Override
    public List<SupplierDTO> searchSuppliers(String keyword) {
        List<SupplierEntity> supplierEntities=supplierRepository.getSearchSuppliers(keyword);
        List<SupplierDTO> supplierDTOS=new ArrayList<>();
        supplierEntities.forEach(supplierEntity -> {
            SupplierDTO supplierDTO=new SupplierDTO();
            BeanUtils.copyProperties(supplierEntity,supplierDTO);
            supplierDTOS.add(supplierDTO);
        });
        return supplierDTOS;
    }

    @Override
    public SupplierDTO getSupplierDetails(String phone) {
        log.info("getSupplierDetails method in supplier service");
        SupplierEntity supplierEntity=supplierRepository.getSupplierByPhone(phone);
        SupplierDTO supplierDTO=new SupplierDTO();
        BeanUtils.copyProperties(supplierEntity,supplierDTO);
        return supplierDTO;
    }

    @Override
    public boolean sendOtpTOSupplierForLogin(String email) {
        log.info("sendOtpTOSupplierForLogin method in supplier service");
        String otp= OTPUtil.generateNumericOtp(6);
        if(supplierRepository.setOTPAndTime(email,otp,LocalDateTime.now()))
        {
            return emailSender.mailForSupplierLoginOtp(email, otp);
        }
        return false;
    }

    @Override
    public boolean checkOTPForSupplierLogin(String email, String otp) {
        log.info("checkOTPForSupplierLogin method in supplier service");
        SupplierEntity supplierEntity = supplierRepository.getSupplierByEmail(email);

        if (supplierEntity == null) {
            log.warn("No supplier found with email: {}", email);
            return false;
        }

        String savedOtp = supplierEntity.getLoginOTP();
        LocalDateTime savedTime = supplierEntity.getExpiryTime();

        if (savedOtp == null || !savedOtp.equals(otp)) {
            log.warn("Invalid OTP entered for email: {}", email);
            throw new RuntimeException("Invalid OTP");
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(savedTime, now);
        if (duration.toMinutes() >= 5) {
            log.warn("OTP expired for email: {}", email);
            throw new RuntimeException("Time is expiry.Goto Login");
        }

        log.info("OTP verified successfully for email: {}", email);

        return supplierRepository.setOTPAndTime(email,null,null);
    }

    @Override
    public void setOtpAndTimeNull(String email) {
        log.info("setOtpAndTimeNull method in supplier service");
        supplierRepository.setOTPAndTime(email,null,null);
    }

    @Override
    public SupplierDTO getDetailsByEmail(String email) {
        log.info("getDetailsBy email method in supplier service");
        SupplierEntity supplierEntity= supplierRepository.getSupplierByEmail(email);
        SupplierDTO supplierDTO=new SupplierDTO();
        BeanUtils.copyProperties(supplierEntity,supplierDTO);
        if(supplierEntity.getSupplierBankDetails()!=null)
        {
            SupplierBankDetailsDTO supplierBankDetailsDTO=new SupplierBankDetailsDTO();
            BeanUtils.copyProperties(supplierEntity.getSupplierBankDetails(),supplierBankDetailsDTO);
            supplierDTO.setSupplierBankDetails(supplierBankDetailsDTO);
        }
        return supplierDTO;
    }

    @Override
    public boolean updateSupplierDetailsBySupplier(SupplierDTO supplierDTO) {
        log.info("updateSupplierDetailsBySupplier method in supplier service");
        SupplierEntity existingEntity=supplierRepository.getSupplierByEmail(supplierDTO.getEmail());
        SupplierAuditEntity supplierAuditEntity;
        if(existingEntity==null)
        {
            log.error("Entity not found for update");
            return false;
        }
        supplierAuditEntity=existingEntity.getSupplierAuditEntity();
        if(supplierAuditEntity==null)
        {
            log.error("log not found");
            return false;
        }
        existingEntity.setFirstName(supplierDTO.getFirstName());
        existingEntity.setLastName(supplierDTO.getLastName());
        existingEntity.setAddress(supplierDTO.getAddress());
        if(supplierDTO.getProfilePath()!=null)
        {
            existingEntity.setProfilePath(supplierDTO.getProfilePath());
        }
        supplierAuditEntity.setUpdatedAt(LocalDateTime.now());
        supplierAuditEntity.setUpdatedBy(supplierDTO.getFirstName()+" "+supplierDTO.getLastName());
        existingEntity.setSupplierAuditEntity(supplierAuditEntity);
        supplierAuditEntity.setSupplierEntity(existingEntity);

        return supplierRepository.updateSupplierDetailsBySupplier(existingEntity);
    }

    @Override
    public boolean updateSupplierBankDetails(SupplierBankDetailsDTO supplierBankDetailsDTO, String email) {
        log.info("updateSupplierBankDetails method in supplier service");
        SupplierEntity supplierEntity=supplierRepository.getSupplierByEmail(email);
        if(supplierEntity.getSupplierAuditEntity()==null)
        {
            log.error("Entity not found for supplier");
            return false;
        }
        SupplierAuditEntity supplierAuditEntity=supplierEntity.getSupplierAuditEntity();
        supplierAuditEntity.setUpdatedBy(supplierEntity.getFirstName()+" "+supplierEntity.getLastName());
        supplierAuditEntity.setUpdatedAt(LocalDateTime.now());

        supplierEntity.setSupplierAuditEntity(supplierAuditEntity);
        supplierAuditEntity.setSupplierEntity(supplierEntity);

        SupplierBankDetailsEntity supplierBankDetailsEntity = supplierEntity.getSupplierBankDetails();
        SupplierBankDetailsAuditEntity supplierBankDetailsAuditEntity;

        if (supplierBankDetailsEntity == null) {
            supplierBankDetailsEntity = new SupplierBankDetailsEntity();
            BeanUtils.copyProperties(supplierBankDetailsDTO, supplierBankDetailsEntity);

            supplierBankDetailsAuditEntity = new SupplierBankDetailsAuditEntity();
            supplierBankDetailsAuditEntity.setCreatedAt(LocalDateTime.now());
            supplierBankDetailsAuditEntity.setCreatedBy(supplierEntity.getFirstName() + " " + supplierEntity.getLastName());
        } else {
            BeanUtils.copyProperties(supplierBankDetailsDTO, supplierBankDetailsEntity);
            supplierBankDetailsAuditEntity = supplierBankDetailsEntity.getSupplierBankDetailsAuditEntity();
        }
        supplierBankDetailsAuditEntity.setUpdatedBy(supplierEntity.getFirstName()+" "+supplierEntity.getLastName());
        supplierBankDetailsAuditEntity.setUpdatedAt(LocalDateTime.now());

        supplierEntity.setSupplierBankDetails(supplierBankDetailsEntity);
        supplierBankDetailsEntity.setSupplierEntity(supplierEntity);
        supplierBankDetailsEntity.setSupplierBankDetailsAuditEntity(supplierBankDetailsAuditEntity);
        supplierBankDetailsAuditEntity.setSupplierBankDetailsEntity(supplierBankDetailsEntity);

        if(supplierRepository.updateSupplierDetailsBySupplier(supplierEntity))
        {
            return emailSender.mailForSupplierBankDetails(supplierEntity.getEmail(), supplierBankDetailsEntity);
        }
        return false;
    }

    @Override
    public boolean updateSupplierBankDetailsByAdmin(SupplierBankDetailsDTO supplierBankDetailsDTO, String email,String adminEmail) {
        log.info("updateSupplierBankDetailsByAdmin method in supplier service");
        SupplierEntity supplierEntity=supplierRepository.getSupplierByEmail(email);
        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(adminEmail);
        if(supplierEntity.getSupplierAuditEntity()==null)
        {
            log.error("Entity not found for supplier");
            return false;
        }
        SupplierAuditEntity supplierAuditEntity=supplierEntity.getSupplierAuditEntity();
        supplierAuditEntity.setUpdatedBy(adminDTO.getAdminName());
        supplierAuditEntity.setUpdatedAt(LocalDateTime.now());

        supplierEntity.setSupplierAuditEntity(supplierAuditEntity);
        supplierAuditEntity.setSupplierEntity(supplierEntity);

        SupplierBankDetailsEntity supplierBankDetailsEntity = supplierEntity.getSupplierBankDetails();
        SupplierBankDetailsAuditEntity supplierBankDetailsAuditEntity;

        BeanUtils.copyProperties(supplierBankDetailsDTO, supplierBankDetailsEntity);
        supplierBankDetailsAuditEntity = supplierBankDetailsEntity.getSupplierBankDetailsAuditEntity();

        supplierBankDetailsAuditEntity.setUpdatedBy(adminDTO.getAdminName());
        supplierBankDetailsAuditEntity.setUpdatedAt(LocalDateTime.now());

        supplierEntity.setSupplierBankDetails(supplierBankDetailsEntity);
        supplierBankDetailsEntity.setSupplierEntity(supplierEntity);
        supplierBankDetailsEntity.setSupplierBankDetailsAuditEntity(supplierBankDetailsAuditEntity);
        supplierBankDetailsAuditEntity.setSupplierBankDetailsEntity(supplierBankDetailsEntity);

        if(supplierRepository.updateSupplierDetailsBySupplier(supplierEntity))
        {
            return emailSender.mailForSupplierBankDetails(supplierEntity.getEmail(), supplierBankDetailsEntity);
        }
        return false;
    }

    @Override
    public SupplierDTO getSupplierDetailsByNotificationId(Long notificationId) {
        log.info("getSupplierDetailsByNotificationId method in supplier service");
        SupplierEntity supplierEntity=notificationRepository.getSupplierEntityByNotificationId(notificationId);
        return getDetailsByEmail(supplierEntity.getEmail());
    }

    @Override
    public boolean requestForSupplierBankDetails(String supplierEmail) {
        log.info("requestForSupplierBankDetails method in supplier service");
        SupplierEntity supplierEntity=supplierRepository.getSupplierByEmail(supplierEmail);
        return emailSender.mailForBankDetailsRequest(supplierEntity);
    }

    @Override
    public void downloadInvoicePdf(Integer supplierId,Integer paymentId, LocalDate start, LocalDate end, LocalDate paymentDate, HttpServletResponse response) {
        log.info("Generating enhanced supplier invoice for supplierId {}", supplierId);

        try {
            PaymentDetailsEntity payment = paymentDetailsRepository.getPaymentDetailsById(paymentId);
            List<CollectMilkEntity> milkList = collectMilkRepository.getCollectMilkDetailsForSupplierById(supplierId, start, end);
            SupplierEntity supplier = supplierRepository.getSupplierDetailsAndBankById(supplierId);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Invoice_"
                    + supplier.getFirstName() + "_" + supplier.getLastName() + ".pdf");

            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            String imagePath = "D:\\Git\\FarmFresh\\farm-fresh\\src\\main\\webapp\\images\\farm-fresh-logo.png";
            Image bg = Image.getInstance(imagePath);
            bg.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            bg.setAbsolutePosition(0, 0);
            PdfContentByte canvas = writer.getDirectContentUnder();
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(0.15f);
            canvas.saveState();
            canvas.setGState(gState);
            canvas.addImage(bg);
            canvas.restoreState();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.WHITE);
            Font sectionHeaderFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(0, 51, 102));
            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
            Font italicFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell headerCell = new PdfPCell(new Phrase("FARM FRESH - SUPPLIER PAYMENT INVOICE", titleFont));
            headerCell.setBackgroundColor(new BaseColor(0, 102, 204));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(12);
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(headerCell);
            document.add(headerTable);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Invoice Date: " + LocalDate.now(), normalFont));
            document.add(new Paragraph("Period: " + start + " to " + end, normalFont));
            document.add(Chunk.NEWLINE);

            PdfPTable supplierTable = new PdfPTable(2);
            supplierTable.setWidthPercentage(100);
            PdfPCell supplierHeader = new PdfPCell(new Phrase("Supplier Details", sectionHeaderFont));
            supplierHeader.setColspan(2);
            supplierHeader.setBackgroundColor(new BaseColor(230, 240, 255));
            supplierHeader.setPadding(6);
            supplierTable.addCell(supplierHeader);
            supplierTable.addCell("Supplier Name");
            supplierTable.addCell(supplier.getFirstName() + " " + supplier.getLastName());
            supplierTable.addCell("Phone");
            supplierTable.addCell(supplier.getPhoneNumber());
            supplierTable.addCell("Address");
            supplierTable.addCell(supplier.getAddress());
            document.add(supplierTable);
            document.add(Chunk.NEWLINE);

            if (supplier.getSupplierBankDetails() != null) {
                PdfPTable bankTable = new PdfPTable(2);
                bankTable.setWidthPercentage(100);
                PdfPCell bankHeader = new PdfPCell(new Phrase("Bank Details", sectionHeaderFont));
                bankHeader.setColspan(2);
                bankHeader.setBackgroundColor(new BaseColor(230, 240, 255));
                bankHeader.setPadding(6);
                bankTable.addCell(bankHeader);
                bankTable.addCell("Bank Name");
                bankTable.addCell(supplier.getSupplierBankDetails().getBankName());
                bankTable.addCell("Branch");
                bankTable.addCell(supplier.getSupplierBankDetails().getBankBranch());
                bankTable.addCell("Account No");
                bankTable.addCell(supplier.getSupplierBankDetails().getAccountNumber());
                bankTable.addCell("IFSC");
                bankTable.addCell(supplier.getSupplierBankDetails().getIFSCCode());
                document.add(bankTable);
                document.add(Chunk.NEWLINE);
            }

            PdfPTable milkTable = new PdfPTable(5);
            milkTable.setWidthPercentage(100);
            milkTable.setSpacingBefore(5);
            milkTable.setSpacingAfter(5);
            milkTable.setWidths(new float[]{2.5f, 2.5f, 1.5f, 1.5f, 1.5f});

            String[] milkHeaders = {"Milk Type", "Date", "Quantity (L)", "Price per litre", "Total Amount"};
            for (String h : milkHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(h, tableHeaderFont));
                cell.setBackgroundColor(new BaseColor(0, 102, 204));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                milkTable.addCell(cell);
            }

            double total = 0;
            for (CollectMilkEntity m : milkList) {
                milkTable.addCell(new Phrase(m.getTypeOfMilk(), normalFont));
                milkTable.addCell(new Phrase(m.getCollectedDate().toString(), normalFont));
                milkTable.addCell(new Phrase(String.valueOf(m.getQuantity()), normalFont));
                milkTable.addCell(new Phrase(String.valueOf(m.getPrice()), normalFont));
                milkTable.addCell(new Phrase(String.valueOf(m.getTotalAmount()), normalFont));
                total += m.getTotalAmount();
            }
            document.add(new Paragraph("Milk Collection Summary", sectionHeaderFont));
            document.add(milkTable);
            document.add(Chunk.NEWLINE);

            PdfPTable payTable = new PdfPTable(2);
            payTable.setWidthPercentage(100);
            PdfPCell payHeader = new PdfPCell(new Phrase("Payment Details", sectionHeaderFont));
            payHeader.setColspan(2);
            payHeader.setBackgroundColor(new BaseColor(230, 240, 255));
            payHeader.setPadding(6);
            payTable.addCell(payHeader);
            payTable.addCell("Payment Date");
            payTable.addCell(payment.getPaymentDate().toString());
            payTable.addCell("Total Amount");
            payTable.addCell(String.format("₹ %.2f", total));
            payTable.addCell("Status");
            payTable.addCell(payment.getPaymentStatus());
            document.add(payTable);

            document.add(Chunk.NEWLINE);
            Paragraph thanks = new Paragraph("Thank you for your continued partnership!", italicFont);
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();
            log.info("Invoice generated successfully for supplier {}", supplierId);

        } catch (Exception e) {
            log.error("Error generating invoice for supplier {}: {}", supplierId, e.getMessage(), e);
            throw new RuntimeException("Error generating enhanced supplier invoice PDF", e);
        }
    }

}
