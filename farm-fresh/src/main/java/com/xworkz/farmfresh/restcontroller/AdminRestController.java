package com.xworkz.farmfresh.restcontroller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/")
@Api(value = "Admin management")
public class AdminRestController {

    @Autowired
    private AdminService adminService;

    public AdminRestController()
    {
        System.out.println("AdminRestController constructor");
    }

    @PostMapping("/adminDetails")
    @ApiOperation(value = "Save admin data")
    public ResponseEntity<String> saveAdminDetails(@Valid @RequestBody AdminDTO adminDTO,
                                                   BindingResult bindingResult) {
        System.out.println("saveAdminDetails method in rest controller");

        if(bindingResult.hasErrors()) {
            System.out.println("Error in fields");
            bindingResult.getFieldErrors()
                    .forEach(fieldError -> System.out.println(fieldError.getField()+"-> "+fieldError.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in fields");
        }
        if(adminService.save(adminDTO)) {
            return ResponseEntity.ok("Details saved");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid details");
        }
    }

    @GetMapping("/checkEmail")
    public boolean checkEmailForForgotPassword(@RequestParam("email")String email)
    {
        log.info("checkEmailForForgotPassword method in admin Rest Controller");
        return adminService.checkEmail(email);
    }

}
