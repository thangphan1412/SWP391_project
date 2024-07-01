package com.shopping.example.controller.thymleaf;

import com.shopping.example.entity.Account;
import com.shopping.example.entity.Employee;
import com.shopping.example.entity.Receipt;
import com.shopping.example.entity.Supplier;
import com.shopping.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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

//    @PostMapping("viewPro")
//    public String viewPro(Model model, @RequestParam("supId") String supId) {
//        model.addAttribute("supplierList", supplierService.findAllSuppliers());
//
//        if (!"All".equals(supId)) {
//            Long supplierId = Long.parseLong(supId);
//            model.addAttribute("proTypeList", productTypeService.getProductTypeBySupplier(supplierId));
//            model.addAttribute("selectedSupId", supplierId);
//        } else {
//            model.addAttribute("proTypeList", productTypeService.getProductTypeByAll());
//            model.addAttribute("selectedSupId", null);
//        }
//
//        return "admin-receipt";
//    }





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



}
