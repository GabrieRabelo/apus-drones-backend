package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.PartnerService;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partners")
@Slf4j
public class PartnerAPI {

    private final PartnerService partnerService;

    public PartnerAPI(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public ResponseEntity<List<PartnerDTO>> getPartners() {
        log.info("Getting a list of partners.");
        return ResponseEntity.ok(partnerService.findAllPartners());
    }
}
