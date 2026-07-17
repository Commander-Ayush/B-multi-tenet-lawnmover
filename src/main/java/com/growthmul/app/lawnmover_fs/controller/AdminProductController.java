package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.AdminProductDto;
import com.growthmul.app.lawnmover_fs.dto.ProductRequest;
import com.growthmul.app.lawnmover_fs.security.CurrentAdmin;
import com.growthmul.app.lawnmover_fs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<AdminProductDto> getProducts() {
        return productService.getProductsForAdmin(CurrentAdmin.companyId());
    }

    // Body's `image` field is the Cloudinary secure_url the admin browser
    // already got back from its own unsigned upload — this endpoint never
    // touches raw image bytes, it just persists the finished link.
    @PostMapping
    public AdminProductDto add(@RequestBody ProductRequest req) {
        return productService.addProduct(CurrentAdmin.companyId(), req);
    }

    @PutMapping("/{id}")
    public AdminProductDto edit(@PathVariable Long id, @RequestBody ProductRequest req) {
        return productService.editProduct(CurrentAdmin.companyId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(CurrentAdmin.companyId(), id);
    }
}