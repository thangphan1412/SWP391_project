package com.shopping.example.controller.thymleaf;

import com.shopping.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller()

public class ShipperController {

    @Autowired
    OrderService orderService;

//    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")

//    @GetMapping("/shipper")
//    public String viwshipper() {
//        return "shipping";
//    }

    @GetMapping("/shipping")
    public ModelAndView viewOrderList(){
        ModelAndView modelAndView = new ModelAndView("shipping-order-list");
        modelAndView.addObject("orders", orderService.getAllOrdersNotShip());
        System.out.println(orderService.getAllOrdersNotShip());
        return modelAndView;
    }


    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId, @RequestParam("action") String action) {
        if ("receive".equals(action)) {
            orderService.updateOrderStatus(orderId, "Shipping");
        } else if ("complete".equals(action)) {
            orderService.updateOrderStatus(orderId, "Complete");
            orderService.updatePaymentStatus(orderId, "Received");
        }
        return "redirect:/shipping";
    }
}
