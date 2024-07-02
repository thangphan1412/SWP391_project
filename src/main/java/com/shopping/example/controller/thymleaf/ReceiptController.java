package com.shopping.example.controller.thymleaf;

import com.shopping.example.entity.*;
import com.shopping.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ReceiptController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptDetailService receiptDetailService;

    @Autowired
    private ProductTypeService productTypeService;


    @GetMapping("/receipt")
    public String receipt(Model model) {
        model.addAttribute("supplierList", supplierService.findAllSuppliers());
        model.addAttribute("receiptList", receiptService.getReceipts());
        return "admin-receipt";
    }

    //Create Receipt

    @PostMapping("/createReceipt")
    public String createReceipt(@RequestParam("selectedSupplier") Long supId, RedirectAttributes redirectAttributes) {
        Account currentAccount = accountService.getCurrentAccount();
        if (currentAccount == null) {
            return "redirect:/login";
        } else {
            Employee currentEmployee = currentAccount.getEmployee();
            if (currentEmployee == null) {
                return "redirect:/login";
            } else {
                Optional<Supplier> optionalSup = supplierService.findSupplierById(supId);
                if (optionalSup.isPresent()) {
                    Supplier sup = optionalSup.get();
                    Receipt receipt = new Receipt();
                    receipt.setEmployee(currentEmployee);
                    receipt.setReceiptDate(LocalDate.now());
                    receipt.setSupplier(sup);
                    receiptService.addReceipt(receipt);
                    redirectAttributes.addFlashAttribute("receiptMessage", "Add receipt successfully");
                    return "redirect:/receipt";
                }
                redirectAttributes.addFlashAttribute("receiptMessage", "Add receipt fail");
                return "redirect:/receipt";
            }
        }
    }


    //List all the receipt detail
    @GetMapping("/receiptDetail/{receiptId}")
    public String managerReceiptDetail(@PathVariable Long receiptId, Model model) {
        List<ReceiptDetail> receiptDetailList = receiptDetailService.findAllbyReceiptId(receiptId);
        model.addAttribute("receiptDetailList", receiptDetailList);

        // Tính tổng giá tiền
        double totalAmount = receiptDetailList.stream()
                .mapToDouble(rd -> rd.getPrice() * rd.getQuantity())
                .sum();
        model.addAttribute("totalAmount", totalAmount);

        Optional<Receipt> optionalReceipt = receiptService.getReceipt(receiptId);
        if (optionalReceipt.isPresent()) {
            Receipt existReceipt = optionalReceipt.get();
            model.addAttribute("existReceipt", existReceipt);
            List<ProductType> productTypeList = productTypeService.getProductTypeBySupplier(existReceipt.getSupplier().getSupplierId());
            model.addAttribute("productTypeList", productTypeList);
            return "admin-receipt-detail";
        }
        return "admin-receipt-detail";
    }


    @PostMapping("/deleteReceiptDetail")
    public String deleteReceiptDetail(@RequestParam("receiptDetailId") Long receiptDetailId,
                                      @RequestParam("receiptId") Long receiptId,
                                      RedirectAttributes redirectAttributes) {
        try {
            receiptDetailService.deleteByReceiptDetailId(receiptDetailId);
            redirectAttributes.addFlashAttribute("successMessage", "Receipt detail deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting receipt detail: " + e.getMessage());
        }
        return "redirect:/receiptDetail/" + receiptId;
    }





}
