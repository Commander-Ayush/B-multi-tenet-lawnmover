package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.OrderDto;
import com.growthmul.app.lawnmover_fs.dto.OrderSubmitRequest;
import com.growthmul.app.lawnmover_fs.repository.OrderRepository;
import com.growthmul.app.lawnmover_fs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public void submitOrder(@RequestHeader(value = "Origin", required = false) String origin,
                            @RequestBody OrderSubmitRequest req) {
        orderService.submitOrder(origin, req);
    }
}