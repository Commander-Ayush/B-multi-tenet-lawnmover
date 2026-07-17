package com.growthmul.app.lawnmover_fs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    // Snapshotted at order time — so the admin panel still shows a sane
    // name/price on old orders even if the product is later renamed,
    // re-priced, or deleted entirely.
    private String productName;
    private String productPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Nullable on purpose: if the product is deleted later, the order
    // (and its snapshot above) should still exist and still display fine.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}