package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String fulfillment;
    private String address;
    private LocalDate preferredDate;
    private LocalDateTime submittedAt;
    private boolean completed;
    private List<OrderItemDto> items;

    public static OrderDto from(Order entity) {
        OrderDto dto = new OrderDto();
        dto.id = entity.getId();
        dto.firstName = entity.getFirstName();
        dto.lastName = entity.getLastName();
        dto.email = entity.getEmail();
        dto.phone = entity.getPhone();
        dto.fulfillment = entity.getFulfillment();
        dto.address = entity.getAddress();
        dto.preferredDate = entity.getPreferredDate();
        dto.submittedAt = entity.getSubmittedAt();
        dto.completed = entity.isCompleted();
        dto.items = entity.getItems().stream().map(OrderItemDto::from).toList();
        return dto;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getFulfillment() { return fulfillment; }
    public String getAddress() { return address; }
    public LocalDate getPreferredDate() { return preferredDate; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public boolean isCompleted() { return completed; }
    public List<OrderItemDto> getItems() { return items; }
}