package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByCustomer_IdAndStatus(Long id, OrderStatus status);

    List<OrderEntity> findAllByCustomer_IdAndStatusIsNotIn(Long id, List<OrderStatus> statuses);

    List<OrderEntity> findAllByPartner_Id(Long id);
    List<OrderEntity> findAllByPartner_IdAndStatus(Long id, OrderStatus status);
}
