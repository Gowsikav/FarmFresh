package com.xworkz.farmfresh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class FarmerRegisterController {

    public FarmerRegisterController() {
        System.out.println("FarmerRegisterController constructor");
    }

    @GetMapping("/redirectToIndex")
    public String getHomePage()
    {
        System.out.println("redirected to Home page");
        return "index";
    }

    @GetMapping("/redirectToFarmerRegister")
    public String getFarmerRegisterPage()
    {
        System.out.println("redirected to farmer register");
        return "FarmerRegister";
    }
}
