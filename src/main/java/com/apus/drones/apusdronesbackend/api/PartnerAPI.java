package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.PartnerService;
import com.apus.drones.apusdronesbackend.service.ProductService;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partners")
@Slf4j
public class PartnerAPI {

    private final PartnerService partnerService;
    private final ProductService productService;

    public PartnerAPI(PartnerService partnerService, ProductService productService) {
        this.partnerService = partnerService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<PartnerDTO>> getPartners() {
        log.info("Getting a list of partners.");
        return ResponseEntity.ok(partnerService.findAllPartners());
    }

    @GetMapping("/{partnerId}/products")
    public ResponseEntity<List<ProductDTO>> findAllProductsByPartnerId(@PathVariable Long partnerId) {
        var response = productService.findAllActiveProductsByUserId(partnerId);

        return ResponseEntity.ok(response);
    }
}
