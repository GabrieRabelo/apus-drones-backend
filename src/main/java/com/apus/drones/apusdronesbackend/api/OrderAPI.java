package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.service.OrderService;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
import com.apus.drones.apusdronesbackend.service.dto.UpdateCartDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderAPI {

    private final OrderService orderService;

    public OrderAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam(required = false) OrderStatus status) {
        log.info("Getting a list of orders.");
        return ResponseEntity.ok(orderService.getByCustomerId(status));
    }

    @GetMapping("/to-collect")
    public ResponseEntity<List<OrderDTO>> getOrdersWaitingForPilot() {
        log.info("Getting a list of orders waiting for collect");
        return ResponseEntity.ok(orderService.findAllWaitingForPilot());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        log.info("Getting an order.");
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @PutMapping()
    public ResponseEntity<OrderDTO> update(@RequestBody OrderDTO orderDto) {
        log.info(Objects.nonNull(orderDto.getId()) ? "Updating an order." : "Creating an order.");
        OrderDTO updatedOrder = orderService.update(orderDto);
        if (updatedOrder == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/cart")
    public ResponseEntity<Void> addToCart(@RequestBody UpdateCartDTO updateCartDTO) {
        orderService.addToCart(updateCartDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<OrderDTO> getCart() {
        return ResponseEntity.ok(orderService.getCart());
    }
}
