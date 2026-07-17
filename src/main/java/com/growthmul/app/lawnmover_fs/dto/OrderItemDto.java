package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.OrderItem;

public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productPrice;
    private int quantity;

    public static OrderItemDto from(OrderItem entity) {
        OrderItemDto dto = new OrderItemDto();
        dto.productId = entity.getProduct() != null ? entity.getProduct().getId() : null;
        dto.productName = entity.getProductName();
        dto.productPrice = entity.getProductPrice();
        dto.quantity = entity.getQuantity();
        return dto;
    }

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductPrice() { return productPrice; }
    public int getQuantity() { return quantity; }
}