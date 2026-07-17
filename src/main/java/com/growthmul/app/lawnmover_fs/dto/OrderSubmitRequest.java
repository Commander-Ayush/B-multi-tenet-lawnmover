package com.growthmul.app.lawnmover_fs.dto;

import java.time.LocalDate;
import java.util.List;

// Body for POST /orders (public, tenant resolved via Origin header):
// { firstName, lastName, email, phone, fulfillment, address,
//   preferredDate, notes, items: [ { productId, quantity } ] }
public class OrderSubmitRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String fulfillment; // "pickup" | "delivery"
    private String address;
    private LocalDate preferredDate;
    private String notes;
    private List<OrderItemRequest> items;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getFulfillment() { return fulfillment; }
    public void setFulfillment(String fulfillment) { this.fulfillment = fulfillment; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDate getPreferredDate() { return preferredDate; }
    public void setPreferredDate(LocalDate preferredDate) { this.preferredDate = preferredDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}