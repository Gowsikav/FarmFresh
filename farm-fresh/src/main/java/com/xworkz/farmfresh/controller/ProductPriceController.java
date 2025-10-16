package com.xworkz.farmfresh.controller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.ProductPriceDTO;
import com.xworkz.farmfresh.service.AdminService;
import com.xworkz.farmfresh.service.ProductPriceService;
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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
public class ProductPriceController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductPriceService productPriceService;

    @Autowired
    private CommonControllerHelper controllerHelper;

    public ProductPriceController()
    {
        log.info("ProductPriceController constructor");
    }

    @GetMapping("/redirectToProductsPrice")
    public String getProductPricePage(@RequestParam String email, Model model)
    {
        log.info("getProductPricePage method in product price controller");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);

        List<ProductPriceDTO> list=productPriceService.getAllDetails();
        model.addAttribute("productsPrice",list);
        controllerHelper.addNotificationData(model,email);
        return "ProductsPrice";
    }

    @PostMapping("/addProductWithPrice")
    public String getProduct(@Valid ProductPriceDTO productPriceDTO, BindingResult bindingResult,@RequestParam String adminEmail,Model model)
    {
        log.info("getProduct method in product price controller");
        if(bindingResult.hasErrors())
        {
            log.error("fields has error");
            bindingResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getField() + "->" + fieldError.getDefaultMessage())
                    .forEach(System.out::println);
            model.addAttribute("product", productPriceDTO);
            model.addAttribute("error", "Details not saved");
            return getProductPricePage(adminEmail, model);
        }
        if(productPriceService.saveProduct(productPriceDTO,adminEmail))
            model.addAttribute("success","Product Details saved");
        else
            model.addAttribute("error","Product details not saved");
        return getProductPricePage(adminEmail,model);
    }

    @PostMapping("/updateProductPrice")
    public String updateProductPrice(@Valid ProductPriceDTO productPriceDTO,BindingResult bindingResult,@RequestParam String adminEmail,Model model)
    {
        log.info("updateProductPrice method in product price controller");
        log.info("edit{}",productPriceDTO);
        if(bindingResult.hasErrors())
        {
            log.error("fields has error");
            bindingResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getField() + "->" + fieldError.getDefaultMessage())
                    .forEach(System.out::println);
            model.addAttribute("product", productPriceDTO);
            model.addAttribute("error", "Details has error");
            return getProductPricePage(adminEmail, model);
        }
        if(productPriceService.updateProduct(productPriceDTO,adminEmail))
            model.addAttribute("success","Product updated");
        else
            model.addAttribute("error","product details not updated");
        return getProductPricePage(adminEmail,model);
    }

    @GetMapping("/deleteProductPrice")
    public String deleteProduct(@RequestParam Integer productId,@RequestParam String adminEmail,Model model)
    {
        log.info("delete product method in product price controller");
        if(productPriceService.deleteProduct(productId))
            model.addAttribute("success","Product deleted successfully!");
        else model.addAttribute("error","Product not deleted");
        return getProductPricePage(adminEmail,model);
    }
}
