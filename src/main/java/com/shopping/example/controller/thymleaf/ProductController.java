package com.shopping.example.controller.thymleaf;

import com.shopping.example.entity.*;

import com.shopping.example.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProductController {
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


    //Get method with url
    //Get method with url
    @GetMapping("/ListProduct")
    public String ListProduct(Model model,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "12") int size,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) Double minPrice,
                              @RequestParam(required = false) Double maxPrice,
                              @RequestParam(required = false) Long colorId,
                              @RequestParam(required = false) String memory,
                              @RequestParam(required = false) String ram,
                              @RequestParam(required = false) String minSize,
                              @RequestParam(required = false) String maxSize
    ) {
        if (ram != null && ram.isEmpty()) {
            ram = null;
        }
        if (minSize != null && minSize.isEmpty()) {
            minSize = null;
        }
        if (maxSize != null && maxSize.isEmpty()) {
            maxSize = null;
        }

        if (memory != null && memory.isEmpty()) {
            memory = null;
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> products = productService.filterProducts(name, categoryId, minPrice, maxPrice, colorId, memory, ram, minSize, maxSize, pageable);
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("colorList", colorService.getAllColors());
        model.addAttribute("ramList", productTechService.getAllProductRam());
        model.addAttribute("hasProducts", !products.isEmpty());
        model.addAttribute("memoryList", productTechService.getAllProductMemory());
        model.addAttribute("productTechList", productTechService.getAllProductTechs());

        return "shop-side-version-2";
    }



    @GetMapping("/filterProducts")
    public String filterProducts(@RequestParam("minPrice") double minPrice,
                                 @RequestParam("maxPrice") double maxPrice,
                                 @RequestParam(value = "color", required = false) List<String> colors,
                                 Model model) {
        List<Product> productList;
        productList = productService.getProductsByPriceRange(minPrice, maxPrice);
        model.addAttribute("products", productList);
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "shop-side-version-2";
    }



    //Get method with url
    @GetMapping("/ProductDetail/{id}")
    public String productDetail(Model model, @PathVariable long id) {
        Optional<Product> product = productService.getProductById(id);
        //if product not null
        if (product.isPresent()) {
            // Get the value of product and product type
            model.addAttribute("product", product.get());
            List<ProductType> listProductType = productTypeService.findByProduct(product.get());
            model.addAttribute("ListProductType", listProductType);
            List<Product> listRelateCategory = productService.getProductsByCategory(product.get().getCategory());
            model.addAttribute("ListRelate", listRelateCategory);
        }
        else{
            System.out.println("Nothing to show");
        }
        // return result to product-detail.html
        return "product-detail";
    }




    //Get method with url
    @GetMapping("/Category/{id}")
    public String Category(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "12") int size,
                           @PathVariable long id) {
        //List all the category
        model.addAttribute("categoryList", categoryService.findAll());
        //Find the category with id in the url
        Optional<Category> optionalCategory = categoryService.findById(id);
        if (optionalCategory.isPresent()) {
            //List all the product in that category
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productService.getProductsByCategory(optionalCategory.get(), pageable);
            model.addAttribute("products", products);
            model.addAttribute("page", page);
            model.addAttribute("totalPages", products.getTotalPages());
            model.addAttribute("categoryId", id);
            model.addAttribute("type", 1);
        }
        // return the result
        return "shop-side-version-2";
    }


    @PostMapping("/search")
    public String search(@RequestParam("name") String keyword, Model model) {
        List<Product> productList = productService.getProductsByName(keyword);
        model.addAttribute("products", productList);
        return "shop-side-version-2";
    }



    @GetMapping("/create")
    public String create(Model model) {
        List<Category> categoryList = categoryService.findAll();
        List<Brand> brandList =  brandService.findAll();
        List<Supplier> supplierList = supplierService.findAllSuppliers();
        model.addAttribute("supplierList", supplierList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("BrandList", brandList);
        return "admin-add-product";
    }


    @GetMapping("/createSupBraCate")
    public String createSupBra(Model model) {
        return "admin-add-supplier-brand";
    }

    @PostMapping("/createCategory")
    public String createCategory(@RequestParam("name") String name,RedirectAttributes redirectAttributes){
        Category newCategory = new Category();
        newCategory.setCategoryName(name);
        redirectAttributes.addFlashAttribute("successMessage", "Category added successfully");
        categoryService.save(newCategory);
        return "redirect:/createSupBraCate";
    }

    @PostMapping("/createPro")
    public String create(@RequestParam("name") String name,
                         @RequestParam("image") MultipartFile productImage,
                         @RequestParam("selectedBrand") Long brandId,
                         @RequestParam("selectedCate") Long cateId,
                         @RequestParam("selectedSupplier") Long supId,
                         @RequestParam("description") String description,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        Product newProduct = new Product();

        try {
            Category category = categoryService.findById(cateId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            Brand brand = brandService.findById(brandId)
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            Supplier supplier = supplierService.findSupplierById(supId)
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));

            newProduct.setCategory(category);
            newProduct.setBrand(brand);
            newProduct.setProductName(name);
            newProduct.setSupplier(supplier);
            newProduct.setProductDescription(description);

            if (!productImage.isEmpty()) {

                String fileName =productImage.getOriginalFilename();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

                saveProductImage(productImage, uniqueFileName);
                newProduct.setProductImage(uniqueFileName);
            }


            productService.addProduct(newProduct);


            redirectAttributes.addFlashAttribute("successMessage", "Product added successfully");
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add product: " + e.getMessage());
        }

        return "redirect:/create";
    }


    @PostMapping("/createBrand")
    public String createBrand(@RequestParam("name") String name,RedirectAttributes redirectAttributes){
        Brand newBrand = new Brand();
        newBrand.setBrandName(name);
        brandService.save(newBrand);
        redirectAttributes.addFlashAttribute("successMessage", "Brand added successfully");
        return "redirect:/createSupBraCate";

    }

    @PostMapping("/createTech")
    public String createTech(@RequestParam("Ram") int ram,@RequestParam("Memory") int memory,@RequestParam("Size") double size,RedirectAttributes redirectAttributes){
        ProductTech tech = new ProductTech();
        tech.setSize(size);
        tech.setRam(ram);
        tech.setMemory(memory);
        productTechService.saveProductTech(tech);
        redirectAttributes.addFlashAttribute("successMessage", "Product tech added successfully");
        return "redirect:/create";
    }







    //Create Supplier
    @PostMapping("/createSupplies")
    public String createSup(@RequestParam("name") String name,@RequestParam("address") String address, RedirectAttributes redirectAttributes){
        Supplier newSup = new Supplier();
        newSup.setSupplierAddress(address);
        newSup.setSupplierName(name);
        supplierService.saveSupplier(newSup);
        redirectAttributes.addFlashAttribute("successMessage", "Supplier added successfully");
        return "redirect:/createSupBra";
    }


    //get the product to the manager product
    @GetMapping("/update")
    public String update(Model model) {
        model.addAttribute("productList", productService.getAllProducts());
        return "manager-product";
    }






    // to get the value of product
    @GetMapping("/updateProduct/{proId}")
    public String updateProduct(Model model, @PathVariable long proId) {
        Optional<Product> product = productService.getProductById(proId);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("categoryList", categoryService.findAll());
            model.addAttribute("brandList", brandService.findAll());
            model.addAttribute("supplierList", supplierService.findAllSuppliers());
            model.addAttribute("colorList", colorService.getAllColors());
            model.addAttribute("TechList", productTechService.getAllProductTechs());
            List<ProductType>  listVariant = productTypeService.findByProduct(product.get());
            model.addAttribute("listVariant", listVariant);
        }
        return "admin-update-product";
    }



    @PostMapping("/updateProduct")
    public String updateProduct(@RequestParam Long proId,
                                @RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("image") MultipartFile productImage,
                                @RequestParam("selectedBrand") Long brandId,
                                @RequestParam("selectedCate") Long cateId,
                                RedirectAttributes redirectAttributes) {
        Optional<Product> optionalProduct = productService.getProductById(proId);
        if (optionalProduct.isPresent()) {
            Product existProduct = optionalProduct.get();
            existProduct.setProductName(name);
            existProduct.setProductDescription(description);

            // Xử lý file ảnh
            if (!productImage.isEmpty()) {
                // Xóa ảnh cũ nếu có
                String oldImagePath = existProduct.getProductImage();
                if (oldImagePath != null && !oldImagePath.isEmpty()) {
                    File oldImageFile = new File(oldImagePath);
                    if (oldImageFile.exists()) {
                        boolean deleted = oldImageFile.delete();
                        if (!deleted) {
                            logger.warn("Failed to delete old image: " + oldImagePath);
                        } else {
                            logger.info("Deleted old image: " + oldImagePath);
                        }
                    }
                }

                // Lưu ảnh mới
                String fileName = productImage.getOriginalFilename();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

                String filePath = saveProductImage(productImage, uniqueFileName);
                existProduct.setProductImage(uniqueFileName);
            }

            Category category = categoryService.findById(cateId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            Brand brand = brandService.findById(brandId)
                    .orElseThrow(() -> new RuntimeException("Brand not found"));

            existProduct.setCategory(category);
            existProduct.setBrand(brand);
            productService.addProduct(existProduct);

            redirectAttributes.addFlashAttribute("successMessage", "Update successful");
            return "redirect:/updateProduct/" + proId;
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Update not successful");
        return "redirect:/updateProduct/" + proId;
    }

    @PostMapping("/addProductType")
    public String addProductType(@RequestParam Long proId,
                                 @RequestParam("selectedTech") Long techId,
                                 @RequestParam("selectedColor") Long colorId,
                                 @RequestParam("price") double price,
                                 @RequestParam("quantity") int quantity,
                                 RedirectAttributes redirectAttributes) {
        Optional<Product> optionalProduct = productService.getProductById(proId);
        if (optionalProduct.isPresent()) {
            Product existProduct = optionalProduct.get();

            ProductType newProductType = new ProductType();
            newProductType.setProduct(existProduct);

            Optional<ProductTech> optionalTech = productTechService.getProductTechById(techId);
            ProductTech newProductTech = optionalTech.orElseThrow(() -> new RuntimeException("ProductTech not found"));
            newProductType.setProductTech(newProductTech);

            Optional<Color> optionalColor = colorService.getColorById(colorId);
            Color newColor = optionalColor.orElse(null);
            newProductType.setColor(newColor);

            newProductType.setProduct_type_price(price);
            newProductType.setProduct_type_quantity(quantity);

            productTypeService.saveProductType(newProductType);

            redirectAttributes.addFlashAttribute("successMessage", "Add ProductType successful");
            return "redirect:/updateProduct/" + proId;
        }
        redirectAttributes.addFlashAttribute("successMessage", "Add ProductType not successful");
        return "redirect:/updateProduct/" + proId;
    }





    @GetMapping("/shopsideversion2")
    public String ShowProduct(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size, Model model) {
        Page<Product> ProductListPage = productService.getProducts(page,size);
        model.addAttribute("productListPage", ProductListPage);
        model.addAttribute("page", ProductListPage.getNumber());
        model.addAttribute("totalPages", ProductListPage.getTotalPages());
        return "shop-side-version-2";
    }


    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private String saveProductImage(MultipartFile file, String fileName) {

        //Change the path after clone
        String uploadDir = "D:\\Study\\SWP391\\SWP391_Project_Final\\SWP391_project\\src\\main\\resources\\static\\product_img";
        File uploadDirFile = new File(uploadDir);

        // Kiểm tra và tạo thư mục nếu chưa tồn tại
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // Tạo đường dẫn đầy đủ của file ảnh
        File filePath = new File(uploadDirFile, fileName);
        try {
            Files.copy(file.getInputStream(), filePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Failed to save product image", e);
            throw new RuntimeException("Failed to save product image: " + e.getMessage());
        }
        return filePath.getAbsolutePath();
    }



    @PostMapping("/changeStatus")
    public String changeStatus(@RequestParam("typeId") Long typeId, RedirectAttributes redirectAttributes) {
        // Lấy thông tin ProductType từ typeId
        Optional<ProductType> optionalProductType = productTypeService.getProductTypeById(typeId);
        if (optionalProductType.isPresent()) {
            ProductType productType = optionalProductType.get();
            // Thay đổi trạng thái
            if ("close".toLowerCase().equals(productType.getProduct_type_status())) {
                productType.setProduct_type_status("active");
            } else if ("active".toLowerCase().equals(productType.getProduct_type_status())) {
                productType.setProduct_type_status("close");
            }
            // Lưu lại thay đổi
            productTypeService.saveProductType(productType);
            redirectAttributes.addFlashAttribute("successMessage", "Status changed successfully");
            return "redirect:/updateProduct/" + productType.getProduct().getProductId();
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "ProductType not found");
            return null;
        }

    }


}




























