package com.growthmul.app.lawnmover_fs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String name;
    private String brand;

    // One of: push-mower, riding-mower, robotic-mower, trimmer, blower, accessory
    // (the storefront builds its filter tabs from whichever of these are
    // actually present — see API contract)
    private String category;

    // Cloudinary secure_url, uploaded unsigned straight from the admin
    // browser — the backend never touches the image bytes, just stores
    // the resulting link like any oth  er string field.
    @Column(length = 1000)
    private String image;

    private String price;
    private String originalPrice;

    // Free text badge — "Best Seller" / "New" / "Sale" / null. Only "Sale"
    // and "New" get special styling client-side; anything else is a plain
    // dark badge, so no validation needed here.
    private String badge;

    @Column(length = 300)
    private String spec;

    @Column(length = 2000)
    private String description;

    private String stockStatus = "in-stock";

    private int sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}