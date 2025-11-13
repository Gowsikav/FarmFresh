package com.xworkz.farmfresh.controller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.CollectMilkDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.service.AdminService;
import com.xworkz.farmfresh.service.CollectMilkService;
import com.xworkz.farmfresh.service.SupplierService;
import com.xworkz.farmfresh.util.CommonControllerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
public class CollectMilkController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CollectMilkService collectMilkService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CommonControllerHelper controllerHelper;

    public CollectMilkController()
    {
        log.info("CollectMilkController constructor");
    }

    // admin
    @GetMapping("/redirectToCollectMilk")
    public String getCollectMilkPage(HttpSession session, Model model)
    {
        log.info("getCollectMilkPage in CollectMilkController");
        String email = (String) session.getAttribute("adminEmail");
        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto",adminDTO);
        controllerHelper.addNotificationData(model,email);
        return "CollectMilk";
    }

    //admin
    @PostMapping("addCollectMilk")
    public String getCollectedMilk(@Valid CollectMilkDTO collectMilkDTO, BindingResult bindingResult,HttpSession session, Model model)
    {
        log.info("getCollectedMilk method in CollectMilkController");
        String email = (String) session.getAttribute("adminEmail");
        if(bindingResult.hasErrors())
        {
            log.error("Fields has error");
            bindingResult.getFieldErrors().stream().map(e->e.getField()+" -> "+e.getDefaultMessage())
                    .forEach(log::error);
            model.addAttribute("error","Wrong details");
            model.addAttribute("milk",collectMilkDTO);
            return getCollectMilkPage(session,model);
        }
        if(collectMilkService.save(collectMilkDTO,email))
        {
            log.info("successfully saved");
            model.addAttribute("success","Details saved");
            return getCollectMilkDetailsPage(session,String.valueOf(LocalDate.now()),String.valueOf(LocalDate.now()),model);
        }else {
            log.info("Not saved");
            model.addAttribute("error","Details not saved");
            model.addAttribute("milk",collectMilkDTO);
        }
        return getCollectMilkPage(session,model);
    }

    // admin
    @GetMapping("/redirectToCollectMilkDetails")
    public String getCollectMilkDetailsPage(HttpSession session,@RequestParam(required = false) String fromSearchDate,
                                            @RequestParam(required = false) String toSearchDate, Model model)
    {
        log.info("getCollectMilkDetailsPage method in collect milk controller");
        String email = (String) session.getAttribute("adminEmail");
        LocalDate fromDate = (fromSearchDate != null && !fromSearchDate.isEmpty())
                ? LocalDate.parse(fromSearchDate)
                : LocalDate.now();
        LocalDate toDate = (toSearchDate != null && !toSearchDate.isEmpty())
                ? LocalDate.parse(toSearchDate)
                : LocalDate.now();
        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto",adminDTO);
        List<CollectMilkDTO> list=collectMilkService.getAllDetailsByDate(fromDate,toDate);
        model.addAttribute("milkList",list);
        model.addAttribute("fromSearchDate", fromDate);
        model.addAttribute("toSearchDate",toDate);
        controllerHelper.addNotificationData(model,email);
        return "CollectMilkDetails";
    }

    // supplier
    @GetMapping("redirectToMilkCollection")
    public String getAllMilkDetailsBySupplier(HttpSession session,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "15") int size, Model model)
    {
        log.info("getAllMilkDetailsBySupplier method in collect milk controller");
        String email = (String) session.getAttribute("supplierEmail");
        List<CollectMilkDTO> list=collectMilkService.getAllDetailsBySupplier(email,page,size);
        model.addAttribute("milkCollectionList",list);
        SupplierDTO supplierDTO=supplierService.getDetailsByEmail(email);
        model.addAttribute("dto",supplierDTO);
        Integer totalMilk= collectMilkService.getCountOFMilkDetailsByEmail(email);
        int totalPages = (int) Math.ceil((double) totalMilk / size);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        return "SupplierMilkCollection";
    }

    // admin
    @GetMapping("/redirectToExportAllMilkCollectData")
    public void exportAllMilkCollectData(HttpServletResponse response)
    {
        log.info("exportAllMilkCollectData method in collect milk controller");
        collectMilkService.exportAllMilkCollectData(response);
    }
}
