package com.growthmul.app.lawnmover_fs.dto;

// Request body for POST /admin/products and PUT /admin/products/{id}.
// Field names/shape match submitProductForm() in admin-ui.js exactly:
// { name, brand, category, price, originalPrice, badge, spec,
//   description, image, inStock }
public class ProductRequest {
    private String name;
    private String brand;
    private String category;
    private String price;
    private String originalPrice;
    private String badge;
    private String spec;
    private String description;
    private String image;
    private boolean inStock;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(String originalPrice) { this.originalPrice = originalPrice; }
    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }
    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
}