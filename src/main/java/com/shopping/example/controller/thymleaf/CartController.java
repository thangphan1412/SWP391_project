package com.shopping.example.controller.thymleaf;

import com.shopping.example.entity.*;
import com.shopping.example.service.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class CartController {

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



    @PostMapping("/addToCart")
    public String addToCart(Model model, RedirectAttributes redirectAttributes, Account currentAccount,
                            @RequestParam("productType") Long productTypeId, @RequestParam("quantity") int quantity) {
        currentAccount = accountService.getCurrentAccount();
        if (currentAccount == null) {
            return "redirect:/login";
        } else {
            Customer customer = currentAccount.getCustomer();
            if (customer != null) {
                // Lấy giỏ hàng của khách hàng, nếu không có thì tạo mới
                Cart cart = cartService.getCartByCustomer(customer);
                if (cart == null) {
                    cart = new Cart();
                    cart.setCustomer(customer);
                    cartService.save(cart);
                }

                // Lấy thông tin sản phẩm từ productTypeId
                Optional<ProductType> optionalProductType = productTypeService.getProductTypeById(productTypeId);
                if (optionalProductType.isPresent()) {
                    ProductType productType = optionalProductType.get();

                    // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
                    CartItems existingCartItem = cartItemsService.getCartItemByProductType(productType);

                    if (existingCartItem != null) {
                        // Nếu sản phẩm đã tồn tại trong giỏ hàng, cập nhật số lượng
                        int newQuantity = existingCartItem.getQuantity() + quantity;
                        existingCartItem.setQuantity(newQuantity);
                        cartItemsService.save(existingCartItem);
                    } else {
                        // Nếu sản phẩm chưa tồn tại trong giỏ hàng, tạo mới và thêm vào giỏ hàng
                        CartItems newCartItem = new CartItems();
                        newCartItem.setCart(cart);
                        newCartItem.setProductType(productType);
                        newCartItem.setQuantity(quantity);
                        cartItemsService.save(newCartItem);
                    }
                }
                return "redirect:/viewCart";
            }
        }
        return "redirect:/login";
    }



        @GetMapping("/viewCart")
        public String cart (Model model, Account currentAccount){
            currentAccount = accountService.getCurrentAccount();
            if (currentAccount == null) {
                return "redirect:/login";
            } else {
                Customer customer = currentAccount.getCustomer();
                Cart customerCart = cartService.getCartByCustomer(customer);
                List<CartItems> listCartItems = cartItemsService.findByCartId(customerCart.getCartId());
                model.addAttribute("ListCart", listCartItems);
                model.addAttribute("MiniCartItems", listCartItems);

                return "cart";
            }
        }



//        @PostMapping("/deleteCartItem")
//        public String deleteIte(@RequestParam("cartItemId") Long id, RedirectAttributes redirectAttributes){
//            cartItemsService.delete(id);
//            return "redirect:/viewCart";
//        }


    @PostMapping("/deleteSelectedCartItems")
    public String deleteSelectedCartItems(@RequestParam(required = false) List<Long> selectedCartItems, Model model, RedirectAttributes redirectAttributes) {
        if (selectedCartItems == null || selectedCartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("delMessage", "No items selected for deletion");
            return "redirect:/viewCart";
        }

        for (Long cartItemId : selectedCartItems) {
            cartItemsService.delete(cartItemId);
        }
        Account currentAccount = accountService.getCurrentAccount();
        Customer existCustomer = currentAccount.getCustomer();
        if (existCustomer != null) {
            Customer customer = currentAccount.getCustomer();
            Cart customerCart = cartService.getCartByCustomer(customer);
            List<CartItems> listCartItems = cartItemsService.findByCartId(customerCart.getCartId());
            model.addAttribute("ListCart", listCartItems);
            redirectAttributes.addFlashAttribute("delMessage", "Delete successfully");
            return  "redirect:/viewCart";
        }
        redirectAttributes.addFlashAttribute("delMessage", "Delete fail");
        return "redirect:/viewCart";
    }
    @PostMapping("/updateCartItem")
    @ResponseBody
    public String updateCartItemQuantity(@RequestParam("cartItemId") Long cartItemId, @RequestParam("quantity") int quantity) {
        Optional<CartItems> optionalCartItem = cartItemsService.findById(cartItemId);

        if (optionalCartItem.isPresent()) {
            CartItems cartItem = optionalCartItem.get();
            cartItem.setQuantity(quantity);
            cartItemsService.save(cartItem);
            return "success";
        } else {
            return "error";
        }
    }
}

