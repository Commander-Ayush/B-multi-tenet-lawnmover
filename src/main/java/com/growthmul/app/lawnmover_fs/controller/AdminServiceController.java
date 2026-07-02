package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.ServiceItemDto;
import com.growthmul.app.lawnmover_fs.dto.ServiceItemRequest;
import com.growthmul.app.lawnmover_fs.security.CurrentAdmin;
import com.growthmul.app.lawnmover_fs.service.ServiceCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/services")
public class AdminServiceController {

    @Autowired
    private ServiceCatalogService catalogService;

    @GetMapping
    public Map<String, List<ServiceItemDto>> getCatalog() {
        return catalogService.getCatalogForAdmin(CurrentAdmin.companyId());
    }

    @PostMapping
    public ServiceItemDto add(@RequestBody ServiceItemRequest req) {
        return catalogService.addItem(CurrentAdmin.companyId(), req);
    }

    @PutMapping("/{id}")
    public ServiceItemDto edit(@PathVariable Long id, @RequestBody ServiceItemRequest req) {
        return catalogService.editItem(CurrentAdmin.companyId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        catalogService.deleteItem(CurrentAdmin.companyId(), id);
    }
}
