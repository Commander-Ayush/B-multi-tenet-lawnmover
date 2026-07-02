package com.growthmul.app.lawnmover_fs.dto;

import java.util.List;

// Request body for POST /admin/services and PUT /admin/services/{id}.
// Plain setters/getters (not a Lombok @Data entity) since this is a wire
// shape, not something JPA ever touches directly.
public class ServiceItemRequest {
    private String icon;
    private String name;
    private String description;
    private String price;
    private boolean featured;
    private String type; // "service" | "plan" | "addon"
    private List<String> features;

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
}
