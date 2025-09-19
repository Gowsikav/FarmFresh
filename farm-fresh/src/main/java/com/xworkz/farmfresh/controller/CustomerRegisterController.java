package com.xworkz.farmfresh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class CustomerRegisterController {

    public CustomerRegisterController() {
        System.out.println("CustomerRegisterController constructor");
    }

    @GetMapping("/redirectToIndex")
    public String getHomePage()
    {
        System.out.println("redirected to Home page");
        return "index";
    }

    @GetMapping("/redirectToCustomerRegister")
    public String getFarmerRegisterPage()
    {
        System.out.println("redirected to farmer register");
        return "FarmerRegister";
    }
}
