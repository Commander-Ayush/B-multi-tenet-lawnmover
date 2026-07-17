package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.Product;

public class ProductDto {
    private Long id;
    private String sku;
    private String name;
    private String brand;
    private String category;
    private String image;
    private String price;
    private String originalPrice;
    private String badge;
    private String stock; // in-stock | low-stock | out-of-stock
    private String spec;
    private String description;

    public static ProductDto from(Product entity) {
        ProductDto dto = new ProductDto();
        dto.id = entity.getId();
        dto.sku = entity.getSku();
        dto.name = entity.getName();
        dto.brand = entity.getBrand();
        dto.category = entity.getCategory();
        dto.image = entity.getImage();
        dto.price = entity.getPrice();
        dto.originalPrice = entity.getOriginalPrice();
        dto.badge = entity.getBadge();
        dto.stock = entity.getStockStatus();
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
    public String getStock() { return stock; }
    public String getSpec() { return spec; }
    public String getDescription() { return description; }
}