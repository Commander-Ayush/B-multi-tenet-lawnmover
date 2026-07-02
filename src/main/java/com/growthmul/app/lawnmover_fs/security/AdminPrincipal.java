package com.growthmul.app.lawnmover_fs.security;

/** What ends up as the Authentication principal once a JWT validates. */
public class AdminPrincipal {
    private final Long companyId;
    private final String email;

    public AdminPrincipal(Long companyId, String email) {
        this.companyId = companyId;
        this.email = email;
    }

    public Long getCompanyId() { return companyId; }
    public String getEmail() { return email; }
}
