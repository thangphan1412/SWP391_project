package com.shopping.example.controller.thymleaf;


import com.shopping.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckOutController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SupplierService supplierService;


    @Autowired
    private ProductTechService productTechService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemsService cartItemsService;

    @GetMapping("/checkout")
    public String checkOut(Model model) {
        return "checkout";
    }





}
