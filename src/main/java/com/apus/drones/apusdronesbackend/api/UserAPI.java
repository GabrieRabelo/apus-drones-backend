package com.apus.drones.apusdronesbackend.api;

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

    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long userId) {
        log.info("Getting an user profile.");
        return ResponseEntity.ok(userService.getById(userId));
    }

}
