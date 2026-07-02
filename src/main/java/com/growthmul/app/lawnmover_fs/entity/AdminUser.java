package com.growthmul.app.lawnmover_fs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admin_users")
@Data
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Login identifier — globally unique across ALL tenants, so login
    // never needs to know which company an admin belongs to up front.
    @Column(unique = true, nullable = false)
    private String email;

    // BCrypt hash, never plaintext.
    @Column(nullable = false)
    private String passwordHash;

    // This is how we know which business this admin manages — the JWT
    // issued at login carries this company's id as a claim, and every
    // /admin/** request after that is scoped to it.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
