package com.shopping.example.controller.thymleaf;


import com.shopping.example.entity.Voucher;
import com.shopping.example.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class VoucherController {

    @Autowired
    private VoucherService voucherService;



    @GetMapping("/createVoucher")
    public String createVoucher(Model model) {
        List<Voucher> listVouchers = voucherService.findAll();
        model.addAttribute("Vouchers", listVouchers);
        return "admin-voucher";
    }

    @PostMapping("/addVoucher")
    public String addVoucher(@RequestParam("code") String code,
                             @RequestParam("percentage") double percentage,
                             @RequestParam("quantity") int quantity,
                             @RequestParam("endDate")LocalDate endDate,
                             RedirectAttributes redirectAttributes, Model model) {
        Voucher voucher = new Voucher();


        if(code.isEmpty()){
            redirectAttributes.addFlashAttribute("addMessage", "Code cannot be empty ");
            return "redirect:/createVoucher";
        }
        List<Voucher> listVouchers = voucherService.findAll();
        for(Voucher v : listVouchers){
            if (code.equalsIgnoreCase(v.getVoucherCode())) {
                redirectAttributes.addFlashAttribute("addMessage", "Voucher already exist ");
                return "redirect:/createVoucher";
            }
        }

        voucher.setVoucherCode(code.trim());
        voucher.setPercentageDiscount(percentage);
        voucher.setQuantity(quantity);
        voucher.setCreateDate(LocalDate.now());
        if (endDate.isEqual(voucher.getCreateDate())) {
            redirectAttributes.addFlashAttribute("addMessage", " Must be availiable than 1 day ");
            return "redirect:/createVoucher";
        }

        voucher.setEndDate(endDate);
        voucher.setStatus("active");
        voucherService.save(voucher);
        redirectAttributes.addFlashAttribute("addMessage", "Voucher successfully added");
        return "redirect:/createVoucher";
    }


    @PostMapping("/changeVoucherStatus")
    public String changeVoucherStatus(@RequestParam("voucherId") Long id, RedirectAttributes redirectAttributes) {
        Optional<Voucher> optionalVoucher = voucherService.findById(id);
        if (optionalVoucher.isPresent()) {
            Voucher voucher = optionalVoucher.get();
            voucher.setStatus("inative");
            voucherService.save(voucher);
            redirectAttributes.addFlashAttribute("changeMessage", "Voucher successfully changed");
            return "redirect:/createVoucher";
        }
        redirectAttributes.addFlashAttribute("changeMessage", "Fail to changed");
        return "redirect:/createVoucher";
    }





}
