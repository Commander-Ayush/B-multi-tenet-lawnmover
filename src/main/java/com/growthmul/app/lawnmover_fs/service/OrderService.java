package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.OrderDto;
import com.growthmul.app.lawnmover_fs.dto.OrderItemRequest;
import com.growthmul.app.lawnmover_fs.dto.OrderSubmitRequest;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.entity.Order;
import com.growthmul.app.lawnmover_fs.entity.OrderItem;
import com.growthmul.app.lawnmover_fs.entity.Product;
import com.growthmul.app.lawnmover_fs.repository.OrderRepository;
import com.growthmul.app.lawnmover_fs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private PublicTenantResolver tenantResolver;

    // ───────────────────────── PUBLIC ───────────────────────── //

    public void submitOrder(String origin, OrderSubmitRequest req) {
        Company company = tenantResolver.resolve(origin);

        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your cart is empty");
        }

        Order order = new Order();
        order.setFirstName(req.getFirstName());
        order.setLastName(req.getLastName());
        order.setEmail(req.getEmail());
        order.setPhone(req.getPhone());
        order.setFulfillment(req.getFulfillment());
        order.setAddress(req.getAddress());
        order.setPreferredDate(req.getPreferredDate());
        order.setNotes(req.getNotes());
        order.setCompany(company);

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemReq : req.getItems()) {
            if (itemReq.getProductId() == null || itemReq.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item in cart");
            }
            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown product in cart"));
            // Same cross-tenant guard as BookingService: never let an order
            // attribute a purchase (and its revenue) to another business's product.
            if (!product.getCompany().getId().equals(company.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown product in cart");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setProductName(product.getName());
            item.setProductPrice(product.getPrice());
            items.add(item);
        }
        order.setItems(items);

        orderRepo.save(order);
    }

    // ───────────────────────── ADMIN ───────────────────────── //

    public List<OrderDto> getOrders(Long companyId) {
        return orderRepo.findByCompanyIdOrderBySubmittedAtDesc(companyId)
                .stream().map(OrderDto::from).toList();
    }

    public void completeOrder(Long companyId, Long id) {
        Order order = ownedOrThrow(companyId, id);
        order.setCompleted(true);
        orderRepo.save(order);
    }

    public void reopenOrder(Long companyId, Long id) {
        Order order = ownedOrThrow(companyId, id);
        order.setCompleted(false);
        orderRepo.save(order);
    }

    public void deleteOrder(Long companyId, Long id) {
        Order order = ownedOrThrow(companyId, id);
        orderRepo.delete(order);
    }

    private Order ownedOrThrow(Long companyId, Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        if (!order.getCompany().getId().equals(companyId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your order");
        }
        return order;
    }
}