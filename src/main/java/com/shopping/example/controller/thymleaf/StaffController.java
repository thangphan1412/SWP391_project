package com.shopping.example.controller.thymleaf;

import com.shopping.example.entity.*;
import com.shopping.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("employee")
public class StaffController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;

    // show sreec for employee
    @GetMapping("/employees")
    public String showScreen(){
        return "admin-manager";
    }

    // view customer from to employee
    @GetMapping("/viewCustomer")
    public String viewListCustomer(Model model){
        model.addAttribute("customers", customerService.getAll());
        return "staff-page-viewCustomers";
    }

    // view customer detail
    @GetMapping("/customerDetail/{id}")
    public String getCustomer(@PathVariable Long id, Model model){
        Optional<Customer> customer = customerService.findCustomerById(id);

        if (customer.isPresent()){
            model.addAttribute("customerDetail", customer.get());

        } else {
            model.addAttribute("error customer", "customer not found");
        }

        return "customers-detail";
    }

    // view brand detail from to employees
    @GetMapping("/brandDetail/{id}")
    public String getBrand(@PathVariable Long id, Model model){
        Optional<Brand> brand = brandService.findById(id);
        if(brand.isPresent()){
            model.addAttribute("brandDetail", brand.get());
        } else {
            model.addAttribute("error brand", "brand not found");
        }
        return "brands-detail";
    }

    // view suppliers detail for employees
    @GetMapping("/suppliers/{id}")
    public String getSuppliers(@PathVariable Long id, Model model){
        Optional<Supplier> supplier = supplierService.findSupplierById(id);
        if (supplier.isPresent()){
            model.addAttribute("supplierDetail", supplier.get());
        } else {
            model.addAttribute("error supplier", "supplier not found");
        }
        return "suppliers-detail";
    }

    // view brand product from to employee
    @GetMapping("/viewBrand")
    public String viewBrand(Model model){
        List <Brand> allBrand = brandService.findAll();
        Set<Brand> uniqueBrand = new HashSet<>(allBrand);
        List<Brand> uniqueBrandList = uniqueBrand.stream().collect(Collectors.toList());
        model.addAttribute("brands", uniqueBrandList);
        return "brand-list";
    }

    // view supppliers product form employee
    @GetMapping("/viewSuppliers")
    public String viewSuppliers(Model model){
        model.addAttribute("suppliers", supplierService.findAllSuppliers());
        return "suppliers-list";
    }


    // view order list
    @GetMapping("/viewOrder")
    public String viewOrderList(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "order-list";
    }

    // view order detail of one product
    @GetMapping("/viewOrderDetail/{id}")
    public String viewOrderDetail(Model model, @PathVariable("id")Long id){
        Optional<Order> order = Optional.ofNullable(orderService.getOrderById(id));
        if(order.isPresent()){
            model.addAttribute("orderDetailss", order.get());
        } else {
            System.out.println("Nothing to show");
        }
        return "order-detail";
    }


    @GetMapping("/editOrder/{id}")
    public String viewEditOrder(Model model, @PathVariable("id")Long id){
        Optional<Order> order = Optional.ofNullable(orderService.getOrderById(id));
        if(order.isPresent()){
            model.addAttribute("orderDetails", order.get());
        } else {
            System.out.println("Nothing to show");
        }

        return "editOrder";
    }
    // edit status order detail by status
    @PostMapping("/updateOrder/{id}")
    public String updateOrder(@PathVariable("id") Long id, @RequestParam(name = "status") String status, @RequestParam("email") String email, Model model){
        Order order = (Order) orderService.findByCustomerEmail(email);
        if (order != null && order.getOrderId().equals(id)) {
            // Set new status
            order.setOrderStatus(status);
            // Save the updated order
            orderService.save(order);
            // Add message to the model
            model.addAttribute("message", "Cập nhật trạng thái đơn hàng thành công!");
            return "editOrder";
        } else {
            // Handle case where order is not found or ID does not match
            model.addAttribute("message", "Không tìm thấy đơn hàng hoặc ID không khớp!");
            return "orderNotFound";
        }
    }

    @PostMapping("/updateOrderPaymentStatus")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId, @RequestParam("action") String action) {
        if ("confirm".equals(action)) {
            orderService.updatePaymentStatus(orderId, "Success");
        }
        return "redirect:/viewOrder";
    }


}
