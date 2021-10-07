package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.ProfileService;
import com.apus.drones.apusdronesbackend.service.UserService;
import com.apus.drones.apusdronesbackend.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserAPI {

    private final ProfileService profileService;

    public UserAPI(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long userId) {
        log.info("Getting an user profile.");
        return ResponseEntity.ok(profileService.getById(userId));
    }

}
