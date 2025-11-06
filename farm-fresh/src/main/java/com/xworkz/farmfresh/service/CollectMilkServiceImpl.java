package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.CollectMilkDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.entity.*;
import com.xworkz.farmfresh.repository.CollectMilkRepository;
import com.xworkz.farmfresh.repository.NotificationRepository;
import com.xworkz.farmfresh.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CollectMilkServiceImpl implements CollectMilkService {

    @Autowired
    private CollectMilkRepository collectMilkRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public CollectMilkServiceImpl() {
        log.info("CollectMilkServiceImpl constructor");
    }

    @Override
    public boolean save(CollectMilkDTO collectMilkDTO, String email) {
        log.info("save method in CollectMilkServiceImpl");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);

        AdminEntity adminEntity = new AdminEntity();
        BeanUtils.copyProperties(adminDTO, adminEntity);

        CollectMilkEntity collectMilkEntity = new CollectMilkEntity();
        BeanUtils.copyProperties(collectMilkDTO, collectMilkEntity);
        CollectMilkAuditEntity collectMilkAuditEntity = new CollectMilkAuditEntity();
        collectMilkAuditEntity.setCreatedAt(LocalDateTime.now());
        collectMilkAuditEntity.setCreatedBy(adminDTO.getAdminName());
        collectMilkAuditEntity.setCollectMilkEntity(collectMilkEntity);

        collectMilkEntity.setCollectMilkAuditEntity(collectMilkAuditEntity);

        collectMilkEntity.setAdmin(adminEntity);
        collectMilkEntity.setSupplier(supplierRepository.getSupplierByPhone(collectMilkDTO.getPhoneNumber()));
        return collectMilkRepository.save(collectMilkEntity);
    }

    @Override
    public List<CollectMilkDTO> getAllDetailsByDate(LocalDate selectDate) {
        log.info("getAllDetailsByDate method in collect milk service");
        List<CollectMilkEntity> collectMilkEntityList = collectMilkRepository.getAllDetailsByDate(selectDate);
        List<CollectMilkDTO> collectMilkDTOS = new ArrayList<>();
        collectMilkEntityList.forEach(collectMilkEntity -> {
            CollectMilkDTO collectMilkDTO = new CollectMilkDTO();
            BeanUtils.copyProperties(collectMilkEntity, collectMilkDTO);
            if (collectMilkEntity.getSupplier() != null) {
                SupplierDTO supplierDTO = new SupplierDTO();
                BeanUtils.copyProperties(collectMilkEntity.getSupplier(), supplierDTO);
                collectMilkDTO.setSupplier(supplierDTO);
            }
            collectMilkDTOS.add(collectMilkDTO);
        });
        return collectMilkDTOS;
    }

    @Override
    public List<CollectMilkDTO> getAllDetailsBySupplier(String email, int page, int size) {
        log.info("getAllDetailsBySupplier method in collect milk service");
        List<CollectMilkEntity> collectMilkEntityList = collectMilkRepository.getAllDetailsBySupplier(email, page, size);
        List<CollectMilkDTO> collectMilkDTOS = new ArrayList<>();
        collectMilkEntityList.forEach(collectMilkEntity -> {
            CollectMilkDTO collectMilkDTO = new CollectMilkDTO();
            BeanUtils.copyProperties(collectMilkEntity, collectMilkDTO);
            if (collectMilkEntity.getSupplier() != null) {
                SupplierDTO supplierDTO = new SupplierDTO();
                BeanUtils.copyProperties(collectMilkEntity.getSupplier(), supplierDTO);
                collectMilkDTO.setSupplier(supplierDTO);
            }
            collectMilkDTOS.add(collectMilkDTO);
        });
        return collectMilkDTOS;
    }

    @Override
    public Integer getCountOFMilkDetailsByEmail(String email) {
        log.info("getCountOFMilkDetailsByEmail method in collectMilk service");
        return collectMilkRepository.getCountOFMilkDetailsByEmail(email);
    }

    @Override
    public List<CollectMilkDTO> getAllDetailsBySupplier(Long notificationId) {
        log.info("getAllDetailsBySupplier method in collect milk service");
        NotificationEntity notification = notificationRepository.getNotificationById(notificationId);
        SupplierEntity supplier = notification.getSupplier();

        String message = notification.getMessage();
        Pattern pattern = Pattern.compile("\\((\\d{4}-\\d{2}-\\d{2})\\s+to\\s+(\\d{4}-\\d{2}-\\d{2})\\)");
        Matcher matcher = pattern.matcher(message);
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (matcher.find()) {
            String startDateStr = matcher.group(1);
            String endDateStr = matcher.group(2);

            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        }
        List<CollectMilkEntity> list = collectMilkRepository.getCollectMilkDetailsForSupplierById(supplier.getSupplierId(), startDate, endDate);
        List<CollectMilkDTO> collectMilkDTOS = new ArrayList<>();
        list.forEach(e -> {
            CollectMilkDTO collectMilkDTO = new CollectMilkDTO();
            BeanUtils.copyProperties(e, collectMilkDTO);
            collectMilkDTOS.add(collectMilkDTO);
        });
        return collectMilkDTOS;
    }

    @Override
    public LocalDate getLastCollectedDate(Integer supplierId) {
        log.info("getLastCollectedDate method in collect milk service");
        return collectMilkRepository.getLastCollectedDate(supplierId);
    }

    @Override
    public Double getTotalLitre(Integer supplierId) {
        log.info("getTotalLitre method in collect milk service");
        return collectMilkRepository.getTotalLitre(supplierId);
    }

    @Override
    public Double getTotalMilkCollected() {
        log.info("getTotalMilkCollected method in collect milk service");
        return collectMilkRepository.getTotalMilkCollected();
    }

    @Override
    public List<CollectMilkDTO> getRecentCollections() {
        log.info("getRecentCollections method in milk collect service");
        List<CollectMilkDTO> collectMilkDTOS = new ArrayList<>();
        List<CollectMilkEntity> collectMilkEntityList = collectMilkRepository.getRecentCollections();
        collectMilkEntityList.forEach(collectMilkEntity -> {
            CollectMilkDTO collectMilkDTO = new CollectMilkDTO();
            BeanUtils.copyProperties(collectMilkEntity, collectMilkDTO);
            if (collectMilkEntity.getSupplier() != null) {
                SupplierDTO supplierDTO = new SupplierDTO();
                BeanUtils.copyProperties(collectMilkEntity.getSupplier(), supplierDTO);
                collectMilkDTO.setSupplier(supplierDTO);
            }
            collectMilkDTOS.add(collectMilkDTO);
        });
        return collectMilkDTOS;
    }

    @Override
    public void exportAllMilkCollectData(HttpServletResponse response) {
        log.info("exportAllMilkCollectData method in collect milk service");
        List<CollectMilkEntity> collectMilk = collectMilkRepository.getAllEntityForExport();
        try {
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=collect_milk_data.csv");

            PrintWriter writer = response.getWriter();
            writer.println("CollectMilkId,SupplierName,CollectedBy,TypeOfMilk,Price,Quantity,TotalAmount,CollectedDate");

            for (CollectMilkEntity milk : collectMilk) {
                writer.println(
                        milk.getCollectMilkId() + "," +
                                milk.getSupplier().getFirstName() + " " + milk.getSupplier().getLastName() + "," +
                                milk.getAdmin().getAdminName() + "," +
                                milk.getTypeOfMilk() + "," +
                                milk.getPrice() + "," +
                                milk.getQuantity() + "," +
                                milk.getTotalAmount() + "," +
                                milk.getCollectedDate()
                );
            }

            writer.flush();
            writer.close();
            log.info("Export collect milk data list in csv is done");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
