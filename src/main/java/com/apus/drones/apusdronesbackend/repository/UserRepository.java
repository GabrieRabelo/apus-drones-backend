package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByRole(Role role);
    List<UserEntity> findAllByRoleAndNameContainingIgnoreCase(Role role, String name);
    UserEntity findByEmail(String email);
    Optional<UserEntity> findAllByIdAndRole(Long id, Role role);
}
