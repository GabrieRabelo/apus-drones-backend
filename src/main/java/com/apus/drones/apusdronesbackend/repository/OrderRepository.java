package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByCustomer_Id(Long id);
}
