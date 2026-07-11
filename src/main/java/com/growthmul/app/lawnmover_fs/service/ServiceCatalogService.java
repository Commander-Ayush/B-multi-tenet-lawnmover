package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.ServiceDto;
import com.growthmul.app.lawnmover_fs.dto.ServiceItemDto;
import com.growthmul.app.lawnmover_fs.dto.ServiceItemRequest;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.entity.ServiceOffering;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import com.growthmul.app.lawnmover_fs.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceCatalogService {

    @Autowired private ServiceRepository serviceRepo;
    @Autowired private CompanyRepo companyRepo;
    @Autowired private PublicTenantResolver tenantResolver;

    // ───────────────────────── PUBLIC (storefront) ───────────────────────── //

    public List<ServiceDto> getServices(String origin) {
        return fetchOrSeed(tenantResolver.resolve(origin), "service").stream().map(ServiceDto::from).toList();
    }

    public List<ServiceDto> getPlans(String origin) {
        return fetchOrSeed(tenantResolver.resolve(origin), "plan").stream().map(ServiceDto::from).toList();
    }

    public List<ServiceDto> getAddons(String origin) {
        return fetchOrSeed(tenantResolver.resolve(origin), "addon").stream().map(ServiceDto::from).toList();
    }

    private List<ServiceOffering> fetchOrSeed(Company company, String type) {
        List<ServiceOffering> existing = serviceRepo.findByCompanyIdAndTypeOrderBySortOrder(company.getId(), type);
        if (!existing.isEmpty()) return existing;

        List<ServiceOffering> defaults = switch (type) {
            case "plan" -> defaultPlans(company);
            case "addon" -> defaultAddons(company);
            default -> defaultServices(company);
        };
        return serviceRepo.saveAll(defaults);
    }

    private List<ServiceOffering> defaultServices(Company company) {
        return List.of(
                svc(company, "🌿", "Lawn Mowing", "Weekly or bi-weekly mowing, edging along driveways and walkways, and full cleanup of all clippings.", "Starting at $45 / visit", "service", 1, false, null),
                svc(company, "✂️", "Trimming & Edging", "Crisp edges along every fence, flower bed, and curb.", "Starting at $35 / visit", "service", 2, false, null),
                svc(company, "🍂", "Seasonal Cleanup", "Spring and fall deep cleans — leaf removal, debris hauling, and bed prep.", "Starting at $120 / session", "service", 3, false, null),
                svc(company, "💧", "Irrigation Check", "Sprinkler inspections, head adjustments, and timer programming.", "Starting at $80 / visit", "service", 4, false, null),
                svc(company, "🌱", "Overseeding & Fertilizing", "Thicker, greener grass starts with the right soil treatment.", "Starting at $95 / treatment", "service", 5, false, null),
                svc(company, "🏡", "Yard Debris Removal", "Branches, clippings, and organic debris — hauled away cleanly.", "Starting at $65 / visit", "service", 6, false, null)
        );
    }

    private List<ServiceOffering> defaultPlans(Company company) {
        return List.of(
                svc(company, "", "Basic", "Perfect for small yards", "$79", "plan", 1, false,
                        List.of("Bi-weekly mowing", "Edging & blowing", "Clipping cleanup", "Email support")),
                svc(company, "", "Standard", "Our most popular plan", "$139", "plan", 2, true,
                        List.of("Weekly mowing", "Edging & trimming", "Monthly fertilizing", "Seasonal cleanup (x2)", "Priority scheduling", "Phone & email support")),
                svc(company, "", "Premium", "Full-service yard care", "$199", "plan", 3, false,
                        List.of("Everything in Standard", "Irrigation check (monthly)", "Overseeding (seasonal)", "Debris removal included", "Dedicated account manager", "Same-day service calls"))
        );
    }

    private List<ServiceOffering> defaultAddons(Company company) {
        return List.of(
                svc(company, "🌸", "Flower Bed Weeding", "Hand-pull weeds from all flower beds and mulched areas.", "From $40 / visit", "addon", 1, false, null),
                svc(company, "🪵", "Fresh Mulching", "Refresh mulch in beds to retain moisture and improve curb appeal.", "From $85 / yard", "addon", 2, false, null),
                svc(company, "🌳", "Tree & Shrub Trimming", "Shape hedges, shrubs, and small trees for a clean, manicured look.", "From $60 / session", "addon", 3, false, null),
                svc(company, "🧹", "Driveway & Path Edging", "Define clean borders between lawn and hardscapes.", "From $30 / visit", "addon", 4, false, null)
        );
    }

    private ServiceOffering svc(Company company, String icon, String name, String desc, String price,
                                 String type, int order, boolean featured, List<String> features) {
        ServiceOffering s = new ServiceOffering();
        s.setCompany(company);
        s.setIcon(icon);
        s.setName(name);
        s.setDescription(desc);
        s.setPrice(price);
        s.setType(type);
        s.setSortOrder(order);
        s.setFeatured(featured);
        if (features != null) s.setFeatures(features);
        return s;
    }

    // ───────────────────────── ADMIN ─────────────────────────

    public Map<String, List<ServiceItemDto>> getCatalogForAdmin(Long companyId) {
        Map<String, List<ServiceItemDto>> result = new LinkedHashMap<>();
        result.put("services", toDto(serviceRepo.findByCompanyIdAndTypeOrderBySortOrder(companyId, "service")));
        result.put("plans", toDto(serviceRepo.findByCompanyIdAndTypeOrderBySortOrder(companyId, "plan")));
        result.put("addons", toDto(serviceRepo.findByCompanyIdAndTypeOrderBySortOrder(companyId, "addon")));
        return result;
    }

    private List<ServiceItemDto> toDto(List<ServiceOffering> list) {
        return list.stream().map(ServiceItemDto::from).toList();
    }

    public ServiceItemDto addItem(Long companyId, ServiceItemRequest req) {
        ServiceOffering entity = new ServiceOffering();
        applyRequest(entity, req);
        entity.setCompany(companyRepo.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found")));
        return ServiceItemDto.from(serviceRepo.save(entity));
    }

    public ServiceItemDto editItem(Long companyId, Long itemId, ServiceItemRequest req) {
        ServiceOffering entity = ownedOrThrow(companyId, itemId);
        applyRequest(entity, req);
        return ServiceItemDto.from(serviceRepo.save(entity));
    }

    public void deleteItem(Long companyId, Long itemId) {
        ServiceOffering entity = ownedOrThrow(companyId, itemId);
        serviceRepo.delete(entity);
    }

    private void applyRequest(ServiceOffering entity, ServiceItemRequest req) {
        entity.setIcon(req.getIcon());
        entity.setName(req.getName());
        entity.setDescription(req.getDescription());
        entity.setPrice(req.getPrice());
        entity.setFeatured(req.isFeatured());
        entity.setType(req.getType());
        if (req.getFeatures() != null) entity.setFeatures(req.getFeatures());
    }

    private ServiceOffering ownedOrThrow(Long companyId, Long itemId) {
        ServiceOffering entity = serviceRepo.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));
        if (!entity.getCompany().getId().equals(companyId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your service");
        }
        return entity;
    }
}
