package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.service.OrderService;
import com.apus.drones.apusdronesbackend.service.PartnerService;
import com.apus.drones.apusdronesbackend.service.ProductService;
import com.apus.drones.apusdronesbackend.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partners")
@Slf4j
public class PartnerAPI {

    private final PartnerService partnerService;
    private final ProductService productService;
    private final OrderService orderService;

    public PartnerAPI(PartnerService partnerService, ProductService productService, OrderService orderService) {
        this.partnerService = partnerService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<CreatePartnerResponseDTO> create(@RequestBody @Validated CreatePartnerDTO createPartnerDTO) {
        return ResponseEntity.ok(partnerService.create(createPartnerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.get(id));
    }

    @PatchMapping
    public ResponseEntity<PartnerDTO> update(@RequestBody @Validated CreatePartnerDTO updatePartnerDTO) {
        return ResponseEntity.ok(partnerService.update(updatePartnerDTO));
    }

    @GetMapping
    public ResponseEntity<List<PartnerDTO>> getPartners() {
        log.info("Getting a list of partners.");
        return ResponseEntity.ok(partnerService.findAllPartners());
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findAllProductsByPartnerId() {
        var response = productService.findAllActiveProductsByUser();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> findAllOrdersByPartnerIdFilterStatus(
        @RequestParam(required = false) OrderStatus status) {
        var response = orderService.findAllByPartnerIdAndFilterByStatus(status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        partnerService.delete();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("'%s' should be a valid '%s' and '%s' isn't", name, type, value);

        System.out.println(message);
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @PatchMapping("/{partnerId}/approve")
    public ResponseEntity<Object> changeStatus(@PathVariable Long partnerId,
                                               @RequestBody @Validated PartnerApprovedDTO partnerApprovedDTO) {
        var response = partnerService.changeApprovalStatus(partnerId, partnerApprovedDTO);

        return ResponseEntity.ok(response);
    }
}
