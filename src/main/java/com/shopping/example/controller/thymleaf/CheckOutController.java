package com.shopping.example.controller.thymleaf;


import com.shopping.example.payment.Config;
import com.shopping.example.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.shopping.example.entity.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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

    @Autowired
    private ProductService productService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private VoucherService voucherService;


    @GetMapping("/checkout")
    public String checkOut(Model model, RedirectAttributes redirectAttributes) {
        Account currentAccount = accountService.getCurrentAccount();
        Double discount = 0.0;

        if (currentAccount == null) {
            return "redirect:/login";
        } else {
            Customer customer = currentAccount.getCustomer();
            Cart customerCart = cartService.getCartByCustomer(customer);


            System.out.println(customer.getAddressDetail());
            System.out.println(customer.getCity());
            System.out.println(customer.getFullname());
            System.out.println(customer.getPhone());


            if (customerCart == null){
                redirectAttributes.addFlashAttribute("delMessage", "Please add product to cart");
                return "redirect:/viewCart";
            }
            List<CartItems> listCartItems = cartItemsService.findByCartId(customerCart.getCartId());
            if (listCartItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("delMessage", "Please add product to cart");
                return "redirect:/viewCart";
            }

                double total = 0.0;
            for (CartItems cartItem : listCartItems) {
                total += cartItem.getQuantity() * cartItem.getProductType().getProduct_type_price();
            }
            model.addAttribute("ListCart", listCartItems);
            model.addAttribute("total", total);
            model.addAttribute("customer", currentAccount);
            model.addAttribute("discount", discount);
            return "checkout";
        }
    }


    //place order
    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam String phone,
                             @RequestParam String address,
                             @RequestParam(required = false) String payment,
                             @RequestParam String voucherCode,
                             Model model,
                             HttpServletRequest req,
                             RedirectAttributes redirectAttributes) {
        Account currentAccount = accountService.getCurrentAccount();
        List<Employee> existEmployeeList = employeeService.getAllEmployees();
        Employee randomEmployee = existEmployeeList.get(new Random().nextInt(existEmployeeList.size()));

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

            double total = 0;
            for (CartItems cartItem : listCartItems) {
                total += cartItem.getQuantity() * cartItem.getProductType().getProduct_type_price();
            }

            if (voucherCode == null || voucherCode.isEmpty()) {
                // Không có mã voucher, lưu đơn hàng với tổng số tiền
                order.setOrderAmount(total);
            } else {
                // Fetch voucher details
                Voucher voucher = voucherService.findVoucherWithCoce(voucherCode);
                // Check if voucher is valid
                if (voucher == null || voucher.getStatus().equalsIgnoreCase("inactive") || voucher.getEndDate().isBefore(LocalDate.now()) ) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Invalid voucher code");
                    return "redirect:/checkout";
                }
                // Check if the voucher has already been used by the customer
                List<Order> orderList = orderService.getOrdersByCustomer(customer);
                boolean voucherUsed = orderList.stream()
                        .anyMatch(o -> {
                            Voucher orderVoucher = o.getVoucher();
                            return orderVoucher != null && orderVoucher.getVoucherCode().equalsIgnoreCase(voucherCode);
                        });

                if (voucherUsed) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Voucher has been used");
                    return "redirect:/checkout";
                }

                // Apply voucher discount
                total = total - (voucher.getPercentageDiscount() / 100.0 * total);
                order.setVoucher(voucher);
                voucher.setQuantity(voucher.getQuantity()-1);
                voucherService.save(voucher);
                order.setOrderAmount(total);
            }

            order.setCustomer(customer);
            order.setEmployee(randomEmployee);
            order.setAddressOfReceiver(address);
            order.setCreateDate(LocalDate.now());
            order.setPhoneOfReceiver(phone);
            order.setOrderStatus("Pending");
            order.setPaymentMethod(payment);
            order.setPaymentStatus("Pending");
            orderService.save(order);

            if (payment.equalsIgnoreCase("Pay with VnPay")) {
                req.getSession().setAttribute("type", "order");
                req.getSession().setAttribute("amount", total);
                req.getSession().setAttribute("Id", order.getOrderId());
                return "redirect:/initiate-vnpay-payment";
            }

            for (CartItems cartItem : listCartItems) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProductType(cartItem.getProductType());
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPrice(cartItem.getProductType().getProduct_type_price());
                orderDetailService.saveOrderDetail(orderDetail);
                cartItemsService.delete(cartItem.getCartItemsId());

                ProductType productType = cartItem.getProductType();
                productType.setProduct_type_quantity(productType.getProduct_type_quantity() - cartItem.getQuantity());
                productTypeService.saveProductType(productType);
            }

            model.addAttribute("order", order);
            return "redirect:/checkout-success";
        }
    }


    //check out with vnpay
    @GetMapping("/initiate-vnpay-payment")
    public void processVnPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long amount;
        double doubleAmount = (double) req.getSession().getAttribute("amount");
        long totalAmount = (long) doubleAmount;
        System.out.println("BBB      " + totalAmount);


        try {
//            amount = totalAmount * 100;
            amount = totalAmount * 25454 * 100;
            System.out.println("Payment:  " + amount);
            // Chuyển đổi sang đơn vị xu
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid amount");
            return;
        }

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = (String) req.getSession().getAttribute("type");
        String bankCode = req.getParameter("bankCode");

        Long orderId = (Long) req.getSession().getAttribute("Id");

        String vnp_TxnRef = String.valueOf(orderId);
        String vnp_IpAddr = Config.getIpAddress(req);

        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:8080/api/payment/orderPaymentResult");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        resp.sendRedirect(paymentUrl);
    }



    @GetMapping("/checkout-success")
    public String home(Model model) {
        return "checkout-success";
    }


    @PostMapping("/checkDiscount")
    public String checkDiscount(@RequestParam("voucherCode") String code, Model model, RedirectAttributes redirectAttributes) {
        if (code.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Voucher code must not be empty.");
            return "redirect:/checkout";
        }

        Voucher existVoucher = voucherService.findVoucherWithCoce(code);
        if (existVoucher == null || existVoucher.getStatus().equals("inactive") || existVoucher.getEndDate().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Voucher is no longer available.");
            return "redirect:/checkout";
        }

        double discount = voucherService.VoucherDiscount(code.trim());
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

            double discountAmount = (discount / 100) * total;
            total = total - discountAmount;
            model.addAttribute("ListCart", listCartItems);
            model.addAttribute("total", total);
            model.addAttribute("customer", currentAccount);
            model.addAttribute("discount", discount);
            model.addAttribute("voucherCode", code);

            return "checkout";
        }
    }

}
