package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.response.OrderResponse;
import com.apus.drones.apusdronesbackend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable Long userId) {
        log.info("Getting a list of orders.");
        return ResponseEntity.ok(orderService.findAllByCustomerId(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        log.info("Getting an order.");
        return ResponseEntity.ok(orderService.findById(orderId));
    }

    @PutMapping()
    public ResponseEntity<OrderResponse> update(@RequestBody OrderResponse orderResponse) {

        return ResponseEntity.ok(orderService.update(orderResponse));
    }
}
