package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.TripService;
import com.apus.drones.apusdronesbackend.service.dto.CreateTripDTO;
import com.apus.drones.apusdronesbackend.service.dto.TripDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@Slf4j
public class TripAPI {
    private final TripService tripService;

    public TripAPI(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("")
    public ResponseEntity<TripDTO> createTrip(@RequestBody @Validated CreateTripDTO createTripDTO) {
        log.info("Creating trip");
        return ResponseEntity.ok(tripService.create(createTripDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getById(@PathVariable Long id) {
        log.info("Getting trip " + id);
        return ResponseEntity.ok(tripService.get(id));
    }

    @GetMapping("/by-pilot")
    public ResponseEntity<List<TripDTO>> getTrips() {
        log.info("Getting a list of trips.");
        return ResponseEntity.ok(tripService.getByPilot());
    }

    @PatchMapping("/{id}/collected")
    public ResponseEntity<TripDTO> setCollected(@PathVariable Long id) {
        log.info("Getting trip " + id);
        return ResponseEntity.ok(tripService.setCollected(id));
    }

    @PatchMapping("/{id}/delivered")
    public ResponseEntity<TripDTO> setDelivered(@PathVariable Long id) {
        log.info("Getting trip " + id);
        return ResponseEntity.ok(tripService.setDelivered(id));
    }
}
