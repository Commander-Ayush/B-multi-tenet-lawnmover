package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.Product;

// Same underlying Product as ProductDto, but shaped for the admin panel:
// inStock is a plain boolean (the toggle in products.html), not the
// richer in-stock/low-stock/out-of-stock string the public storefront
// consumes. "low-stock" collapses to true here since the admin UI has no
// third state to show it in.
public class AdminProductDto {
    private Long id;
    private String sku;
    private String name;
    private String brand;
    private String category;
    private String image;
    private String price;
    private String originalPrice;
    private String badge;
    private boolean inStock;
    private String spec;
    private String description;

    public static AdminProductDto from(Product entity) {
        AdminProductDto dto = new AdminProductDto();
        dto.id = entity.getId();
        dto.sku = entity.getSku();
        dto.name = entity.getName();
        dto.brand = entity.getBrand();
        dto.category = entity.getCategory();
        dto.image = entity.getImage();
        dto.price = entity.getPrice();
        dto.originalPrice = entity.getOriginalPrice();
        dto.badge = entity.getBadge();
        dto.inStock = !"out-of-stock".equals(entity.getStockStatus());
        dto.spec = entity.getSpec();
        dto.description = entity.getDescription();
        return dto;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public String getImage() { return image; }
    public String getPrice() { return price; }
    public String getOriginalPrice() { return originalPrice; }
    public String getBadge() { return badge; }
    public boolean isInStock() { return inStock; }
    public String getSpec() { return spec; }
    public String getDescription() { return description; }
}