package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.mapper.TripDTOMapper;
import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.TripEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.OrderRepository;
import com.apus.drones.apusdronesbackend.repository.TripRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateTripDTO;
import com.apus.drones.apusdronesbackend.service.dto.TripDTO;
import com.apus.drones.apusdronesbackend.utils.PermissionsChecker;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {
    TripRepository tripRepository;
    OrderRepository orderRepository;
    UserRepository userRepository;

    public TripServiceImpl(TripRepository tripRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TripDTO> getByPilot() {
        var userDetails = PermissionsChecker.checkPermissions(Role.PILOT);
        var trips = tripRepository.findAllByPilot_Id(userDetails.getUserID());
        return trips.stream().map(TripDTOMapper::fromTripEntity).collect(Collectors.toList());
    }

    @Override
    public TripDTO get(Long tripId) {
        var trip = this.getTrip(tripId);
        return TripDTOMapper.fromTripEntity(trip);
    }

    @Override
    public TripDTO create(CreateTripDTO createTripDTO) {
        var userDetails = PermissionsChecker.checkPermissions(List.of(Role.PILOT));

        OrderEntity order = Optional.of(this.orderRepository.findById(createTripDTO.getOrderId())).get().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido não encontrado")
        );
        UserEntity pilot = Optional.of(this.userRepository.findById(userDetails.getUserID())).get().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Piloto não encontrado")
        );
        if (pilot.getRole() != Role.PILOT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Piloto não encontrado");
        }

        TripEntity tripEntity = TripEntity.builder()
                .pilot(pilot)
                .order(order)
                .collectedAt(createTripDTO.getCollectedAt())
                .build();

        order.setStatus(OrderStatus.PICKING_UP);

        var savedEntity = tripRepository.save(tripEntity);
        return TripDTOMapper.fromTripEntity(savedEntity);
    }

    @Override
    public TripDTO setCollected(Long tripId) {
        var trip = this.getTrip(tripId);
        trip.setCollectedAt(LocalDateTime.now());
        trip.getOrder().setStatus(OrderStatus.IN_FLIGHT);
        var saved = tripRepository.save(trip);
        return TripDTOMapper.fromTripEntity(saved);
    }

    @Override
    public TripDTO setDelivered(Long tripId) {
        var trip = this.getTrip(tripId);
        trip.setDeliveredAt(LocalDateTime.now());
        trip.getOrder().setStatus(OrderStatus.DELIVERED);
        var saved = tripRepository.save(trip);
        return TripDTOMapper.fromTripEntity(saved);
    }

    public TripEntity getTrip(Long tripId) {
        PermissionsChecker.checkPermissions(List.of(Role.PILOT));

        Optional<TripEntity> oTrip = tripRepository.findById(tripId);
        if (oTrip.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Corrida não encontrada");
        }
        return oTrip.get();
    }
}
;