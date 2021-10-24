package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.UserDTO;

public interface ProfileService {

    UserDTO getById();
    UserDTO update(UserDTO userDTO);

}


