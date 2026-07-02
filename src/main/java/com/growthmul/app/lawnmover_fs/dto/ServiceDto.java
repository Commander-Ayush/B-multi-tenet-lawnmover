package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.ServiceOffering;
import java.util.List;

public class ServiceDto {
    private Long id;
    private String icon;
    private String name;
    private String description;
    private String price;
    private boolean featured;
    private List<String> features;

    public static ServiceDto from(ServiceOffering entity) {
        ServiceDto dto = new ServiceDto();
        dto.id = entity.getId();
        dto.icon = entity.getIcon();
        dto.name = entity.getName();
        dto.description = entity.getDescription();
        dto.price = entity.getPrice();
        dto.featured = entity.isFeatured();
        dto.features = entity.getFeatures();
        return dto;
    }

    public Long getId() { return id; }
    public String getIcon() { return icon; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public boolean isFeatured() { return featured; }
    public List<String> getFeatures() { return features; }
}
