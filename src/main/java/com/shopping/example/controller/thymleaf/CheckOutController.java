package com.shopping.example.controller.thymleaf;


import com.shopping.example.entity.*;
import com.shopping.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class CheckOutController {
    @Autowired
    CartItemsService cartItemsService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/checkout")
    public String checkOut(Model model) {
        Account currentAccount = accountService.getCurrentAccount();
        if (currentAccount == null) {
            return "redirect:/login";
        } else {
            Customer customer = currentAccount.getCustomer();
            Cart customerCart = cartService.getCartByCustomer(customer);
            List<CartItems> listCartItems = cartItemsService.findByCartId(customerCart.getCartId());

            double total = 0.0;
            for (CartItems cartItem : listCartItems) {
                total += cartItem.getQuantity() * cartItem.getProductType().getProduct_type_price();
            }
            model.addAttribute("ListCart", listCartItems);
            model.addAttribute("total", total);
            model.addAttribute("customer", currentAccount);
            return "checkout";
        }
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam String phone,
                             @RequestParam String address,
                             @RequestParam(required = false) String payment,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        Account currentAccount = accountService.getCurrentAccount();
        if (currentAccount == null) {
            return "redirect:/login";
        } else {
            if (payment == null || payment.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Payment method is required.");
                return "redirect:/checkout";
            }
            Customer customer = currentAccount.getCustomer();
            Cart customerCart = cartService.getCartByCustomer(customer);
            List<CartItems> listCartItems = cartItemsService.findByCartId(customerCart.getCartId());

            Order order = new Order();
            order.setCustomer(customer);
            order.setEmployee(null); // Assuming no employee involvement
            order.setAddressOfReceiver(address);
            order.setCreateDate(new Date());
            order.setPhoneOfReceiver(phone);
            order.setOrderStatus("Pending");
            order.setPaymentMethod(payment);
            order.setPaymentStatus("Pending");
            orderService.save(order);
            for (CartItems cartItem : listCartItems) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProductType(cartItem.getProductType());
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPrice(cartItem.getProductType().getProduct_type_price());
                orderDetailService.saveOrderDetail(orderDetail);
                cartItemsService.delete(cartItem.getCartItemsId());
            }
            model.addAttribute("order", order);
            return "redirect:/checkout-success";
        }
    }
    @GetMapping("/checkout-success")
    public String home(Model model) {
        return "checkout-success";
    }

}
