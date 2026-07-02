package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.CompanyDto;
import com.growthmul.app.lawnmover_fs.service.PublicTenantResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    @Autowired
    private PublicTenantResolver tenantResolver;

    @GetMapping("/company")
    public CompanyDto getCompany(@RequestHeader(value = "Origin", required = false) String origin) {
        return CompanyDto.from(tenantResolver.resolve(origin));
    }
}
