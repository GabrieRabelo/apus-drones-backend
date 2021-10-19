package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUser_Id(Long id);
}
