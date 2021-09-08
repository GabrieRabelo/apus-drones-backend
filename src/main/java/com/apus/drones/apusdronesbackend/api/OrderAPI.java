package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.service.OrderSerivce;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderAPI {

    private final OrderSerivce orderSerivce;

    public OrderAPI(OrderSerivce orderSerivce) {
        this.orderSerivce = orderSerivce;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderEntity>> getPartners(@RequestParam Long userId) {
        log.info("Getting a list of orders.");
        return ResponseEntity.ok(orderSerivce.findAllByCustomerId(userId));
    }
}
