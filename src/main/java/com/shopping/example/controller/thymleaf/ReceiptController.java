package com.shopping.example.controller.thymleaf;

import com.shopping.example.entity.*;
import com.shopping.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.time.LocalDate;
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
            redirectAttributes.addFlashAttribute("delMessage", "Receipt detail deleted successfully");
            return "redirect:/receiptDetail/" + receiptId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("delMessage", "Error deleting receipt detail: " + e.getMessage());
            return "redirect:/receiptDetail/" + receiptId;
        }
    }


    @PostMapping("/updateReceiptPrice")
    public String updateReceiptPrice(@RequestParam("receiptDetailId") Long receiptDetailId, @RequestParam("receiptId") Long receiptId, @RequestParam("price") double price, RedirectAttributes redirectAttributes) {
        Optional<ReceiptDetail> receiptDetailOptional = receiptDetailService.getReceiptDetail(receiptDetailId);
        if (receiptDetailOptional.isPresent()) {
            ReceiptDetail existReceiptDetail = receiptDetailOptional.get();
            existReceiptDetail.setPrice(price);
            receiptDetailService.saveReceiptDetail(existReceiptDetail);
            redirectAttributes.addFlashAttribute("delMessage", "Update price successfully");
            return "redirect:/receiptDetail/" + receiptId;
        }
        redirectAttributes.addFlashAttribute("delMessage", "Update price fail");
        return "redirect:/receiptDetail/" + receiptId;
    }


    @PostMapping("/updateReceiptQuantity")
    public String updateReceiptPrice(@RequestParam("receiptDetailId") Long receiptDetailId, @RequestParam("receiptId") Long receiptId, @RequestParam("quantity") int quantity, RedirectAttributes redirectAttributes) {
        Optional<ReceiptDetail> receiptDetailOptional = receiptDetailService.getReceiptDetail(receiptDetailId);
        if (receiptDetailOptional.isPresent()) {
            ReceiptDetail existReceiptDetail = receiptDetailOptional.get();
            existReceiptDetail.setQuantity(quantity);
            receiptDetailService.saveReceiptDetail(existReceiptDetail);
            redirectAttributes.addFlashAttribute("delMessage", "Update quantity successfully");
            return "redirect:/receiptDetail/" + receiptId;
        }
        redirectAttributes.addFlashAttribute("delMessage", "Update quantity fail");
        return "redirect:/receiptDetail/" + receiptId;
    }


    @PostMapping("/addToReceipt")
    public String addToReceipt(@RequestParam("receiptId") Long receiptId, @RequestParam("productTypeId") Long productTypeId, RedirectAttributes redirectAttributes) {
        Optional<ProductType> productTypeOptional = productTypeService.getProductTypeById(productTypeId);
        Optional<Receipt> receiptOptional = receiptService.getReceipt(receiptId);

        if (productTypeOptional.isPresent() && receiptOptional.isPresent()) {
            ProductType productType = productTypeOptional.get();
            Receipt existReceipt = receiptOptional.get();

            // Kiểm tra xem sản phẩm đã tồn tại trong danh sách chi tiết của receipt chưa
            List<ReceiptDetail> existReceiptDetailList = receiptDetailService.findAllbyReceiptId(existReceipt.getReceiptId());
            boolean found = false;
            for (ReceiptDetail receiptDetail : existReceiptDetailList) {
                if (receiptDetail.getProductTypes().getProduct_type_id().equals(productType.getProduct_type_id())) {
                    // Nếu sản phẩm đã tồn tại, in ra thông báo và kết thúc vòng lặp
                    redirectAttributes.addFlashAttribute("delMessage", "Product type already exists in receipt");
                    found = true;
                    break;
                }
            }

            // Nếu sản phẩm chưa tồn tại trong danh sách chi tiết, tạo mới ReceiptDetail và thêm vào
            if (!found) {
                ReceiptDetail newReceiptDetail = new ReceiptDetail();
                newReceiptDetail.setProductTypes(productType);
                newReceiptDetail.setReceipt(existReceipt);
                newReceiptDetail.setQuantity(1);
                newReceiptDetail.setPrice(productType.getProduct_type_price());
                receiptDetailService.saveReceiptDetail(newReceiptDetail);
                redirectAttributes.addFlashAttribute("delMessage", "Added new product type to receipt");
            }
            return "redirect:/receiptDetail/" + receiptId;
        }
        return "redirect:/receiptDetail/" + receiptId;
    }
}


