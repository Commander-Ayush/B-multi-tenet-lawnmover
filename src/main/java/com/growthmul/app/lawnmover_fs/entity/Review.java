package com.growthmul.app.lawnmover_fs.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewerName;
    private String reviewerCity;

    // 1–5, validated in the service layer
    private int stars;

    @Column(length = 2000)
    private String text;

    // Starts false — visible on the public storefront only after the admin
    // approves it. Protects clients from spam/competitor reviews going live
    // the moment they're submitted.
    private boolean approved = false;

    @CreationTimestamp
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
