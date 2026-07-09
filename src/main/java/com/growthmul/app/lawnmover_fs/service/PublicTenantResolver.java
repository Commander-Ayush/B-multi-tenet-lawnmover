package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * Every public (storefront) endpoint resolves its tenant through here.
 * Origin tells us which business is asking — it's not a secret, but it
 * doesn't need to be: this only gates access to data that's meant to be
 * publicly readable anyway (services, pricing, "submit a booking").
 * Admin routes never use this — they get companyId from the JWT instead
 * (see CurrentAdmin).
 */
@Component
public class PublicTenantResolver {

    @Autowired
    private CompanyRepo companyRepo;

    public Company resolve(String origin) {
        if (origin == null || origin.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Origin header");
        }
        String domain = origin.toLowerCase()
                .replaceFirst("^https?://", "")
                .replaceFirst("^www\\.", "")
                .replaceFirst("/$", "");

        System.out.println("Origin = '" + origin + "'");
        System.out.println("Domain = '" + domain + "'");

        Company company = companyRepo.findByDomain(domain).orElse(null);

        System.out.println("Company = " + company);

        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown business");
        }

        return company;
    }
}
