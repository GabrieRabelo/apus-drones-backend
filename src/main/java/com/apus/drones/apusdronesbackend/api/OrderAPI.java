package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.service.OrderSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderAPI {

    private final OrderSerivce orderSerivce;

    public OrderAPI(OrderSerivce orderSerivce) {
        this.orderSerivce = orderSerivce;
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<OrderEntity>> getOrders(@PathVariable Long userId) {
        log.info("Getting a list of orders.");
        return ResponseEntity.ok(orderSerivce.findAllByCustomerId(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable Long orderId) {
        log.info("Getting an order.");
        return ResponseEntity.of(orderSerivce.findById(orderId));
    }
}
