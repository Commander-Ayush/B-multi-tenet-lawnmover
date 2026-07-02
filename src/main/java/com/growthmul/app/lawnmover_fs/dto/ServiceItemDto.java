package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.ServiceOffering;
import java.util.List;

public class ServiceItemDto {
    private Long id;
    private String icon;
    private String name;
    private String description;
    private String price;
    private boolean featured;
    private String type;
    private List<String> features;

    public static ServiceItemDto from(ServiceOffering entity) {
        ServiceItemDto dto = new ServiceItemDto();
        dto.id = entity.getId();
        dto.icon = entity.getIcon();
        dto.name = entity.getName();
        dto.description = entity.getDescription();
        dto.price = entity.getPrice();
        dto.featured = entity.isFeatured();
        dto.type = entity.getType();
        dto.features = entity.getFeatures();
        return dto;
    }

    public Long getId() { return id; }
    public String getIcon() { return icon; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public boolean isFeatured() { return featured; }
    public String getType() { return type; }
    public List<String> getFeatures() { return features; }
}
