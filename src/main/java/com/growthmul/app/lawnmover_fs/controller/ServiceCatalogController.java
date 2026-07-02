package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.ServiceDto;
import com.growthmul.app.lawnmover_fs.service.ServiceCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServiceCatalogController {

    @Autowired
    private ServiceCatalogService catalogService;

    @GetMapping("/services")
    public List<ServiceDto> getServices(@RequestHeader(value = "Origin", required = false) String origin) {
        return catalogService.getServices(origin);
    }

    @GetMapping("/services/plans")
    public List<ServiceDto> getPlans(@RequestHeader(value = "Origin", required = false) String origin) {
        return catalogService.getPlans(origin);
    }

    @GetMapping("/services/addons")
    public List<ServiceDto> getAddons(@RequestHeader(value = "Origin", required = false) String origin) {
        return catalogService.getAddons(origin);
    }
}
