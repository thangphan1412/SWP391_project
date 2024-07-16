package com.shopping.example.controller.thymleaf;

import com.shopping.example.entity.*;
import com.shopping.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("employee")
public class StaffController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleService roleService;

    // show sreec for employee
    @GetMapping("/employees")
    public String showScreen(){
        return "admin-manager";
    }

    // view customer from to employee
    @GetMapping("/viewCustomer")
    public String viewListCustomer(Model model){
        model.addAttribute("accountList", accountService.findAllByRole("ROLE_USER"));
        return "staff-page-viewCustomers";
    }

    // view customer detail
    @GetMapping("/customerDetail/{id}")
    public String getCustomer(@PathVariable Long id, Model model){
        Optional<Customer> customer = customerService.findCustomerById(id);

        if (customer.isPresent()){
            model.addAttribute("customerDetail", customer.get());

        } else {
            model.addAttribute("errorcustomer", "customer not found");
        }
        return "customers-detail";
    }

    //search customer
    @PostMapping("/search-customer")
    public String searchCustomer(@RequestParam("name") String email, Model model){
        List<Account> accountList = accountService.searchAccount(email);
        if (accountList.isEmpty()) {
            model.addAttribute("searchMessage", "No customers found with the email \"" + email + "\".");
        }
        model.addAttribute("accountList", accountList);
        return "staff-page-viewCustomers";
    }


    // view brand detail from to employees
    @GetMapping("/brandDetail/{id}")
    public String getBrand(@PathVariable Long id, Model model){
        Optional<Brand> brand = brandService.findById(id);
        if(brand.isPresent()){
            model.addAttribute("brandDetail", brand.get());
        } else {
            model.addAttribute("error brand", "brand not found");
        }
        return "brands-detail";
    }

    // view suppliers detail for employees
    @GetMapping("/suppliers/{id}")
    public String getSuppliers(@PathVariable Long id, Model model){
        Optional<Supplier> supplier = supplierService.findSupplierById(id);
        if (supplier.isPresent()){
            model.addAttribute("supplierDetail", supplier.get());
            List<Product> products = productService.findBySuppliers(supplier.get());
            model.addAttribute("listProducts", products);

        } else {
            model.addAttribute("error supplier", "supplier not found");
        }
        return "suppliers-detail";
    }

    // view brand product from to employee
    @GetMapping("/viewBrand")
    public String viewBrand(Model model){
        List <Brand> allBrand = brandService.findAll();
        Set<Brand> uniqueBrand = new HashSet<>(allBrand);
        List<Brand> uniqueBrandList = uniqueBrand.stream().collect(Collectors.toList());
        model.addAttribute("brands", uniqueBrandList);
        return "brand-list";
    }

    // view supppliers product form employee
    @GetMapping("/viewSuppliers")
    public String viewSuppliers(Model model){
        model.addAttribute("suppliers", supplierService.findAllSuppliers());
        return "suppliers-list";
    }


    // view order list
    @GetMapping("/viewOrder")
    public String viewOrderList(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "order-list";
    }

    // view order detail of one product
    @GetMapping("/viewOrderDetail/{id}")
    public String viewOrderDetail(Model model, @PathVariable("id")Long id){
        Order order = orderService.getOrderById(id);
        List<OrderDetail> orderDetailsList = orderDetailService.getOrderDetailsByOrder(order);
        double totalPrice = orderDetailsList.stream()
                .mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                .sum();
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("order", order);
        model.addAttribute("ListOrderDetail", orderDetailsList);
        return "order-detail";
    }


    @GetMapping("/editOrder/{id}")
    public String viewEditOrder(Model model, @PathVariable("id")Long id){
        Optional<Order> order = Optional.ofNullable(orderService.getOrderById(id));
        if(order.isPresent()){
            model.addAttribute("orderDetails", order.get());
        } else {
            System.out.println("Nothing to show");
        }

        return "editOrder";
    }


    // edit status order detail by status
    @PostMapping("/updateOrder")
    public String updateOrder(@RequestParam(name = "id") Long id, @RequestParam(name = "status") String status, RedirectAttributes redirectAttributes){
        Order existOrder = orderService.getOrderById(id);
        if(existOrder != null){
            if (status.equalsIgnoreCase("Delivered")){
                existOrder.setOrderStatus(status);
                existOrder.setApprovalDate(LocalDate.now());
            }
            existOrder.setOrderStatus(status);
            orderService.save(existOrder);
            redirectAttributes.addFlashAttribute("message", "Order updated successfully");
            return "redirect:/viewOrder";
        }
        redirectAttributes.addFlashAttribute("message", "Order updated fail");
        return "redirect:/viewOrder";
    }


    @GetMapping("/customerOrder/{id}")
    public String viewCustomerOrder(@PathVariable Long id, Model model){
        Optional<Customer> customer = customerService.findCustomerById(id);
        if(customer.isPresent()){
            Customer existCustomer = customer.get();
            List<Order> orders = orderService.getOrdersByCustomer(existCustomer);
            model.addAttribute("orders", orders);
            model.addAttribute("customer", existCustomer);
            return "customer-order";
        }
        return "customer-order";
    }


    @GetMapping("/viewAllAccount")
    public String viewAllAccount(Model model){
        Account currentAccount = accountService.getCurrentAccount();
        List<Account> accountList = accountService.getAllAccountsWithRoles();
        accountList.removeIf(account -> account.getId().equals(currentAccount.getId()));
        model.addAttribute("accounts", accountList);
        model.addAttribute("roleList", roleService.findAll() );
        return "view-all-account";
    }


    @PostMapping("/replaceRole")
    public String replaceRole(@RequestParam(name = "id") Long id, @RequestParam(name = "roleId") Long roleId, RedirectAttributes redirectAttributes){
        accountService.replaceRoleAccount(id,roleId);
        redirectAttributes.addFlashAttribute("roleMessage", "Role updated successfully");
        return "redirect:/viewAllAccount";

    }


    @GetMapping("/employeeDetail/{id}")
    public String viewEmployeeDetail(@PathVariable Long id, Model model){
        Optional<Employee> optionalEmployee = employeeService.findById(id);
        if (optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            model.addAttribute("employee", employee);
        }
        return "employee-detail";
    }



    @PostMapping("/updateEmployee")
    public String updateEmployee(@RequestParam("id") Long id,
                                 @RequestParam("name") String name,
                                 @RequestParam("birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("addressDetail") String addressDetail,
                                 @RequestParam("city") String city,
                                 @RequestParam("district") String district,
                                 @RequestParam("ward") String ward,
                                 RedirectAttributes redirectAttributes) {
        Optional<Employee> optionalEmployee = employeeService.findById(id);
        if (name.isBlank() || addressDetail.isBlank() || city.isBlank() || district.isBlank() || ward.isBlank()) {
            redirectAttributes.addFlashAttribute("updateMessage", "Can't not update");
            return "redirect:/employeeDetail/" + id;
        }
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setName(name.trim());
            employee.setBirthday(date);
            employee.setPhone(phone);
            employee.setAddressDetail(addressDetail.trim());
            employee.setCity(city.trim());
            employee.setDistrict(district.trim());
            employee.setWard(ward.trim());
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("updateMessage", "Update Successful");
        }
        return "redirect:/employeeDetail/" + id;
    }














}
