package com.shopping.example.controller.api;



import com.shopping.example.entity.*;
import com.shopping.example.payment.Config;
import com.shopping.example.service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ReceiptDetailService receiptDetailService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemsService cartItemsService;

    @Autowired
    private OrderDetailService orderDetailService;



    @PostMapping("/processPayment")
    public void processPayment(@RequestParam("amount")  String amountStr,@RequestParam("type") String type,@RequestParam("Id") String id,HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long amount;
        Long totalAmount = (long) Double.parseDouble(amountStr);
        System.out.println("BBB: "+ amountStr);
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
        String orderType = type;
        String bankCode = req.getParameter("bankCode");

        String vnp_TxnRef = id;
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

        if(orderType.equalsIgnoreCase("receipt")){
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", "http://localhost:8080/api/payment/receiptPaymentResult");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        }
        else if(orderType.equalsIgnoreCase("order")){
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", "http://localhost:8080/api/payment/orderPaymentResult");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        }

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


    @GetMapping("/receiptPaymentResult")
    public void paymentResult(@RequestParam Map<String, String> requestParams,HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = requestParams.get("vnp_ResponseCode");
        String receiptId = requestParams.get("vnp_TxnRef");
        if ("00".equals(vnp_ResponseCode)) {
            Optional<Receipt> existReceipt = receiptService.getReceipt(Long.parseLong(receiptId));
            if (existReceipt.isPresent()) {
                Receipt receipt = existReceipt.get();
                receipt.setReceiptStatus("Complete");
                receiptService.addReceipt(receipt);
                List<ReceiptDetail> receiptDetailList = receiptDetailService.findAllbyReceiptId(Long.parseLong(receiptId));
                for (ReceiptDetail receiptDetail : receiptDetailList) {
                    Optional<ProductType> existProductType = productTypeService.getProductTypeById(receiptDetail.getProductTypes().getProduct_type_id());
                    if (existProductType.isPresent()) {
                        ProductType productType = existProductType.get();
                        productType.setProduct_type_quantity(productType.getProduct_type_quantity() + receiptDetail.getQuantity());
                        productTypeService.saveProductType(productType);
                    }
                }
                response.sendRedirect("/receiptDetail" + receiptId);
            }
        } else {
            response.sendRedirect("/error");
        }
    }



    @GetMapping("/orderPaymentResult")
    public void paymentOrderResult(@RequestParam Map<String, String> requestParams, Model model, HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = requestParams.get("vnp_ResponseCode");
        String orderId = requestParams.get("vnp_TxnRef");
        Order existOrder = orderService.getOrderById(Long.parseLong(orderId));
        Account currentAccount = accountService.getCurrentAccount();
        Customer customer = currentAccount.getCustomer();
        Cart customerCart = cartService.getCartByCustomer(customer);
        List<CartItems> listCartItems = cartItemsService.findByCartId(customerCart.getCartId());
        if ("00".equals(vnp_ResponseCode)) {
            // Thiết lập chi tiết đơn hàng
            for (CartItems cartItem : listCartItems) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(existOrder);
                orderDetail.setProductType(cartItem.getProductType());
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPrice(cartItem.getProductType().getProduct_type_price());
                orderDetailService.saveOrderDetail(orderDetail);
                cartItemsService.delete(cartItem.getCartItemsId());
            }
            response.sendRedirect("/checkout-success"); // Chuyển hướng tới trang "checkout-success"
        } else {
            response.sendRedirect("/error"); // Chuyển hướng tới trang "failed"
        }
    }

}







