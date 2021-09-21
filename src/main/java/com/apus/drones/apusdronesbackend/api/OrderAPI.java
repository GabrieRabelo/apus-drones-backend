package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.service.OrderService;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderAPI {

    private final OrderService orderService;

    public OrderAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable Long userId, @RequestParam(required = false) OrderStatus status) {
        log.info("Getting a list of orders.");
        return ResponseEntity.ok(orderService.getByCustomerId(userId, status));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        log.info("Getting an order.");
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @PutMapping()
    public ResponseEntity<OrderDTO> update(@RequestBody OrderDTO orderDto) {
        log.info("Updating an order.");
        return ResponseEntity.ok(orderService.update(orderDto));
    }
}
