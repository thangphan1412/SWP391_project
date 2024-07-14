package com.shopping.example.controller.thymleaf;


import com.shopping.example.entity.Account;
import com.shopping.example.entity.Customer;
import com.shopping.example.entity.Order;
import com.shopping.example.entity.OrderDetail;
import com.shopping.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class OrderController {

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

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private RouterFunctionMapping routerFunctionMapping;

    @GetMapping("viewOrderList")
    public String viewOrderList(Model model) {
        // Fetch current account
        Account currentAccount = accountService.getCurrentAccount();

        // If no current account, redirect to login
        if (currentAccount == null) {
            return "redirect:/login";
        }

        // Fetch customer associated with the current account
        Customer customer = currentAccount.getCustomer();

        // Add current account to the model
        model.addAttribute("account", currentAccount);

        // Count orders by status
        List<Order> orderList = orderService.getAllOrders();
        Map<String, Long> orderCountsByStatus = orderList.stream()
                .collect(Collectors.groupingBy(Order::getOrderStatus, Collectors.counting()));

        // Add order counts by status to the model
        model.addAttribute("placedOrders", orderCountsByStatus.getOrDefault("Pending", 0L));
        model.addAttribute("cancelledOrders", orderCountsByStatus.getOrDefault("Cancelled", 0L));
        model.addAttribute("deliveredOrders", orderCountsByStatus.getOrDefault("Delivered", 0L));
        model.addAttribute("processingOrders", orderCountsByStatus.getOrDefault("Processing", 0L));

        // Fetch orders associated with the customer
        List<Order> customerOrdersList = orderService.getOrdersByCustomer(customer);

        // Add customer's orders to the model
        model.addAttribute("customerOrdersList", customerOrdersList);

        return "dash-my-order";
    }


    @GetMapping("/viewOrder/{orderId}")
    public String viewOrderDetail(@PathVariable Long orderId, Model model) {
        // Fetch current account
        Account account = accountService.getCurrentAccount();
        Account currentAccount = accountService.findByEmail(account.getEmail());
        model.addAttribute("account", currentAccount);

        // Fetch order details
        Order order = orderService.getOrderById(orderId);
        List<OrderDetail> orderDetailsList = orderDetailService.getOrderDetailsByOrder(order);

        // Count orders by status
        List<Order> orderList = orderService.getAllOrders();
        Map<String, Long> orderCountsByStatus = orderList.stream()
                .collect(Collectors.groupingBy(Order::getOrderStatus, Collectors.counting()));

        long placedOrders = orderCountsByStatus.getOrDefault("Pending", 0L);
        long cancelledOrders = orderCountsByStatus.getOrDefault("Cancelled", 0L);
        long deliveredOrders = orderCountsByStatus.getOrDefault("Delivered", 0L);
        long processingOrders = orderCountsByStatus.getOrDefault("Processing", 0L);

        model.addAttribute("placedOrders", placedOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);
        model.addAttribute("processingOrders", processingOrders);

        // Calculate total price of the order
        double totalPrice = orderDetailsList.stream()
                .mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                .sum();

        // Calculate shipping fee based on unique product types
        long uniqueProductTypes = orderDetailsList.stream()
                .map(orderDetail -> orderDetail.getProductType().getProduct_type_id())
                .distinct()
                .count();
        double shippingFee = uniqueProductTypes * 5;

        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("order", order);
        model.addAttribute("ListOrderDetail", orderDetailsList);

        return "dash-manage-order";
    }



//    @PostMapping("/viewStatus")
//    public String viewStatus(Model model, @RequestParam("status") String orderStatus) {
//        Account currentAccount = accountService.getCurrentAccount();
//        Customer customer = currentAccount.getCustomer();
//        model.addAttribute("account", currentAccount);
//
//
//        List<Order> orderStatusList;
//        if (orderStatus.equals("All")) {
//            orderStatusList = orderService.getOrdersByCustomer(customer);
//        } else {
//            orderStatusList = orderService.findByCustomerIdAndOrderStatus(customer.getId(), orderStatus);;
//        }
//        model.addAttribute("customerOrdersList", orderStatusList);
//
//        System.out.println("BBBB" + orderStatus);
//
//
//        List<Order> orderList = orderService.getAllOrders();
//        Map<String, Long> orderCountsByStatus = orderList.stream()
//                .collect(Collectors.groupingBy(Order::getOrderStatus, Collectors.counting()));
//
//        long placedOrders = orderCountsByStatus.getOrDefault("Pending", 0L);
//        long cancelledOrders = orderCountsByStatus.getOrDefault("Cancelled", 0L);
//        long deliveredOrders = orderCountsByStatus.getOrDefault("Delivered", 0L);
//        long processingOrders = orderCountsByStatus.getOrDefault("Processing", 0L);
//
//        model.addAttribute("placedOrders", placedOrders);
//        model.addAttribute("cancelledOrders", cancelledOrders);
//        model.addAttribute("deliveredOrders", deliveredOrders);
//        model.addAttribute("processingOrders", processingOrders);
//
//
//        return "dash-my-order";
//    }

    @GetMapping("/viewStatus")
    public String viewStatus(Model model, @RequestParam("status") String orderStatus) {
        Account currentAccount = accountService.getCurrentAccount();
        Customer customer = currentAccount.getCustomer();
        model.addAttribute("account", currentAccount);

        List<Order> orderStatusList;
        if (orderStatus.equals("All")) {
            orderStatusList = orderService.getOrdersByCustomer(customer);
        } else {
            orderStatusList = orderService.findByCustomerIdAndOrderStatus(customer.getId(), orderStatus);
        }
        model.addAttribute("customerOrdersList", orderStatusList);
        model.addAttribute("selectedStatus", orderStatus); // Add the selected status to the model

        System.out.println("BBBB" + orderStatus);

        List<Order> orderList = orderService.getAllOrders();
        Map<String, Long> orderCountsByStatus = orderList.stream()
                .collect(Collectors.groupingBy(Order::getOrderStatus, Collectors.counting()));

        long placedOrders = orderCountsByStatus.getOrDefault("Pending", 0L);
        long cancelledOrders = orderCountsByStatus.getOrDefault("Cancelled", 0L);
        long deliveredOrders = orderCountsByStatus.getOrDefault("Delivered", 0L);
        long processingOrders = orderCountsByStatus.getOrDefault("Processing", 0L);

        model.addAttribute("placedOrders", placedOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);
        model.addAttribute("processingOrders", processingOrders);

        return "dash-my-order";
    }





}
