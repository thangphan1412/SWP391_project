package com.shopping.example.controller.thymleaf;


import com.shopping.example.entity.Order;
import com.shopping.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RequestController {


    @Autowired
    private OrderService orderService;


    @GetMapping("/viewRequest")
    public String viewRequest(Model model) {
        model.addAttribute("orders", orderService.getAllOrdersRequestCancel());
        int total = 0;
        for(Order order : orderService.getAllOrdersRequestCancel()) {
            total++;
        }
        model.addAttribute("total", total);
        return "/request-cancel";
    }

    @PostMapping("/acceptRequest")
    public String acceptRequest(@RequestParam(name = "orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        order.setOrderStatus("Cancelled");
        order.setOrderRequestCancel(Boolean.TRUE);
        orderService.save(order);
        return "redirect:/viewRequest";
    }


    @PostMapping("/denyRequest")
    public String denyRequest(@RequestParam(name = "orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        order.setOrderStatus("Pending");
        order.setOrderRequestCancel(Boolean.FALSE);
        orderService.save(order);
        return "redirect:/viewRequest";
    }



}
