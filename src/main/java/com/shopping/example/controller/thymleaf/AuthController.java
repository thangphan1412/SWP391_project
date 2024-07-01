package com.shopping.example.controller.thymleaf;

import com.shopping.example.DTO.request.RegisterRequest;
import com.shopping.example.entity.Account;
import com.shopping.example.entity.Customer;
import com.shopping.example.entity.Order;
import com.shopping.example.service.AccountService;
import com.shopping.example.service.CustomerService;

import com.shopping.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;


    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @GetMapping("/UserDetail")
    public String getUserDetailPage(Model model) {
        Account account = accountService.getCurrentAccount();
        if (account == null){
            return "redirect:/login";
        }

        List<Order> orderList = orderService.getAllOrders();
        Map<String, Long> orderCountsByStatus = orderList.stream()
                .collect(Collectors.groupingBy(Order::getOrderStatus, Collectors.counting()));

        model.addAttribute("placedOrders", orderCountsByStatus.getOrDefault("Pending", 0L));
        model.addAttribute("cancelledOrders", orderCountsByStatus.getOrDefault("Cancelled", 0L));
        model.addAttribute("deliveredOrders", orderCountsByStatus.getOrDefault("Delivered", 0L));
        model.addAttribute("processingOrders", orderCountsByStatus.getOrDefault("Processing", 0L));

        model.addAttribute("account", accountService.findByEmail(account.getEmail()));

        return "dash-my-profile";
    }

    @GetMapping("/EditProfile")
    public String getEditProfilePage(Model model) {
        Account account = accountService.getCurrentAccount();
        model.addAttribute("account", accountService.findByEmail(account.getEmail()));

        List<Order> orderList = orderService.getAllOrders();
        Map<String, Long> orderCountsByStatus = orderList.stream()
                .collect(Collectors.groupingBy(Order::getOrderStatus, Collectors.counting()));

        model.addAttribute("placedOrders", orderCountsByStatus.getOrDefault("Pending", 0L));
        model.addAttribute("cancelledOrders", orderCountsByStatus.getOrDefault("Cancelled", 0L));
        model.addAttribute("deliveredOrders", orderCountsByStatus.getOrDefault("Delivered", 0L));
        model.addAttribute("processingOrders", orderCountsByStatus.getOrDefault("Processing", 0L));

        return "dash-edit-profile";
    }

    @PostMapping("/UpdateUser")
    public String updateUser(
            //Get parameters from the form
            @RequestParam(name = "fullname") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name =  "phone") String phone,
            @RequestParam(name = "district") String district,
            @RequestParam(name = "ward") String ward,
            @RequestParam(name = "city") String city,
            RedirectAttributes redirectAttributes) {

        Customer customer = customerService.findCustomerByEmail(email);
        if (customer != null) {
            customer.setFullname(name);
            customer.setPhone(phone);
            customer.setWard(ward);
            customer.setCity(city);
            customer.setDistrict(district);
            customer.setAddressDetail(district + " " + ward + " " + city);
            customerService.saveCus(customer);
            redirectAttributes.addFlashAttribute("successMessage", "User details updated successfully");
        }
        return "redirect:/EditProfile";
    }




}
