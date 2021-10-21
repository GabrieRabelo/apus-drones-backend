package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.service.ProfileService;
import com.apus.drones.apusdronesbackend.service.UserService;
import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserAPI {

    private final ProfileService profileService;

    public UserAPI(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getById() {
        log.info("Getting an user profile.");
        return ResponseEntity.ok(profileService.getById());
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserDTO> update(@RequestBody @Validated UserDTO userDTO) {
        return ResponseEntity.ok(profileService.update(userDTO));
    }

}
