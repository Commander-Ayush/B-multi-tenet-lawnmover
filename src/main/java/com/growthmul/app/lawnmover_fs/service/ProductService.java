package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.AdminProductDto;
import com.growthmul.app.lawnmover_fs.dto.ProductDto;
import com.growthmul.app.lawnmover_fs.dto.ProductRequest;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.entity.Product;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import com.growthmul.app.lawnmover_fs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepo;
    @Autowired private CompanyRepo companyRepo;
    @Autowired private PublicTenantResolver tenantResolver;

    // ───────────────────────── PUBLIC (storefront) ───────────────────────── //

    public List<ProductDto> getProducts(String origin) {
        Company company = tenantResolver.resolve(origin);
        return productRepo.findByCompanyIdOrderBySortOrder(company.getId())
                .stream().map(ProductDto::from).toList();
    }

    // ───────────────────────── ADMIN ───────────────────────── //

    public List<AdminProductDto> getProductsForAdmin(Long companyId) {
        return productRepo.findByCompanyIdOrderBySortOrder(companyId)
                .stream().map(AdminProductDto::from).toList();
    }

    public AdminProductDto addProduct(Long companyId, ProductRequest req) {
        Product entity = new Product();
        applyRequest(entity, req);
        entity.setCompany(companyRepo.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found")));
        return AdminProductDto.from(productRepo.save(entity));
    }

    public AdminProductDto editProduct(Long companyId, Long id, ProductRequest req) {
        Product entity = ownedOrThrow(companyId, id);
        applyRequest(entity, req);
        return AdminProductDto.from(productRepo.save(entity));
    }

    public void deleteProduct(Long companyId, Long id) {
        Product entity = ownedOrThrow(companyId, id);
        productRepo.delete(entity);
    }

    private void applyRequest(Product entity, ProductRequest req) {
        entity.setName(req.getName());
        entity.setBrand(req.getBrand());
        entity.setCategory(req.getCategory());
        entity.setPrice(req.getPrice());
        entity.setOriginalPrice(req.getOriginalPrice());
        entity.setBadge(req.getBadge());
        entity.setSpec(req.getSpec());
        entity.setDescription(req.getDescription());
        // Cloudinary upload happens client-side in the admin browser (unsigned
        // upload preset) — by the time this DTO arrives, `image` is already
        // the finished secure_url string, so we just store it like any other field.
        entity.setImage(req.getImage());
        // Admin UI only exposes a single in/out toggle (no "low-stock" input),
        // so that's the only transition this setter needs to make — see the
        // comment on AdminProductDto for why "low-stock" only ever originates
        // from data seeded/edited some other way, never from this form.
        entity.setStockStatus(req.isInStock() ? "in-stock" : "out-of-stock");
    }

    private Product ownedOrThrow(Long companyId, Long id) {
        Product entity = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (!entity.getCompany().getId().equals(companyId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your product");
        }
        return entity;
    }
}