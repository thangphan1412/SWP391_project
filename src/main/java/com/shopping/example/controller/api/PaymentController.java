package com.shopping.example.controller.api;



import com.shopping.example.entity.Receipt;
import com.shopping.example.payment.Config;
import com.shopping.example.service.ReceiptService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
    public String paymentResult(@RequestParam Map<String, String> requestParams) {
        String vnp_ResponseCode = requestParams.get("vnp_ResponseCode");
        String receiptId = requestParams.get("vnp_TxnRef");
        if ("00".equals(vnp_ResponseCode)) {
            Optional<Receipt> existReceipt = receiptService.getReceipt(Long.parseLong(receiptId));
            if (existReceipt.isPresent()) {
                Receipt receipt = existReceipt.get();
                receipt.setReceiptStatus("Complete");
                receiptService.addReceipt(receipt);
                return "successful";
            }
        } else {
            return "redirect:/failed.html";
        }
        return "redirect:/receiptDetail/" + receiptId;
    }



}
