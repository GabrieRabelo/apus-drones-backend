package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.UserDTO;

public interface UserService {

    UserDTO getById(Long userId);

}
